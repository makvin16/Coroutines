import java.sql.JDBCType;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class Main2 {

    public static void main(String[] args) throws InterruptedException {
        Blocker blocker = new Blocker();
        Thread thread1 = new Thread(() -> {
            blocker.method1();
        });
        Thread thread2 = new Thread(() -> {
            blocker.method2();
        });
        thread2.start();
        thread1.start();
    }

    private static class Blocker {
        private final Lock lock1 = new ReentrantLock();
        private final Lock lock2 = new ReentrantLock();

        public void method() {
            lock1.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " in");
                while (true) {
                }
            } finally {
                lock1.unlock();
            }
        }

        public void method1() {
            lock1.lock();
            try {
                System.out.println("method1 " + Thread.currentThread().getName());
            } finally {
                lock1.unlock();
            }
        }

        public synchronized void method2() {
            lock2.lock();
            System.out.println("method2 " + Thread.currentThread().getName());
            try {
                while (true) {
                }
            } finally {
                lock2.unlock();
            }
        }
     }
}
