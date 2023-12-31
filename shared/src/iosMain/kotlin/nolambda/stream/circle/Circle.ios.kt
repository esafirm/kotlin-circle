package nolambda.stream.circle

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import io.ktor.util.InternalAPI
import io.ktor.util.createFixedThreadDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Create a fixed thread pool dispatcher with the given number of threads.
 */
@OptIn(InternalAPI::class)
internal actual fun _createFixedDispatcher(executorCount: Int): CoroutineDispatcher {
    return Dispatchers.createFixedThreadDispatcher("working-dispatcher", executorCount)
}

/**
 * Create a default shared preference
 */
internal actual fun _getDefaultSettings(): Settings {
    val factory = NSUserDefaultsSettings.Factory()
    return factory.create("default")
}