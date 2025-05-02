package tonicminds.paypaldemo.cart

import org.springframework.stereotype.Service
import tonicminds.paypaldemo.product.ProductRepository
import java.util.*
import org.springframework.transaction.annotation.Transactional

@Service
class CartService(
    private val cartRepo: CartRepository,
    private val productRepo: ProductRepository
) {


    fun getOrCreateCart(cartId: Long?): Cart {

        val cart = if (cartId != null) {
            cartRepo.findById(cartId).orElseGet {
                val newCart = cartRepo.save(Cart())
                newCart
            }
        } else {
            val newCart = cartRepo.save(Cart())
            newCart
        }

        return cart
    }

    fun getCart(cartId: Long?): Optional<Cart> {
        return cartId?.let { cartRepo.findById(it) } ?: Optional.empty()
    }

    fun getCartByToken(token:String): Optional<Cart> {
        return cartRepo.findByToken(token)
    }


    @Transactional
    fun addToCart(productId: Long, quantity: Int, cartId: Long?) {
        val product = productRepo.findById(productId).orElseThrow()
        val cart = getOrCreateCart(cartId)

        val existingItem = cart.items.find { it.product?.id == productId }

        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            val item = CartItem(
                product = product,
                quantity = quantity,
                cart = cart
            )
            cart.items.add(item)
        }
    }

    @Transactional
    fun setPaid(token: String) {
        cartRepo.findByToken(token).get().status = CartStatus.PAID
    }

    @Transactional
    fun setCompleted(token: String) {
        cartRepo.findByToken(token).get().status = CartStatus.COMPLETED
    }

    fun getStatus(token: String) : CartStatus{
        return cartRepo.findByToken(token).get().status
    }

    @Transactional
    fun cancel(token: String) {
        val cart = cartRepo.findByToken(token).get()
        cart.status=CartStatus.CANCELLED
    }

    @Transactional
    fun setInProgress(cartId: Long, token: String) {
        var cart = cartRepo.findById(cartId).get()
        if(cart.status==CartStatus.PAID || cart.status==CartStatus.CANCELLED)
            return
        cart.token=token
        cart.status=CartStatus.IN_PROGRESS
    }


    fun clearCart(cartId: Long?) {

        val cart = cartId?.let { cartRepo.findById(it).orElse(null) }

        if (cart == null) {
           return;
        }

        if(cart.status!= CartStatus.TO_FILL) {
            return
        }

        cartRepo.deleteById(cartId);


    }
}
