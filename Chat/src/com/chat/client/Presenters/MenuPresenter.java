package com.chat.client.Presenters;

import com.chat.client.Events.LogoutEvent;
import com.chat.client.Models.User;
import com.chat.client.Services.ConversationService;
import com.chat.client.Services.ConversationServiceAsync;
import com.chat.client.Services.UserDataService;
import com.chat.client.Services.UserDataServiceAsync;
import com.chat.client.Views.GlobalConversationView;
import com.chat.client.Views.MenuView;
import com.chat.client.Views.PrivateConversationView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.List;


public class MenuPresenter {

    public interface Display{
        Button getLogout();
        Button getButtonGlobalConversation();
        Button getButtonPrivateConversation();
        Button testGetButtonPrivateConversation();
        Button getButtonGroups();
        Widget asWidget();
        MenuView getViewInstance();
        void setLabel(String texto);
        void showUsers(List<User> users);
        HasWidgets getSubContainerChat();
    }

    final Display view;
    final HandlerManager eventBus;
    private final ConversationServiceAsync conversationService = GWT.create(ConversationService.class);
    private final UserDataServiceAsync userService = GWT.create(UserDataService.class);
    private User loggedUser;

    public MenuPresenter(Display display, HandlerManager eventBus){
        this.view = display;
        this.eventBus = eventBus;

        userService.get(Cookies.getCookie("UserID"), new AsyncCallback<User>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("User " + Cookies.getCookie("UserID") + " doesn't exist");
                MenuPresenter.this.eventBus.fireEvent(new LogoutEvent());
            }

            @Override
            public void onSuccess(User result) {
                MenuPresenter.this.loggedUser = result;
                MenuPresenter.this.view.setLabel(Cookies.getCookie("UserID"));
            }
        });

        userService.getAllUsers(new AsyncCallback<List<User>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(List<User> result) {
                MenuPresenter.this.view.showUsers(result);
            }
        });

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
                PrivateConversationPresenter presenter = new PrivateConversationPresenter(
                        new PrivateConversationView(),
                        eventBus,
                        1);
                presenter.go(getView().getSubContainerChat());

            }
        });
        view.testGetButtonPrivateConversation().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //

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
