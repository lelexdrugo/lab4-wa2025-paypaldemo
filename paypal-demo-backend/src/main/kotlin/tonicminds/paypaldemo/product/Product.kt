package tonicminds.paypaldemo.product

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id


@Entity
class Product (

     val name: String,
     val price: Double,  // Optional: imageUrl, description, stock, etc.
     val imageUrl: String
){
    fun toDTO(): ProductDTO? {
        return ProductDTO(
            name = name,
            price = price,
            id = id,
            imageUrl = imageUrl
        )
    }

    @Id
    @GeneratedValue
    val id: Long = 0
}