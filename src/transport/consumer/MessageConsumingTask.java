package transport.consumer;

import transport.broker.MessageBroker;
import transport.model.Message;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class MessageConsumingTask implements Runnable {
    private static final int SECONDS_DURATION_TO_SLEEP_BEFORE_CONSUMING = 3;

    private final MessageBroker messageBroker;

    public MessageConsumingTask(final MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            //TimeUnit.SECONDS.sleep(SECONDS_DURATION_TO_SLEEP_BEFORE_CONSUMING);
            final Optional<Message> optionalMessage = messageBroker.consume();
            optionalMessage.orElseThrow(RuntimeException::new);
        }
    }
}
