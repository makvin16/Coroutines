package coroutines

@Deprecated(message = "This is internal API and may be removed in the future releases")
abstract class JobSupport(active: Boolean) : Job, ChildJob, ParentJob {
    final override val key: CoroutineContext.Key<*> get() = Job
}

internal class JobImpl(parent: Job?) : JobSupport(true), CompletableJob {


    override fun complete(): Boolean {
        TODO("Not yet implemented")
    }

    override fun completeExceptionally(exception: Throwable): Boolean {
        TODO("Not yet implemented")
    }
}