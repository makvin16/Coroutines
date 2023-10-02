package transport.producer;

import transport.broker.MessageBroker;
import transport.model.Message;

import java.util.concurrent.TimeUnit;

public final class MessageProducingTask implements Runnable {
    private static final int SECONDS_DURATION_TO_SLEEP_BEFORE_PRODUCING = 1;

    private final MessageBroker messageBroker;
    private final MessageFactory messageFactory;

    public MessageProducingTask(final MessageBroker messageBroker, final MessageFactory messageFactory) {
        this.messageBroker = messageBroker;
        this.messageFactory = messageFactory;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            //TimeUnit.SECONDS.sleep(SECONDS_DURATION_TO_SLEEP_BEFORE_PRODUCING);
            final Message message = messageFactory.create();
            messageBroker.produce(message);
        }
    }
}
