package coroutines

interface CompletableJob : Job {

    fun complete(): Boolean

    fun completeExceptionally(exception: Throwable): Boolean
}