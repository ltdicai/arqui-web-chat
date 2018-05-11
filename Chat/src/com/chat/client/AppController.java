package com.chat.client;

import com.chat.client.Events.*;
import com.chat.client.Models.Message;
import com.chat.client.Models.TextMessage;
import com.chat.client.Models.User;
import com.chat.client.Presenters.LoginPresenter;
import com.chat.client.Presenters.MenuPresenter;
import com.chat.client.Views.LoginView;
import com.chat.client.Views.MenuView;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;

public class AppController {
    HandlerManager eventBus;;
    HasWidgets container;

    public AppController(HandlerManager manager){
        this.eventBus = manager;
        bindEvents();

        if(Cookies.getCookieNames().contains("UserID")){
            goMenuPage();
        }
        else{
            goLoginPage();
        }
    }

    private void goMenuPage(){
        MenuPresenter mainpage = new MenuPresenter(new MenuView(), eventBus);
        container = mainpage.getView().getViewInstance();
        mainpage.go(RootPanel.get());
    }

    private void goLoginPage(){
        LoginPresenter mainpage = new LoginPresenter(new LoginView(), eventBus);
        container = mainpage.getView().getViewInstance();
        mainpage.go(RootPanel.get());
    }

    public void bindEvents(){
        eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler(){
            @Override
            public void onLogin(LoginEvent event) {
                goMenuPage();
            }
        });

        eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler(){
            @Override
            public void onLogout(LogoutEvent event) {
                goLoginPage();
            }
        });

    }

}