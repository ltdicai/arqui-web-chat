package com.chat.client.Models;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Objects;

public class User implements IsSerializable {
    private String userID;

    public String getUserID() {
        return userID;
    }

    public User() {
    }

    public User(String userID) {
        this.userID = userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userID, user.userID);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userID);
    }
}
