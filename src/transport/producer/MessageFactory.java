package transport.producer;

import transport.model.Message;

public final class MessageFactory {
    private static final int INITIAL_NEXT_MESSAGE = 1;
    private static final String TEMPLATE_CREATED_MESSAGE_DATA = "Message#%d";

    private int nextMessageIndex;

    public MessageFactory() {
        nextMessageIndex = INITIAL_NEXT_MESSAGE;
    }

    public Message create() {
        return new Message(String.format(TEMPLATE_CREATED_MESSAGE_DATA, findAndIncrementNextMessageIndex()));
    }

    private synchronized int findAndIncrementNextMessageIndex() {
        return nextMessageIndex++;
    }
}
