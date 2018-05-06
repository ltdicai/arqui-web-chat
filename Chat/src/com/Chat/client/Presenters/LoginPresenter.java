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

    public LoginPresenter(Display view, HandlerManager eventBus){
        this.eventBus = eventBus;
        this.view = view;
    }


    public void bindEvents(){
        view.getLoginButton().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                String idUser = view.getIdUser();

                Cookies.setCookie("UserID", idUser);

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