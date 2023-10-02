package transport.broker;

import transport.model.Message;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public final class MessageBroker {
    private static final String MESSAGE_OF_MESSAGE_IS_PRODUCED = "Message '%s' is produced. Thread - %s\n";
    private static final String MESSAGE_OF_MESSAGE_IS_CONSUMED = "Message '%s' is consumed. Thread - %s\n";

    private final Queue<Message> messagesToBeConsumed;
    private final int maxStoredMessages;

    public MessageBroker(final int maxStoredMessages) {
        this.maxStoredMessages = maxStoredMessages;
        messagesToBeConsumed = new ArrayDeque<>(maxStoredMessages);
    }

    public synchronized void produce(final Message message) {
        try {
            System.out.println("try produce " + Thread.currentThread().getName());
            while (messagesToBeConsumed.size() >= maxStoredMessages) {
                System.out.println("wait " + Thread.currentThread().getName());
                wait();
            }
            messagesToBeConsumed.add(message);
            System.out.printf(MESSAGE_OF_MESSAGE_IS_PRODUCED, message, Thread.currentThread().getName());
            notify();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized Optional<Message> consume() {
        try {
            while (messagesToBeConsumed.isEmpty()) {
                wait();
            }
            final Message message = messagesToBeConsumed.poll();
            System.out.printf(MESSAGE_OF_MESSAGE_IS_CONSUMED, message, Thread.currentThread().getName());
            notifyAll();
            return Optional.of(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }
}
