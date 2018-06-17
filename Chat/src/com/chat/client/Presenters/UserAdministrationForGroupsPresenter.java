package com.chat.client.Presenters;

import com.chat.client.Models.GroupConversation;
import com.chat.client.Models.User;
import com.chat.client.Services.GroupConversationService;
import com.chat.client.Services.GroupConversationServiceAsync;
import com.chat.client.Views.UserAdministrationForGroupsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class UserAdministrationForGroupsPresenter {

    public interface Display {
        void setPresenter(UserAdministrationForGroupsPresenter presenter);
        void showUsers(List<User> groups);

        Widget asWidget();

        UserAdministrationForGroupsView getViewInstance();
    }

    private Display view;
    private User loggerUser;
    private GroupAdministrationPresenter groupAdministrationPresenter;
    private GroupConversation groupConversation;

    public UserAdministrationForGroupsPresenter(Display view, User user, GroupAdministrationPresenter groupAdministrationPresenter, GroupConversation groupConversation) {
        this.view = view;
        this.loggerUser = user;
        this.groupAdministrationPresenter = groupAdministrationPresenter;
        this.groupConversation = groupConversation;
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

        GroupConversationServiceAsync conversationServiceAsync = GWT.create(GroupConversationService.class);

        conversationServiceAsync.getUserPosibleAdd(groupConversation,  callback);
    }

    public User getLoggedUser(){
        return loggerUser;
    }

    public void addUser(User user){
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(Void v) {
                goBackToGroupAdministration();
            }
        };

        GroupConversationServiceAsync conversationServiceAsync = GWT.create(GroupConversationService.class);

        conversationServiceAsync.addUser(groupConversation, user, callback);
    }

    public void goBackToGroupAdministration(){
        groupAdministrationPresenter.unableAddUser();
    }
}

