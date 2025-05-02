package tonicminds.paypaldemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PaypalDemoApplication

fun main(args: Array<String>) {
    runApplication<PaypalDemoApplication>(*args)
}
