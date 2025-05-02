package tonicminds.paypaldemo.paypal

import com.paypal.sdk.PaypalServerSdkClient
import com.paypal.sdk.models.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import tonicminds.paypaldemo.cart.Cart
import java.util.*


@Service
class PayPalService(
    private val payPalClient: PaypalServerSdkClient,
    @Value("\${paypal.currency}") private val currency: String,
    @Value("\${paypal.return-url}") private val returnUrl :String,
    @Value("\${paypal.cancel-url}") private val cancelUrl : String,
    ) {

    /**
     * Creates a PayPal order for cart checkout
     */
    fun createOrder(
        cartInfo: Cart
    ): Order? {
        val orderRequest = OrderRequest().apply {
            intent = CheckoutPaymentIntent.CAPTURE
        }

        val items = cartInfo.items.map {
            val productName = it.product?.name ?: "Unknown"
            val pricePerUnit = it.product?.price ?: 0.0
            val quantity = it.quantity

            Item().apply {
                name = productName
                unitAmount = Money().apply {
                    currencyCode = currency
                    value = String.format(Locale.US, "%.2f", pricePerUnit)
                }
                this.quantity = quantity.toString()
            }
        }

        val itemTotalValue = cartInfo.items.sumOf { (it.product?.price ?: 0.0) * it.quantity }

        val amount = AmountWithBreakdown().apply {
            currencyCode = currency
            value = String.format(Locale.US, "%.2f", itemTotalValue)
            breakdown = AmountBreakdown().apply {
                itemTotal = Money().apply {
                    currencyCode = currency
                    value = String.format(Locale.US, "%.2f", itemTotalValue)
                }
            }
        }


        val purchaseUnitRequest = PurchaseUnitRequest().apply {
            this.amount = amount
            this.items = items
        }



        orderRequest.purchaseUnits = listOf(purchaseUnitRequest)

        val applicationContext = OrderApplicationContext()
        applicationContext.returnUrl = returnUrl
        applicationContext.cancelUrl = cancelUrl

        orderRequest.applicationContext = applicationContext

        val createOrderInput = CreateOrderInput();
        createOrderInput.body = orderRequest;

        try{
            val response = payPalClient.ordersController.createOrder(createOrderInput)
            val order = response.result

           return order
        }catch (e: Exception){
            println(e.stackTrace)
            return  null;
        }
    }

    /**
     * Captures payment for an approved order
     */
    fun captureOrder(token: String, payerId: String): Order {

        val paymentSource = OrderCaptureRequestPaymentSource ()
        paymentSource.token=Token(token,TokenType.BILLING_AGREEMENT);


        val captureOrderRequest = OrderCaptureRequest()
        captureOrderRequest.paymentSource = paymentSource


        val captureOrderInput = CaptureOrderInput();
        captureOrderInput.body = captureOrderRequest;
        captureOrderInput.id = token

        val result = payPalClient.ordersController.captureOrder(captureOrderInput)
        return result.result
    }


}

class PayPalException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)