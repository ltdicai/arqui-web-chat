package com.chat.client.Presenters;

import com.chat.client.Models.*;
import com.chat.client.Services.*;
import com.chat.client.Views.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.List;


public class MenuPresenter {

    public interface Display{
        Widget asWidget();
        MenuView getViewInstance();
        void setLabel(String texto);
        void showUsers(List<User> users);
        void doLogout();
        HasWidgets getSubContainerChat();
        void setPresenter(MenuPresenter presenter);
    }

    final Display view;
    private final ConversationServiceAsync conversationService = GWT.create(ConversationService.class);
    private final UserDataServiceAsync userService = GWT.create(UserDataService.class);
    private User loggedUser;

    public MenuPresenter(Display display){
        this.view = display;

        userService.get(Cookies.getCookie("UserID"), new AsyncCallback<User>() {
            @Override
            public void onFailure(Throwable caught) {
                //Window.alert("User " + Cookies.getCookie("UserID") + " doesn't exist");
                //goLoginPage();
            }

            @Override
            public void onSuccess(User result) {
                MenuPresenter.this.loggedUser = result;
                MenuPresenter.this.view.setLabel(Cookies.getCookie("UserID"));

            }
        });

    }

    public void bind(){
        view.setPresenter(this);

    }

    public void go(final HasWidgets container){
        bind();
        container.clear();
        container.add(view.getViewInstance().asWidget());
    }

    public Display getView(){
        return view;
    }

    public void Logout(){
        Cookies.removeCookie("UserID");
        getView().doLogout();
        goLoginPage();
    }


    private void goLoginPage(){
        LoginPresenter mainpage = new LoginPresenter(new LoginView());
        mainpage.go(RootPanel.get());
    }

    public void goToGlobalConversation(){

        ConversationView conversationView = new ConversationView();

        AsyncCallback<GlobalConversation> callback = new AsyncCallback<GlobalConversation>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(GlobalConversation globalConversation) {
                GlobalConversationPresenter globalConversationPresenter =
                        new GlobalConversationPresenter(conversationView, loggedUser, globalConversation);
                globalConversationPresenter.go(getView().getSubContainerChat());
            }
        };

        GlobalConversationDataServiceAsync globalConversationDataServiceAsync = GWT.create(GlobalConversationDataService.class);

        globalConversationDataServiceAsync.get(0, callback);

    }

    public void goToPrivateConversation(User user){
        AsyncCallback<PrivateConversation> callback = new AsyncCallback<PrivateConversation>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(PrivateConversation privateConversation) {

                PrivateConversationPresenter priv =
                        new PrivateConversationPresenter(new ConversationView(), loggedUser, user, privateConversation);
                priv.go(getView().getSubContainerChat());
            }
        };

        ConversationServiceAsync conversationServiceAsync = GWT.create(ConversationService.class);

        conversationServiceAsync.getPrivateConversationBetween(loggedUser, user,
                0,  callback);

    }

    public void goToGroupAdmistration(){
        GroupAdministrationPresenter groupConversationPresenter = new GroupAdministrationPresenter(
                new GroupAdministrationView(), loggedUser, this);
        groupConversationPresenter.go(getView().getSubContainerChat());
    }

    public void goToGroupConversation(GroupConversation groupConversation) {

        GroupConversationPresenter groupConversationPresenter = new GroupConversationPresenter(
                new ConversationView(), loggedUser, groupConversation);

        groupConversationPresenter.go(getView().getSubContainerChat());
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void updateUserList() {
        userService.getAllUsers(new AsyncCallback<List<User>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(List<User> result) {
                getView().showUsers(result);
            }
        });

    }
}
