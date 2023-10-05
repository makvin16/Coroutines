package coroutines

interface ThreadContextElement<S> : CoroutineContext.Element {

    fun updateThreadContext(context: CoroutineContext): S

    fun restoreThreadContext(context: CoroutineContext, oldState: S)
}

interface CopyableThreadContextElement<S> : ThreadContextElement<S> {

    fun copyForChild(): CopyableThreadContextElement<S>

    fun mergeForChild(overwritingElement: CoroutineContext.Element): CoroutineContext
}