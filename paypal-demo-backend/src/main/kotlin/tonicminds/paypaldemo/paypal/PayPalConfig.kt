package tonicminds.paypaldemo.paypal

import com.paypal.sdk.Environment
import com.paypal.sdk.PaypalServerSdkClient
import com.paypal.sdk.authentication.ClientCredentialsAuthModel
import com.paypal.sdk.http.client.HttpClientConfiguration
import com.paypal.sdk.logging.configuration.ApiLoggingConfiguration
import com.paypal.sdk.logging.configuration.ApiRequestLoggingConfiguration
import com.paypal.sdk.logging.configuration.ApiResponseLoggingConfiguration
import org.slf4j.event.Level
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class PayPalConfig(
    @Value("\${paypal.client-id}") private val clientId: String,
    @Value("\${paypal.client-secret}") private val clientSecret: String
) {
    @Bean
    fun payPalClient(): PaypalServerSdkClient {
        return PaypalServerSdkClient.Builder()
            .loggingConfig { builder ->
                builder.level(Level.DEBUG)
                    .requestConfig { logConfigBuilder -> logConfigBuilder.body(true) }
                    .responseConfig { logConfigBuilder -> logConfigBuilder.headers(true) }
            }
            .httpClientConfig { configBuilder ->
                configBuilder.timeout(5000) // 5 seconds timeout
            }
            .clientCredentialsAuth(
                ClientCredentialsAuthModel.Builder(clientId, clientSecret).build()
            )
            .environment(
                Environment.SANDBOX
            )
            .build()
    }
}