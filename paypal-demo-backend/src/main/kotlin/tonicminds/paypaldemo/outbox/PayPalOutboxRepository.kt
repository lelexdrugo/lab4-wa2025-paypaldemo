package tonicminds.paypaldemo.outbox

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PayPalOutboxRepository : JpaRepository<PayPalOutboxEvent, Long> {
}