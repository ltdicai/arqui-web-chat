package com.chat.client.Presenters;

import com.chat.client.Events.LogoutEvent;
import com.chat.client.Views.GlobalConversationView;
import com.chat.client.Views.MenuView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.*;


public class MenuPresenter {

    public interface Display{
        Button getLogout();
        Button getButtonGlobalConversation();
        Button getButtonPrivateConversation();
        Button getButtonGroups();
        Widget asWidget();
        MenuView getViewInstance();
        void setLabel(String texto);
        HasWidgets getSubContainerChat();
    }

    final Display view;
    final HandlerManager eventBus;

    public MenuPresenter(Display display, HandlerManager eventBus){
        this.view = display;
        this.eventBus = eventBus;
        this.view.setLabel(Cookies.getCookie("UserID"));
    }

    public void bindEvents(){
        view.getLogout().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                Cookies.removeCookie("UserID");
                eventBus.fireEvent(new LogoutEvent());
            }
        });
        view.getButtonGlobalConversation().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                GlobalConversationPresenter globalConversationPresenter = new GlobalConversationPresenter(new GlobalConversationView(), eventBus);
                globalConversationPresenter.go(getView().getSubContainerChat());
            }
        });
        view.getButtonGroups().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {

            }
        });
        view.getButtonPrivateConversation().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {

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
