package tonicminds.paypaldemo.outbox

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PayPalOutboxService(private val outboxRepository: PayPalOutboxRepository) {

    @Transactional
    fun createPayPalCaptureEvent(paypalToken: String, payerId: String, cartId: Long) {
        val event = PayPalOutboxEvent(
            paypalToken = paypalToken,
            payerId = payerId,
        )
        val cdcOptimization = outboxRepository.save(event)
        outboxRepository.delete(cdcOptimization)
    }

}