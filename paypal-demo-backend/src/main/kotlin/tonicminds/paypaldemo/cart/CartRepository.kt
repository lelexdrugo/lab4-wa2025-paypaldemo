package tonicminds.paypaldemo.cart

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CartRepository:JpaRepository<Cart,Long>{
    fun findByToken(token:String): Optional<Cart>
}