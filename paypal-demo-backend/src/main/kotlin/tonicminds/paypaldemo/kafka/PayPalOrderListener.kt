package tonicminds.paypaldemo.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.paypal.sdk.models.OrderStatus
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import tonicminds.paypaldemo.cart.CartService
import tonicminds.paypaldemo.cart.CartStatus
import tonicminds.paypaldemo.outbox.PayPalOutboxEvent
import tonicminds.paypaldemo.outbox.PayPalOutboxRepository
import tonicminds.paypaldemo.outbox.PayPalOutboxService
import tonicminds.paypaldemo.paypal.PayPalService
import java.time.Duration

@Component
class PayPalOrderListener(
    private val payPalService: PayPalService,
    private val cartService: CartService,
    private val objectMapper: ObjectMapper
) {

    @KafkaListener(topics = ["paypal.public.paypal_outbox_events"], groupId = "\${spring.kafka.consumer.group-id:paypal-demo}")
    @Transactional
    fun processPayPalOrder(message: String, @Header(KafkaHeaders.ACKNOWLEDGMENT) ack: Acknowledgment) {
        try {
            if (message.isEmpty()) {
                ack.acknowledge()
                return;
            }
            // Parse the message from Kafka using the schema
            val eventData = objectMapper.readValue(message, PayPalOutboxEvent::class.java)

            val paypalToken = eventData.paypalToken
            val payerId = eventData.payerId
            val cartStatus = cartService.getStatus(paypalToken)

            when (cartStatus) {
                CartStatus.COMPLETED -> {
                    //already processed, ack the message
                    ack.acknowledge()
                }

                CartStatus.PAID -> {
                    //send http request, set db and ack the message
                    val order = payPalService.captureOrder(paypalToken, payerId)
                    if (order.status == OrderStatus.COMPLETED) {
                        cartService.setCompleted(paypalToken)
                        ack.acknowledge()
                    }
                }

                else -> {
                    println("Ignoring PayPal order event with status $cartStatus")
                    ack.acknowledge()
                }
            }
        } catch (e: Exception) {
            ack.nack(Duration.ofSeconds(5))
            // Log the error and don't acknowledge the message, so it will be retried
            println("Error processing PayPal order: ${e.message}")
        }
    }
}