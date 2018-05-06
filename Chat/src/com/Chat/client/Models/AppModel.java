package com.Chat.client.Models;

import java.util.HashSet;
import java.util.Set;

public class AppModel {
    public Set<User> users;
    public GlobalConversation globalConversation;
    public Set<PrivateConversation> privateConversatios;
    public Set<Group> groups;

    public AppModel() {
        this.users = new HashSet<User>();
        this.globalConversation = new GlobalConversation();
        this.privateConversatios = new HashSet<PrivateConversation>();
        this.groups = new HashSet<Group>();
    }
}
