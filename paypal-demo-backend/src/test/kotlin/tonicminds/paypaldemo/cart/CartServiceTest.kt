package tonicminds.paypaldemo.cart

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import tonicminds.paypaldemo.product.ProductRepository
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)
class CartServiceTest {

    @Mock
    private lateinit var cartRepository: CartRepository

    @Mock
    private lateinit var productRepository: ProductRepository

    @InjectMocks
    private lateinit var cartService: CartService

    @Test
    fun `getOrCreateCart should return existing cart when cartId is valid`() {
        // Arrange
        val cartId = 1L
        val existingCart = Cart()
        `when`(cartRepository.findById(cartId)).thenReturn(Optional.of(existingCart))

        // Act
        val result = cartService.getOrCreateCart(cartId)

        // Assert
        assertNotNull(result)
        assertEquals(existingCart, result)
        verify(cartRepository).findById(cartId)
        verify(cartRepository, never()).save(any())
    }

    @Test
    fun `getOrCreateCart should create new cart when cartId is null`() {
        // Arrange
        val newCart = Cart()
        `when`(cartRepository.save(any())).thenReturn(newCart)

        // Act
        val result = cartService.getOrCreateCart(null)

        // Assert
        assertNotNull(result)
        assertEquals(newCart, result)
        verify(cartRepository, never()).findById(any())
        verify(cartRepository).save(any())
    }
}