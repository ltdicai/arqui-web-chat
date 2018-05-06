package com.Chat.client;

import com.Chat.client.Events.*;
import com.Chat.client.Models.AppModel;
import com.Chat.client.Models.Message;
import com.Chat.client.Models.TextMessage;
import com.Chat.client.Models.User;
import com.Chat.client.Presenters.LoginPresenter;
import com.Chat.client.Presenters.MenuPresenter;
import com.Chat.client.Views.LoginView;
import com.Chat.client.Views.MenuView;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;

public class AppController {
    HandlerManager eventBus;
    LoginPresenter loginPage;
    HasWidgets container;
    AppModel context;

    public AppController(HandlerManager manager, AppModel context){
        this.eventBus = manager;
        this.context = context;
        loginPage = new LoginPresenter(new LoginView(), eventBus, context);
        bindEvents();
    }
    public void bindEvents(){
        eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler(){
            @Override
            public void onLogin(LoginEvent event) {
                // TODO Auto-generated method stub
                //if login successful
                MenuPresenter mainpage = new MenuPresenter(new MenuView(), eventBus, context);
                container = mainpage.getView().getViewInstance();
                mainpage.go(RootPanel.get());
            }
        });

        eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler(){
            @Override
            public void onLogout(LogoutEvent event) {
                Cookies.removeCookie("UserID");
                loginPage.go(RootPanel.get());
            }
        });

        eventBus.addHandler(SendPrivateTextMessage.TYPE, new SendPrivateTextMessageHandler(){
            @Override
            public void onSendPrivateTextMessage(SendPrivateTextMessage event) {
                User user = new User(Cookies.getCookie("userID"));
                Message message = new TextMessage(user, event.message);
                context.globalConversation.addMessage(message);
            }
        });
    }
    public void goTo(HasWidgets page){
        this.container = page;
        loginPage.go(page);
    }

}