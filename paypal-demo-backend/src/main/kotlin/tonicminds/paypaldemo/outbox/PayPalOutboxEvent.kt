package tonicminds.paypaldemo.outbox

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "paypal_outbox_events")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class PayPalOutboxEvent(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @Column(nullable = false)
    @JsonProperty("paypal_token")
    val paypalToken: String,

    @Column(nullable = false)
    @JsonProperty("payer_id")
    val payerId: String,

    @Column(nullable = false)
    @JsonProperty("created_at")
    val createdAt: Instant = Instant.now(),

    )