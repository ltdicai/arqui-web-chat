package com.chat.client.Models;

import java.util.*;

public class Group{
    private String name;
    private User userHost;
    private Set<User> members;
    private GroupConversation conversation;

    public String getName() {
        return name;
    }

    public User getUserHost() {
        return userHost;
    }

    public Set<User> getMember() {
        return members;
    }

    public Group(String name, User userHost) {
        this.name = name;
        this.userHost = userHost;
        this.members = new HashSet<>();
        this.conversation = new GroupConversation();
    }

    public void addUser(User member){
        members.add(member);
    }

    public void addMessage(Message message){
        if(message.getUser().equals(userHost) || members.contains(message.getUser())) {
            conversation.addMessage(message);
        }
    }
}
