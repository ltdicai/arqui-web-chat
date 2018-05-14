package com.chat.client.Presenters;

import com.chat.client.Models.*;
import com.chat.client.Services.GlobalConversationDataService;
import com.chat.client.Services.GlobalConversationDataServiceAsync;
import com.chat.client.Views.GlobalConversationView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.Stack;

public class GlobalConversationPresenter {
    public interface Display{
        HasClickHandlers getSendTextMessageBotton();
        HasClickHandlers getSendImageMessageBotton();
        HasClickHandlers getSendAudioMessageBotton();
        Widget asWidget();
        GlobalConversationView getViewInstance();
        String sendTextMessage();
        void updateMessages(Stack<Message> listMessage);
        void clearText();
        void setVisibleFileUploadPanel(boolean visibility);
        FormPanel getFileUploadPanel();
    }

    private int messageCount;
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
        messageCount = 0;
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
        view.getSendImageMessageBotton().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                getView().setVisibleFileUploadPanel(true);
            }

        });
        view.getFileUploadPanel().addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                updateMessage();
            }
        });


        view.getSendAudioMessageBotton().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                String messageText = getView().sendTextMessage();
                getView().clearText();
                String userid = Cookies.getCookie("UserID");
                User user = new User(userid);
                AudioMessage newMessage = new AudioMessage(user, messageText);

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
                Stack<Message> messageList = globalConversation.getMessages();
                messageCount += messageList.size();
                getView().updateMessages(messageList);
            }
        };

        GlobalConversationDataServiceAsync globalConversationDataServiceAsync = GWT.create(GlobalConversationDataService.class);

        globalConversationDataServiceAsync.get(messageCount, callback);

    }

}

