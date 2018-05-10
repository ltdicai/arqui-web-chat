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
        HasWidgets getSubContainer();
    }

    final Display display;
    final HandlerManager eventBus;
    private GlobalConversationPresenter globalConversationPresenter;

    public MenuPresenter(Display display, HandlerManager eventBus){
        this.display = display;
        this.eventBus = eventBus;
        this.display.setLabel(Cookies.getCookie("UserID"));
        this.globalConversationPresenter = new GlobalConversationPresenter(new GlobalConversationView(), eventBus);

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

                globalConversationPresenter.go(getView().getSubContainer());
                globalConversationPresenter.updateMessage();
                //eventBus.fireEvent(new NewGlobalConversationEntryEvent(mainpage));
            }
        });
        display.getButtonGroups().addClickHandler(new ClickHandler(){
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
