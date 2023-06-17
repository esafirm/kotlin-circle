package nolambda.stream.circle

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

/**
 * Create a fixed thread pool dispatcher with the given number of threads.
 */
internal actual fun _createFixedDispatcher(executorCount: Int): CoroutineDispatcher {
    return Executors.newFixedThreadPool(executorCount).asCoroutineDispatcher()
}
