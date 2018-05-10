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
    HandlerManager eventBus;
    LoginPresenter loginPage;
    HasWidgets container;

    public AppController(HandlerManager manager){
        this.eventBus = manager;
        if(Cookies.getCookieNames().contains("UserID")){
            goMenuPage();
        }
        else{
            loginPage = new LoginPresenter(new LoginView(), eventBus);
            goTo(RootPanel.get());
        }

        bindEvents();
    }

    private void goMenuPage(){
        MenuPresenter mainpage = new MenuPresenter(new MenuView(), eventBus);
        container = mainpage.getView().getViewInstance();
        mainpage.go(RootPanel.get());
    }

    public void bindEvents(){
        eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler(){
            @Override
            public void onLogin(LoginEvent event) {
                // TODO Auto-generated method stub
                //if login successful
                goMenuPage();
            }
        });

        eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler(){
            @Override
            public void onLogout(LogoutEvent event) {
                System.out.print("Llego al logout");
                Cookies.removeCookie("UserID");
                goTo(RootPanel.get());
            }
        });

        eventBus.addHandler(NewGlobalConversationEntryEvent.TYPE, new NewGlobalConversationEntryEventHandler(){
            @Override
            public void onNewGlobalConversationEntry(NewGlobalConversationEntryEvent event) {

            }
        });

        eventBus.addHandler(SendGlobalTextMessageEvent.TYPE, new SendGlobalTextMessageEventHandler(){
            @Override
            public void onSendGlobalTextMessage(SendGlobalTextMessageEvent event) {
                User user = new User(Cookies.getCookie("userID"));
                Message message = new TextMessage(user, event.getMessageText());
            }
        });
    }
    public void goTo(HasWidgets page){
        this.container = page;
        loginPage.go(page);
    }

}