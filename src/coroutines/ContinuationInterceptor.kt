package coroutines

import kotlin.coroutines.Continuation

interface ContinuationInterceptor : CoroutineContext.Element {

    companion object Key : CoroutineContext.Key<ContinuationInterceptor>

    fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T>

    fun releaseInterceptedContinuation(continuation: Continuation<*>) {
        /* do nothing by default */
    }

    override fun <E : CoroutineContext.Element> get(key: CoroutineContext.Key<E>): E? {
        return super.get(key)
    }

    override fun minusKey(key: CoroutineContext.Key<*>): CoroutineContext {
        TODO("Not yet implemented")
    }
}