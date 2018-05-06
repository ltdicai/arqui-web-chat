package com.Chat.client.Presenters;

import com.Chat.client.Events.LogoutEvent;
import com.Chat.client.Models.AppModel;
import com.Chat.client.Views.GlobalConversationView;
import com.Chat.client.Views.MenuView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.*;
import java.util.HashSet;
import java.util.Set;


public class MenuPresenter {

    public interface Display{
        Button getLogout();
        Button getButtonGlobalConversation();
        Button getButtonPrivateConversation();
        Button getButtonGroupsConversation();
        Widget asWidget();
        MenuView getViewInstance();
        void setLabel(String texto);
        HasWidgets getSubContainer();
    }

    final Display display;
    final HandlerManager eventBus;
    AppModel context;

    public MenuPresenter(Display display, HandlerManager eventBus, AppModel context){
        this.display = display;
        this.eventBus = eventBus;
        this.context = context;
        this.display.setLabel(Cookies.getCookie("UserID"));
    }

    public void init(){
        display.getLogout().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                // use the event bus to trigger the event
                eventBus.fireEvent(new LogoutEvent());
            }
        });
        display.getButtonGlobalConversation().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                // use the event bus to trigger the event
                //eventBus.fireEvent(new LogoutEvent());
                GlobalConversationPresenter mainpage = new GlobalConversationPresenter(new GlobalConversationView(), eventBus, context);

                mainpage.go(getView().getSubContainer());
            }
        });
        display.getButtonGroupsConversation().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                // use the event bus to trigger the event
                //eventBus.fireEvent(new LogoutEvent());
            }
        });
        display.getButtonPrivateConversation().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                // use the event bus to trigger the event
                //eventBus.fireEvent(new LogoutEvent());
            }
        });
    }

    public void go(final HasWidgets container){
        init();
        container.clear();
        container.add(display.asWidget());

    }


    public Display getView(){

        return display;
    }
}
