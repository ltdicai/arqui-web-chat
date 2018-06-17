package com.chat.client.Presenters;

import com.chat.client.Models.GroupConversation;
import com.chat.client.Models.User;
import com.chat.client.Services.GroupConversationService;
import com.chat.client.Services.GroupConversationServiceAsync;
import com.chat.client.Views.ConversationView;
import com.chat.client.Views.GroupAdministrationView;
import com.chat.client.Views.UserAdministrationForGroupsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class GroupAdministrationPresenter {

    public interface Display {
        void setPresenter(GroupAdministrationPresenter presenter);
        void showGroups(List<GroupConversation> groups);

        Widget asWidget();

        GroupAdministrationView getViewInstance();
        void unableAddUser();
        HasWidgets getAddUserContainer();
    }

    private Display view;
    private User loggerUser;
    private MenuPresenter menuPresenter;

    public GroupAdministrationPresenter(Display view, User user, MenuPresenter menuPresenter) {
        this.view = view;
        this.loggerUser = user;
        this.menuPresenter = menuPresenter;
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
        updateGroups();
    }

    public void updateGroups() {
        AsyncCallback<List<GroupConversation>> callback = new AsyncCallback<List<GroupConversation>>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(List<GroupConversation> groupConversations) {
                getView().showGroups(groupConversations);
            }
        };

        GroupConversationServiceAsync conversationServiceAsync = GWT.create(GroupConversationService.class);

        conversationServiceAsync.getGroupConversationsForUser(loggerUser,
                0,  callback);
    }

    public void goToGroupConversation(int conversationid){
        AsyncCallback<GroupConversation> callback = new AsyncCallback<GroupConversation>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(GroupConversation groupConversation) {
                menuPresenter.goToGroupConversation(groupConversation);
            }
        };

        GroupConversationServiceAsync conversationServiceAsync = GWT.create(GroupConversationService.class);

        conversationServiceAsync.getGroupConversation(conversationid,
                0,  callback);

    }


    public void NewGroup(String name){
        AsyncCallback<GroupConversation> callback = new AsyncCallback<GroupConversation>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(GroupConversation groupConversation) {
                updateGroups();
            }
        };

        GroupConversationServiceAsync conversationServiceAsync = GWT.create(GroupConversationService.class);

        conversationServiceAsync.createGroupConversation(loggerUser,
                name,  callback);
    }

    public void unableAddUser(){
        getView().unableAddUser();
    }

    public void enableUserAdministration(int conversationid){
        GroupAdministrationPresenter groupAdministrationPresenter = this;
        AsyncCallback<GroupConversation> callback = new AsyncCallback<GroupConversation>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(GroupConversation groupConversation) {

                UserAdministrationForGroupsPresenter group =
                        new UserAdministrationForGroupsPresenter(
                                new UserAdministrationForGroupsView(),
                                loggerUser , groupAdministrationPresenter,
                                groupConversation);
                group.go(view.getAddUserContainer());
            }
        };

        GroupConversationServiceAsync conversationServiceAsync = GWT.create(GroupConversationService.class);

        conversationServiceAsync.getGroupConversation(conversationid,
                0,  callback);


    }
}

