package tonicminds.paypaldemo.cart.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CartRestControllerAdvice {

    @ExceptionHandler(CartNotFoundException::class)
    fun handleCartNotFound(ex: CartNotFoundException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = ex.message ?: "Cart not found"
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    data class ErrorResponse(
        val status: Int,
        val message: String
    )
}