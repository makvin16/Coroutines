package coroutines

enum class CoroutineStart {
    DEFAULT,
    LAZY,
    ATOMIC,
    UNDISPATCHED;

    val isLazy: Boolean get() = this === LAZY

    operator fun <T> invoke(block: () -> T, completion: Continuation<T>) {
        when (this) {
            DEFAULT -> TODO()
            ATOMIC -> TODO()
            UNDISPATCHED -> TODO()
            LAZY -> TODO()
        }
    }

    operator fun <R, T> invoke(block: R.() -> T, receiver: R, completion: Continuation<T>) {
        when (this) {
            DEFAULT -> block.startCoroutineCancellable(receiver, completion)
            ATOMIC -> TODO()
            UNDISPATCHED -> TODO()
            LAZY -> TODO()
        }
    }
}