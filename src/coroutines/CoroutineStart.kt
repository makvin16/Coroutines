package coroutines

import kotlin.coroutines.Continuation

enum class CoroutineStart {
    DEFAULT,
    LAZY,
    ATOMIC,
    UNDISPATCHED;

    val isLazy: Boolean get() = this === LAZY

    operator fun <T> invoke(block: suspend () -> T, completion: Continuation<T>) {}
}