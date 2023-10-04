package coroutines

fun CoroutineScope.newCoroutineContext(context: CoroutineContext): CoroutineContext {
    return context
}

/**
 * Постоянный контекст для корутины. Это индексированный набор из экземпляров Element.
 * Индексированный набор представляет собой смесь между set и map.
 * Каждый элемент в этом наборе имеет уникальный ключ.
 */
interface CoroutineContext {

    /**
     * Возвращает элемент с заданным ключом из этого контекста или значение null.
     */
    operator fun <E : Element> get(key: Key<E>): E?

    /**
     * Накапливает записи этого контекста,
     * начиная с начального значения и применяя операцию слева направо к текущему значению накопителя
     * и каждому элементу этого контекста.
     */
    fun <R> fold(initial: R, operation: (R, Element) -> R): R

    operator fun plus(context: CoroutineContext): CoroutineContext {
        return if (context === EmptyCoroutineContext) this else
            context.fold(this) { acc, element ->
                val removed = acc.minusKey(element.key)
                if (removed === EmptyCoroutineContext) element else {
                    val interceptor = removed[ContinuationInterceptor]
                    if (interceptor == null) CombinedContext(removed, element) else {
                        val left = removed.minusKey(ContinuationInterceptor)
                        if (left === EmptyCoroutineContext) CombinedContext(element, interceptor) else
                            CombinedContext(CombinedContext(left, element), interceptor)
                    }
                }
            }
    }

    /**
     * Возвращает контекст, содержащий элементы из этого контекста, но без элемента с указанным ключом.
     */
    fun minusKey(key: Key<*>): CoroutineContext

    /**
     * Ключ для элементов CoroutineContext. E — это тип элемента с этим ключом.
     */
    interface Key<E : Element>

    /**
     * Элемент CoroutineContext. Элемент корутин контекста является сам по себе синглтоном контекста
     */
    interface Element : CoroutineContext {
        val key: Key<*>

        override fun <E : Element> get(key: Key<E>): E? {
            println("Element method [get] $key")
            @Suppress("UNCHECKED_CAST")
            return if (this.key == key) this as E else null
        }

        override fun <R> fold(initial: R, operation: (R, Element) -> R): R {
            return operation(initial, this)
        }

        override fun minusKey(key: Key<*>): CoroutineContext {
            return if (this.key == key) EmptyCoroutineContext else this
        }
    }
}