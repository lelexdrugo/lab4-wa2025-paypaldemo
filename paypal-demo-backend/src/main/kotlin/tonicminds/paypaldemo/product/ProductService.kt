package tonicminds.paypaldemo.product

import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository,
    repository: ProductRepository
) {

    fun addProduct(productDTO: ProductDTO):Long{
        return productRepository.save(Product(productDTO.name,productDTO.price,productDTO.imageUrl)).id
    }

    fun getProduct(id:Long):ProductDTO?{
        return productRepository.findById(id).get().toDTO();
    }

    fun getProducts():List<ProductDTO>{
        return productRepository.findAll().map { it.toDTO()!! }
    }
}