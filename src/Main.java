import transport.broker.MessageBroker;
import transport.consumer.MessageConsumingTask;
import transport.producer.MessageFactory;
import transport.producer.MessageProducingTask;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        final int max = 1;
        final MessageBroker messageBroker = new MessageBroker(max);

        final MessageFactory messageFactory = new MessageFactory();

        final Thread firstProducingThread = new Thread(new MessageProducingTask(messageBroker, messageFactory), "Producer1");
        final Thread secondProducingThread = new Thread(new MessageProducingTask(messageBroker, messageFactory), "Producer2");
        final Thread thirdProducingThread = new Thread(new MessageProducingTask(messageBroker, messageFactory));

        final Thread firstConsumingThread = new Thread(new MessageConsumingTask(messageBroker), "Consumer1");
        final Thread secondConsumingThread = new Thread(new MessageConsumingTask(messageBroker));
        final Thread thirdConsumingThread = new Thread(new MessageConsumingTask(messageBroker));

//        startThread(firstProducingThread, secondProducingThread, thirdProducingThread, firstConsumingThread, secondConsumingThread, thirdConsumingThread);
        startThread(firstProducingThread, secondProducingThread, firstConsumingThread);

        Thread thread = new Thread(() -> {
           try {
               while (true) {
                   Thread.sleep(500, 1);
                   printThreadsState(firstProducingThread, secondProducingThread, firstConsumingThread);
               }
           } catch (InterruptedException e) {

           }
        });
        thread.start();
    }

    private static void startThread(Thread... threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }

    private static void printThreadsState(Thread... threads) {
        Arrays.stream(threads).forEach(t -> {
            System.out.println(t.getName() + " " + t.getState());
        });
    }
}