package coroutines

fun CoroutineScope.newCoroutineContext(context: CoroutineContext): CoroutineContext {
    println(coroutineContext)
    println(context)
    val combined = foldCopies(coroutineContext, context, true)
    //val debug = if (DEBUG) combined + CoroutineId(COROUTINE_ID.incrementAndGet()) else combined
    return if (combined !== Dispatchers.Default && combined[ContinuationInterceptor] == null)
        debug + Dispatchers.Default else debug
}

/**
 * Это набор параметров для выполнения корутин.
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

private fun foldCopies(originalContext: CoroutineContext, appendContext: CoroutineContext, isNewCoroutine: Boolean): CoroutineContext {
    // Do we have something to copy left-hand side?
    val hasElementsLeft = originalContext.hasCopyableElements()
    val hasElementsRight = appendContext.hasCopyableElements()

    // Nothing to fold, so just return the sum of contexts
    if (!hasElementsLeft && !hasElementsRight) {
        return originalContext + appendContext
    }

    var leftoverContext = appendContext
    val folded = originalContext.fold<CoroutineContext>(EmptyCoroutineContext) { result, element ->
        if (element !is CopyableThreadContextElement<*>) return@fold result + element
        // Will this element be overwritten?
        val newElement = leftoverContext[element.key]
        // No, just copy it
        if (newElement == null) {
            // For 'withContext'-like builders we do not copy as the element is not shared
            return@fold result + if (isNewCoroutine) element.copyForChild() else element
        }
        // Yes, then first remove the element from append context
        leftoverContext = leftoverContext.minusKey(element.key)
        // Return the sum
        @Suppress("UNCHECKED_CAST")
        return@fold result + (element as CopyableThreadContextElement<Any?>).mergeForChild(newElement)
    }
    if (hasElementsRight) {
        leftoverContext = leftoverContext.fold<CoroutineContext>(EmptyCoroutineContext) { result, element ->
            // We're appending new context element -- we have to copy it, otherwise it may be shared with others
            if (element is CopyableThreadContextElement<*>) {
                return@fold result + element.copyForChild()
            }
            return@fold result + element
        }
    }
    return folded + leftoverContext
}

private fun CoroutineContext.hasCopyableElements(): Boolean {
    return fold(false) { result, it -> result || it is CopyableThreadContextElement<*> }
}