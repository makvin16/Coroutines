package coroutines

enum class CoroutineStart {
    DEFAULT,
    LAZY,
    ATOMIC,
    UNDISPATCHED;

    val isLazy: Boolean get() = this === LAZY

    operator fun <T> invoke(completion: Continuation<T>) {}
}