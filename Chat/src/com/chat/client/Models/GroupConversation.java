package com.chat.client.Models;

import java.util.HashSet;
import java.util.Set;

public class GroupConversation extends Conversation{

    private String name;
    private Set<User> members;

    public String getName() {
        return name;
    }

    public Set<User> getMember() {
        return members;
    }

    public GroupConversation(){}

    public GroupConversation(String name) {
        this.name = name;
        this.members = new HashSet<>();
    }

    public void addUser(User member){
        members.add(member);
    }

    public void addMessage(Message message){
        if(members.contains(message.getUser())) {
            super.addMessage(message);
        }
    }
}
