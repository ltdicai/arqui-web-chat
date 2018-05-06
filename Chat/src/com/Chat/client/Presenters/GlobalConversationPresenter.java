package com.Chat.client.Presenters;

import com.Chat.client.Events.LoginEvent;
import com.Chat.client.Events.SendPrivateTextMessage;
import com.Chat.client.Models.AppModel;
import com.Chat.client.Models.Message;
import com.Chat.client.Models.TextMessage;
import com.Chat.client.Models.User;
import com.Chat.client.Views.GlobalConversationView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import javax.servlet.http.Cookie;
import java.util.List;

public class GlobalConversationPresenter {
    public interface Display{
        HasClickHandlers getLoginButton();
        Widget asWidget();
        GlobalConversationView getViewInstance();
        String getIdUser();
        String sendMessage();
        void updateMessages(List<Message> listMessage);
    }
    //event bus used to register events
    final HandlerManager eventBus;
    final GlobalConversationPresenter.Display view;
    AppModel context;

    public GlobalConversationPresenter(GlobalConversationPresenter.Display view, HandlerManager eventBus, AppModel context){
        this.eventBus = eventBus;
        this.view = view;
        this.context = context;

        getView().updateMessages(context.globalConversation.getMessages());
    }

    public void bindEvents(){
        view.getLoginButton().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new SendPrivateTextMessage(view.sendMessage()));
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

