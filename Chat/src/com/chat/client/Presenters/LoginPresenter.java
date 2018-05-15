package com.chat.client.Presenters;

import com.chat.client.Models.User;
import com.chat.client.Services.UserDataService;
import com.chat.client.Services.UserDataServiceAsync;
import com.chat.client.Views.MenuView;
import com.chat.client.errors.UserNotFoundException;
import com.chat.client.Views.LoginView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;


public class LoginPresenter {
    public interface Display{
        Widget asWidget();
        LoginView getViewInstance();
        void setPresenter(LoginPresenter presenter);
    }

    final Display view;

    public LoginPresenter(Display view){
        this.view = view;
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

    public void Login(String idUser){
        User user = new User(idUser);

        UserDataServiceAsync userDataServiceAsync = GWT.create(UserDataService.class);

        AsyncCallback<User> callback = new AsyncCallback<User>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.

                if (caught.getClass().equals(UserNotFoundException.class)){
                    AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                        }

                        @Override
                        public void onSuccess(Void result) {
                            Cookies.setCookie("UserID", idUser);
                            goMenuPage();
                        }
                    };
                    userDataServiceAsync.insert(user, callback);

                }
            }

            public void onSuccess(User user) {
                Cookies.setCookie("UserID", idUser);
                goMenuPage();
            }
        };

        userDataServiceAsync.get(idUser, callback);

    }

    private void goMenuPage(){
        MenuPresenter mainpage = new MenuPresenter(new MenuView());
        mainpage.go(RootPanel.get());
    }
}