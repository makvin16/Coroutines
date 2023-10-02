package coroutines

interface CoroutineContext {

    operator fun <E : Element> get(key: Key<E>): E?

    fun <R> fold(initial: R, operation: (R, Element) -> R): R

    operator fun plus(context: CoroutineContext): CoroutineContext
//    {
//        val b = context.fold("") { a, b ->
//            return@fold ""
//        }
//        return if (context === EmptyCoroutineContext) this else
//            context.fold(this) { acc, element ->
//                val removed = acc.minusKey(element.key)
//                if (removed === EmptyCoroutineContext) element else {
//                    val interceptor = removed[]
//                }
//            }
//    }

    fun minusKey(key: Key<*>): CoroutineContext

    interface Key<E : Element>

    interface Element : CoroutineContext {
        val key: Key<*>

        override fun <E : Element> get(key: Key<E>): E? {
            @Suppress("UNCHECKED_CAST")
            return if (this.key == key) this as E else null
        }
    }
}