package com.chat.client.Services;

import com.chat.client.Models.User;
import com.chat.client.errors.GeneralException;
import com.chat.client.errors.UserInvalidIDOrPassword;
import com.chat.client.errors.UserInvalidPassword;
import com.chat.client.errors.UserNotFoundException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

import java.util.List;

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

    void insert(User user, String password) throws GeneralException;

    User get(String userID) throws UserNotFoundException;

    List<User> getAllUsers();

    User login(String userID, String password) throws UserNotFoundException, UserInvalidPassword, UserInvalidIDOrPassword;
}
