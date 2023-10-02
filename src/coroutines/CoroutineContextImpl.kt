package coroutines

import java.io.Serializable

object EmptyCoroutineContext : CoroutineContext, Serializable {

    override fun <E : CoroutineContext.Element> get(key: CoroutineContext.Key<E>): E? = null

    override fun <R> fold(initial: R, operation: (R, CoroutineContext.Element) -> R): R {
        return initial
    }

    override fun plus(context: CoroutineContext): CoroutineContext {
        return context
    }

    override fun minusKey(key: CoroutineContext.Key<*>): CoroutineContext {
        return this
    }

    override fun hashCode(): Int = 0

    override fun toString(): String = "EmptyCoroutineContext"
}