import coroutines.CoroutineScope
import coroutines.GlobalScope
import coroutines.Job
import coroutines.launch

val scope = CoroutineScope(Job())

fun main() {
    val job = GlobalScope.launch {
        println("test")
    }
    Thread.sleep(2000)
//    GlobalScope.launch {
//        println("test")
//    }
//    GlobalScope.launch {
//        println("test")
//    }
}



