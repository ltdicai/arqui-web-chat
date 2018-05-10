package com.chat.client.Services;

import com.chat.client.Models.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserDataServiceAsync {

    void insert(User user, AsyncCallback<Void> callback);

    void get(String userID, AsyncCallback<User> callback);
}
