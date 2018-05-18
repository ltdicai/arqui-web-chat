package com.chat.client.Presenters;

import com.chat.client.Models.User;
import com.chat.client.Services.UserDataService;
import com.chat.client.Services.UserDataServiceAsync;
import com.chat.client.Views.MenuView;
import com.chat.client.errors.UserInvalidIDOrPassword;
import com.chat.client.errors.UserInvalidPassword;
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
        void setError(String error);
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

    public void Login(String idUser, String password){
        User user = new User(idUser);

        UserDataServiceAsync userDataServiceAsync = GWT.create(UserDataService.class);

        AsyncCallback<User> callback = new AsyncCallback<User>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.

                if (caught.getClass().equals(UserInvalidIDOrPassword.class)){
                    getView().setError("El usuario y la contraseña deben tener, al menos, 6 caracteres.");
                }
                if (caught.getClass().equals(UserInvalidPassword.class)){
                    getView().setError("La contraseña es incorrecta.");
                }
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
                    userDataServiceAsync.insert(user, password, callback);

                }
            }

            public void onSuccess(User user) {
                Cookies.setCookie("UserID", idUser);
                goMenuPage();
            }
        };

        userDataServiceAsync.login(idUser, password, callback);

    }

    private void goMenuPage(){
        MenuPresenter mainpage = new MenuPresenter(new MenuView());
        mainpage.go(RootPanel.get());
    }
}