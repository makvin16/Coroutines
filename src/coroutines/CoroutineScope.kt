package coroutines

interface CoroutineScope {
    val coroutineContext: CoroutineContext
}

object GlobalScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = EmptyCoroutineContext
}