package com.chat.client.Presenters;

import com.chat.client.Models.GlobalConversation;
import com.chat.client.Models.Message;
import com.chat.client.Models.TextMessage;
import com.chat.client.Models.User;
import com.chat.client.Services.GlobalConversationDataService;
import com.chat.client.Services.GlobalConversationDataServiceAsync;
import com.chat.client.Views.GlobalConversationView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;
import java.util.Stack;

public class GlobalConversationPresenter {
    public interface Display{
        HasClickHandlers getSendTextMessageBotton();
        Widget asWidget();
        GlobalConversationView getViewInstance();
        String sendTextMessage();
        void updateMessages(Stack<Message> listMessage);
        void clearText();
    }

    private Timer timer;
    final HandlerManager eventBus;
    final GlobalConversationPresenter.Display view;

    public GlobalConversationPresenter(GlobalConversationPresenter.Display view, HandlerManager eventBus){
        this.eventBus = eventBus;
        this.view = view;
        this.timer = new Timer() {
            @Override
            public void run() {
                updateMessage();
            }
        };
        this.timer.scheduleRepeating(2000);
    }

    public void bindEvents(){
        view.getSendTextMessageBotton().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                String messageText = getView().sendTextMessage();
                getView().clearText();
                String userid = Cookies.getCookie("UserID");
                User user = new User(userid);
                TextMessage newMessage = new TextMessage(user, messageText);

                AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                    public void onFailure(Throwable caught) {
                        // TODO: Do something with errors.
                    }

                    public void onSuccess(Void v) {
                        updateMessage();
                    }
                };

                GlobalConversationDataServiceAsync globalConversationDataServiceAsync = GWT.create(GlobalConversationDataService.class);

                globalConversationDataServiceAsync.addMessage(newMessage, callback);
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


    public void updateMessage(){
        AsyncCallback<GlobalConversation> callback = new AsyncCallback<GlobalConversation>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(GlobalConversation globalConversation) {
                getView().updateMessages(globalConversation.getMessages());
            }
        };

        GlobalConversationDataServiceAsync globalConversationDataServiceAsync = GWT.create(GlobalConversationDataService.class);

        globalConversationDataServiceAsync.get(callback);

    }

}

