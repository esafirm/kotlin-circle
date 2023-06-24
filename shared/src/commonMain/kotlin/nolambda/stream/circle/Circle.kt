package nolambda.stream.circle

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

/**
 * A namespace for components provided by Circle
 *
 * This is just for convenience purpose so the dev does not need to remember anything beside this class
 */
object Circle {

    /**
     * Load a local image that located inside `{common_source_set}/resources`
     */
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun LocalImage(
        imageResourceName: String,
        modifier: Modifier = Modifier,
        contentDescription: String? = null
    ) {
        Image(
            painterResource(imageResourceName),
            null,
            modifier
        )
    }

    /**
     * Create Ktor HTTP Client with common JSON configuration
     */
    fun createHttpClient(
        baseUrl: String,
        tokenProvider: () -> String = DEFAULT_TOKEN_PROVIDER,
        extendConfig: HttpClientConfig<*>.() -> Unit = {}
    ): HttpClient {
        return HttpClient {
            defaultRequest {
                url(baseUrl)

                // Handle token
                if (tokenProvider != DEFAULT_TOKEN_PROVIDER) {
                    val token = tokenProvider()
                    header(HttpHeaders.Authorization, "Bearer $token")
                }

                headers.appendIfNameAbsent(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    useAlternativeNames = false
                    encodeDefaults = true
                })
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.BODY
            }

            extendConfig(this)
        }
    }

    object Coroutine {

        /**
         * Create a single thread dispatcher
         */
        fun createSingleThreadDispatcher() = createFixedDispatcher(1)

        /**
         * Create a dispatcher with a fixed thread/executor
         */
        fun createFixedDispatcher(executorCount: Int) = _createFixedDispatcher(executorCount)
    }

    /**
     * A persistent key-value storage
     */
    class SharedPreference(
        private val settings: Settings = _getDefaultSettings()
    ) {
        fun set(key: String, value: Any) {
            settings[key] = value
        }

        fun getInt(key: String): Int? {
            return settings.getIntOrNull(key)
        }

        fun getDouble(key: String): Double? {
            return settings.getDoubleOrNull(key)
        }

        fun getFloat(key: String): Float? {
            return settings.getFloatOrNull(key)
        }

        fun getString(key: String): String? {
            return settings.getStringOrNull(key)
        }

        fun getBoolean(key: String): Boolean? {
            return settings.getBooleanOrNull(key)
        }

        fun remove(key: String) {
            settings.remove(key)
        }

        fun clear() {
            settings.clear()
        }
    }
}

/* --------------------------------------------------- */
/* > Internal fields */
/* --------------------------------------------------- */

private val DEFAULT_TOKEN_PROVIDER: () -> String = { "" }

/**
 * Create a fixed thread pool dispatcher with the given number of threads.
 */
internal expect fun _createFixedDispatcher(executorCount: Int): CoroutineDispatcher

internal expect fun _getDefaultSettings(): Settings
