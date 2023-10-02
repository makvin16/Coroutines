package coroutines

interface Job : CoroutineContext.Element {

    companion object Key : CoroutineContext.Key<Job>
}