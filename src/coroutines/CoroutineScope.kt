package coroutines

interface CoroutineScope {
    val coroutineContext: CoroutineContext
}

object GlobalScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = EmptyCoroutineContext
}

fun CoroutineScope(context: CoroutineContext): CoroutineScope {
    println(context[Job])
    return ContextScope(context)
}