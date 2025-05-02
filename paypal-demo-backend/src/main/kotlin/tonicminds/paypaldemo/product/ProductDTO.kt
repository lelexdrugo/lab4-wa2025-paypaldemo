package tonicminds.paypaldemo.product

data class ProductDTO (
    val name: String,
    val price: Double,
    val id : Long?=null,
    val imageUrl: String
)