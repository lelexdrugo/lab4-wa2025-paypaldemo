package tonicminds.paypaldemo.cart

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import tonicminds.paypaldemo.product.Product


@Entity
class CartItem(
    @ManyToOne
    var product: Product? = null,
    var quantity: Int,
    @ManyToOne
    @JsonIgnore
    var cart: Cart? = null
){
    @Id
    @GeneratedValue
    val id: Long? = null
}