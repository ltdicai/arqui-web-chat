package com.chat.client.Services;

import com.chat.client.Models.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface UserDataServiceAsync {

    void insert(User user, String password, AsyncCallback<Void> callback);

    void get(String userID, AsyncCallback<User> callback);

    void getAllUsers(AsyncCallback<List<User>> async);

    void login(String userID, String password, AsyncCallback<User> callback);
}
