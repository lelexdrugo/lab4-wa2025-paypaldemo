package tonicminds.paypaldemo.product

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = ["*"])
class ProductController(private val productService: ProductService) {
    @GetMapping("/","")
    fun getProducts():List<ProductDTO>{
        return  productService.getProducts()
    }
    @GetMapping("/{id}")
    fun getProductById( @PathVariable id: Long): ProductDTO? {
        return productService.getProduct(id)
    }
    @PostMapping("/","")
    fun addProduct(@RequestBody productDTO: ProductDTO): Long {
        return productService.addProduct(productDTO);
    }

}