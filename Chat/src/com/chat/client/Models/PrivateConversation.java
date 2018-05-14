package com.chat.client.Models;

public class PrivateConversation extends Conversation {
    private Integer id;
    private User userHost;
    private User userInvite;

    public PrivateConversation() {}

    public PrivateConversation(User userHost, User userInvite) {
        this.userHost = userHost;
        this.userInvite = userInvite;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserHost() {
        return userHost;
    }

    public User getUserInvite() {
        return userInvite;
    }

    public void addMessage(Message message){
        if(message.getUser().equals(userHost) || message.getUser().equals(userInvite)){
            super.addMessage(message);
        }
    }
}
