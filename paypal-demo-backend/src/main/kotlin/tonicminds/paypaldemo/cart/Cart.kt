package tonicminds.paypaldemo.cart

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*


@Entity
class Cart(


    @OneToMany(mappedBy = "cart", cascade = [CascadeType.ALL], orphanRemoval = true)
    val items: MutableList<CartItem> = ArrayList(),
) {
    @Id
    @GeneratedValue
    val id: Long? = null
    @JsonIgnore
    var token : String? = null

    var status: CartStatus = CartStatus.TO_FILL
}

enum class CartStatus {
    CANCELLED,
    IN_PROGRESS,
    PAID,
    TO_FILL,
    COMPLETED
}
