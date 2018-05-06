package com.Chat.client.Presenters;

import com.Chat.client.Events.LoginEvent;
import com.Chat.client.Models.AppModel;
import com.Chat.client.Models.User;
import com.Chat.client.Views.LoginView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;


public class LoginPresenter {
    public interface Display{
        HasClickHandlers getLoginButton();
        Widget asWidget();
        LoginView getViewInstance();
        String getIdUser();
    }
    //event bus used to register events
    final HandlerManager eventBus;
    final Display view;
    AppModel context;

    public LoginPresenter(Display view, HandlerManager eventBus, AppModel context){
        this.eventBus = eventBus;
        this.view = view;
        this.context = context;
    }


    public void bindEvents(){
        view.getLoginButton().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                User user = new User(view.getIdUser());
                if(!context.users.contains(user)){
                    context.users.add(user);
                }
                Cookies.setCookie("UserID", user.getUserID());

                eventBus.fireEvent(new LoginEvent());
            }
        });
    }


    public void go(final HasWidgets container){
        bindEvents();
        container.clear();
        container.add(view.getViewInstance().asWidget());
    }

    public Display getView(){
        return view;
    }

}