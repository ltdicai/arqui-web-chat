package com.Chat.client.Services;

import com.Chat.client.Models.User;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

import java.sql.SQLException;

@RemoteServiceRelativePath("UserDataService")
public interface UserDataService extends RemoteService {
    /**
     * Utility/Convenience class.
     * Use UserDataService.App.getInstance() to access static instance of UserDataServiceAsync
     */
    public static class App {
        private static final UserDataServiceAsync ourInstance = (UserDataServiceAsync) GWT.create(UserDataService.class);

        public static UserDataServiceAsync getInstance() {
            return ourInstance;
        }
    }

    void insert(User user);

    User get(String userID);
}
