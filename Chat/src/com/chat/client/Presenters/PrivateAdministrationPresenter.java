package com.chat.client.Presenters;

import com.chat.client.Models.User;
import com.chat.client.Services.UserDataService;
import com.chat.client.Services.UserDataServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class PrivateAdministrationPresenter {

    public interface Display {
        void setPresenter(PrivateAdministrationPresenter presenter);
        void showUsers(List<User> users);

        Widget asWidget();

        Widget getViewInstance();
    }

    private PrivateAdministrationPresenter.Display view;
    private User loggerUser;
    private MenuPresenter menuPresenter;

    public PrivateAdministrationPresenter(PrivateAdministrationPresenter.Display view, User user, MenuPresenter menuPresenter) {
        this.view = view;
        this.loggerUser = user;
        this.menuPresenter = menuPresenter;
    }

    public PrivateAdministrationPresenter.Display getView(){
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

        UserDataServiceAsync userDataServiceAsync = GWT.create(UserDataService.class);

        userDataServiceAsync.getAllUsers(new AsyncCallback<List<User>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(List<User> result) {
                getView().showUsers(result);
            }
        });

    }

    public User getLoggedUser(){
        return loggerUser;
    }

    public void goToPrivateConversation(User user){
        menuPresenter.goToPrivateConversation(user);
    }
}

