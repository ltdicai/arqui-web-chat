package Models;

import java.util.Stack;

public abstract class Conversation {
    private Stack<Message> messages;

    public Conversation() {
        this.messages = new Stack<Message>();
    }

    public Stack<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message){
        this.messages.add(message);
    }
}
