@file:Suppress("DEPRECATION")

package nolambda.stream.circle

import android.preference.PreferenceManager
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

/**
 * Create a fixed thread pool dispatcher with the given number of threads.
 */
internal actual fun _createFixedDispatcher(executorCount: Int): CoroutineDispatcher {
    return Executors.newFixedThreadPool(executorCount).asCoroutineDispatcher()
}

/**
 * Create a default shared preference using app [android.content.Context]
 */
internal actual fun _getDefaultSettings(): Settings {
    val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CircleHolder.context)
    return SharedPreferencesSettings(sharedPrefs)
}