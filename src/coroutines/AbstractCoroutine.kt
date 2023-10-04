package coroutines

/**
 * Абстрактный базовый класс для реализации корутин в корутин билдерах.
 *
 * Этот класс реализует завершение интерфейсов Continuation, Job и CoroutineScope.
 * Он сохраняет результат continuation в состоянии job.
 * Эта корутина ожидает завершения дочерних корутин перед завершением и завершается сбоем в промежуточном состоянии сбоя
 *
 * Для переопределения доступны следующие методы:
 * - onStart вызывается, когда корутина была создана в неактивном состоянии и запускается.
 * - onCancelling вызывается, как только корутина начинает отменяться по какой-либо причине (или завершается).
 * - onCompleted вызывается, когда корутина завершается значением.
 * - onCancelled вызывается, когда корутина завершается с исключением (отменяется).
 *
 * Параметры:
 * parentContext — контекст родительской корутины.
 * initParentJob — указывает, следует ли создавать экземпляр родительско-дочерних отношений непосредственно в конструкторе AbstractCoroutine.
 * Если установлено значение false, дочерний класс несет ответственность за вызов initParentJob вручную.
 * active — если true (по умолчанию), корутина создается в активном состоянии, в противном случае она создается в новом состоянии.
 */
abstract class AbstractCoroutine<in T>(
    parentContext: CoroutineContext,
    initParentJob: Boolean,
    active: Boolean
) : JobSupport(active), Job, Continuation<T>, CoroutineScope {

    init {
        /**
         * Настройка родительско-дочерних отношений между родителем в контексте и текущей корутиной.
         * Это может привести к тому, что эта корутинка станет _отмененной_, если родительская корутина уже отменена.
         * Здесь опасно устанавливать отношения родитель-потомок, если класс сопрограммы
         * управляет своим состоянием изнутри onCancelled или onCancelling
         * (за исключением интеграций rx, у которых не может быть родительского элемента)
         */
        if (initParentJob) {
            //todo
        }
    }

    final override val context: CoroutineContext = parentContext + this

    final override fun resumeWith(result: Result<T>) {
        TODO("Not yet implemented")
    }

    override val coroutineContext: CoroutineContext
        get() = context

    /**
     * Запускает эту корутину с заданным блоком кода и стратегией запуска.
     * Эта функция должна вызываться не более одного раза в этой корутине.
     */
    fun <R> start(start: CoroutineStart, receiver: R, block: suspend R.() -> T) {
        start(block, receiver, this)
    }
}