package tonicminds.paypaldemo.cart

import com.paypal.sdk.models.OrderStatus
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tonicminds.paypaldemo.cart.exception.CartNotFoundException
import tonicminds.paypaldemo.outbox.PayPalOutboxService
import tonicminds.paypaldemo.paypal.PayPalService
import java.util.*
import kotlin.jvm.optionals.getOrElse

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = ["*"])
class CartController(
    private val cartService: CartService,
    private val payPalService: PayPalService,
    private val payPalOutboxService: PayPalOutboxService
) {
    companion object {
        private const val SESSION_CART_ID = "CART_ID"
    }
    @PostMapping("/add")
    fun addToCart(@RequestParam productId: Long, @RequestParam(defaultValue = "1") quantity: Int, httpSession: HttpSession): ResponseEntity<*> {
        val cartId = httpSession.getAttribute(SESSION_CART_ID) as? Long
        cartService.addToCart(productId, quantity, cartId)
        return ResponseEntity.ok("Added to cart")
    }

    @GetMapping("","/")
    fun viewCart(httpSession: HttpSession): Cart? {
        val cartId = httpSession.getAttribute(SESSION_CART_ID) as? Long
        val cart = cartService.getOrCreateCart(cartId)
        httpSession.setAttribute(SESSION_CART_ID,cart.id)
        return cart
    }

    @PostMapping("/paypal/create")
    fun createPayPalOrder(
        httpSession: HttpSession,
      //  response: HttpServletResponse
    ) : String {
        val cartId = httpSession.getAttribute(SESSION_CART_ID) as? Long
        if(cartId == null) {
            throw RuntimeException("cartId is empty")
        }
        val cart = cartService.getCart(cartId).get()
        val order = payPalService.createOrder(cart)!!
        cartService.setInProgress(cart.id!!,order.id);
        println(order.id);
       // response.sendRedirect(order.links!![1].href)
        return order.links!![1].href
    }

    @GetMapping("/paypal/capture")
    fun capturePayPalOrder(
        @RequestParam("token") token: String,
        @RequestParam("PayerID") payerID: String,
        httpSession: HttpSession,
        response: HttpServletResponse
    ): String {
        // Get the cart by token
        val cart = cartService.getCartByToken(token).getOrElse { 
            throw CartNotFoundException("Order not found") 
        }

        // Set the cart status to PAID
        cartService.setPaid(token)

        // Create an outbox event for the PayPal order capture
        payPalOutboxService.createPayPalCaptureEvent(token, payerID, cart.id!!)

        // Remove the cart ID from the session
        httpSession.removeAttribute(SESSION_CART_ID)

        // Redirect to the order page
        response.sendRedirect("/order/${token}")

        return "redirect:/order/${token}"
    }


    @GetMapping("/paypal/cancel")
    fun cancelPayPalOrder(@RequestParam("token") token: String,response: HttpServletResponse,httpSession: HttpSession): String {
        cartService.cancel(token)
        httpSession.removeAttribute(SESSION_CART_ID)
        response.sendRedirect("/order/${token}")
        return "redirect:/order/${token}"
    }

    @DeleteMapping("","/")
    fun deleteCart(httpSession: HttpSession){
        val cartId = httpSession.getAttribute(SESSION_CART_ID) as? Long
        cartService.clearCart(cartId)
        httpSession.removeAttribute(SESSION_CART_ID)
    }


    @GetMapping("/paypal/order/{token}")
    fun getPaypalOrder(@PathVariable token: String): Cart {
        val cart = cartService.getCartByToken(token).getOrElse { throw CartNotFoundException("Order not found") }
        if (cart.status==CartStatus.TO_FILL)
            throw CartNotFoundException("Order not found")
        return cart
    }


}
