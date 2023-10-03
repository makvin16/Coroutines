package coroutines

interface Job : CoroutineContext.Element {

    companion object Key : CoroutineContext.Key<Job>
}

@Suppress("FunctionName")
fun Job(parent: Job? = null): CompletableJob {
    return JobImpl(parent)
}

@Deprecated("This is internal API and may be removed in the future releases")
interface ChildJob : Job

@Deprecated("This is internal API and may be removed in the future releases")
interface ParentJob : Job