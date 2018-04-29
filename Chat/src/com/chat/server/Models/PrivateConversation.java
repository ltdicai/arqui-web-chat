package com.chat.server.Models;

public class PrivateConversation extends Conversation {
    private User userHost;
    private User userInvite;

    public PrivateConversation(User userHost, User userInvite) {
        this.userHost = userHost;
        this.userInvite = userInvite;
    }

    public User getUserHost() {
        return userHost;
    }

    public User getUserInvite() {
        return userInvite;
    }
}
