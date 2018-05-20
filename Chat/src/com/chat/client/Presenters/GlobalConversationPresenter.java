package com.chat.client.Presenters;

import com.chat.client.Models.*;
import com.chat.client.Services.GlobalConversationDataService;
import com.chat.client.Services.GlobalConversationDataServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.List;

public class GlobalConversationPresenter implements ConversationPresenter.SpecificConversationPresenter {

    private ConversationPresenter conversationPresenter;
    private Timer timer;


    public GlobalConversationPresenter(ConversationPresenter.Display view, User user, GlobalConversation conversation) {
        conversationPresenter = new ConversationPresenter(view, user, conversation);
    }

    public void go(final HasWidgets container) {
        conversationPresenter.setSpecificConversation(this);
        conversationPresenter.go(container);
        timer = new Timer() {
            @Override
            public void run() {
                timer.cancel();
                updateMessage();
            }
        };
        timer.scheduleRepeating(500);
    }

    public void updateMessage() {
        AsyncCallback<GlobalConversation> callback = new AsyncCallback<GlobalConversation>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(GlobalConversation globalConversation) {
                List<Message> messageList = globalConversation.getMessages();
                conversationPresenter.updateMessageInView(messageList);
                timer = new Timer() {
                    @Override
                    public void run() {
                        timer.cancel();
                        updateMessage();
                    }
                };
                timer.scheduleRepeating(500);
            }
        };

        GlobalConversationDataServiceAsync globalConversationDataServiceAsync = GWT.create(GlobalConversationDataService.class);

        globalConversationDataServiceAsync.get(conversationPresenter.getMessageCount(), callback);

    }


    public void sendTextMessage(String messageText) {
        timer.cancel();
        User user = conversationPresenter.getUser();
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

}

