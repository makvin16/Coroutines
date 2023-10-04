import coroutines.*

internal val job1: CompletableJob = JobImpl(null)
internal val job2: CompletableJob = JobImpl(null)
val scope = CoroutineScope(job1)

fun main() {
    scope.launch {  }
    println(job1)
    println(job2)
    println(scope.coroutineContext)
//    val job = GlobalScope.launch {
//        println("test")
//    }
//    Thread.sleep(2000)
//    GlobalScope.launch {
//        println("test")
//    }
//    GlobalScope.launch {
//        println("test")
//    }
}



