package com.chat.client.Presenters;

import com.chat.client.Models.GroupConversation;
import com.chat.client.Models.User;
import com.chat.client.Services.GroupConversationService;
import com.chat.client.Services.GroupConversationServiceAsync;
import com.chat.client.Services.UserDataService;
import com.chat.client.Services.UserDataServiceAsync;
import com.chat.client.Views.UserAdministrationForGroupsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateGroupsPresenter {

    public interface Display {
        void setPresenter(CreateGroupsPresenter presenter);
        void showUsers(List<User> users);
        Widget asWidget();
        CreateGroupsView getViewInstance();
    }

    private Display view;
    private User loggerUser;
    private GroupAdministrationPresenter groupAdministrationPresenter;
    private GroupConversation groupConversation;
    private Map<String, User> selectedUsers;

    public CreateGroupsPresenter(Display view, User user, GroupAdministrationPresenter groupAdministrationPresenter) {
        this.view = view;
        this.loggerUser = user;
        this.groupAdministrationPresenter = groupAdministrationPresenter;
        selectedUsers = new HashMap<>();
    }

    public Display getView(){
        return view;
    }

    public void bind(){
        view.setPresenter(this);
    }

    public void go(final HasWidgets container){
        bind();
        container.clear();
        container.add(view.asWidget());
        updateUsers();
    }

    public void updateUsers() {
        AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(List<User> users) {
                getView().showUsers(users);
            }
        };

        UserDataServiceAsync userDataServiceAsync = GWT.create(UserDataService.class);
        userDataServiceAsync.getAllUsers(callback);

    }

    public User getLoggedUser(){
        return loggerUser;
    }

    public void createGroup(String groupName){
        List<User> users = new ArrayList<>(selectedUsers.values());
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                // TODO
            }

            @Override
            public void onSuccess(Void result) {
                Window.alert("Created group!");
            }
        };


        GroupConversationServiceAsync groupConversationServiceAsync = GWT.create(GroupConversationService.class);
        groupConversationServiceAsync.createGroupConversationWith(loggerUser, groupName, users, callback);
    }

    public void addUser(User user) {
        selectedUsers.put(user.getUserID(), user);
    }

    public void removeUser(User user) {
        selectedUsers.remove(user.getUserID());
    }

    public void goBackToGroupAdministration(){
        groupAdministrationPresenter.unableAddUser();
    }
}

