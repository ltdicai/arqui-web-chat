package com.Chat.client.Services;

import com.Chat.client.Models.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.sql.SQLException;

public interface UserDataServiceAsync {

    void insert(User user, AsyncCallback<Void> callback);

    void get(String userID, AsyncCallback<User> callback);
}
