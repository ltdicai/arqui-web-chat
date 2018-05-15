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
import java.util.Stack;

public class GlobalConversationPresenter implements ConversationPresenter.SpecificConversation {

    private ConversationPresenter conversationPresenter;
    private Timer timer;
    private boolean isUpdating;

    public GlobalConversationPresenter(ConversationPresenter.Display view, User user) {
        conversationPresenter = new ConversationPresenter(view, user);
        this.timer = new Timer() {
            @Override
            public void run() {
                if (!isUpdating) {
                    isUpdating = true;
                    updateMessage();
                    isUpdating = false;
                }
            }
        };
        this.timer.scheduleRepeating(2000);
    }

    public void go(final HasWidgets container) {
        conversationPresenter.go(container);
        conversationPresenter.setSpecificConversation(this);
    }

    public void updateMessage() {
        AsyncCallback<GlobalConversation> callback = new AsyncCallback<GlobalConversation>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(GlobalConversation globalConversation) {
                List<Message> messageList = globalConversation.getMessages();
                conversationPresenter.updateMessageInView(messageList);
            }
        };

        GlobalConversationDataServiceAsync globalConversationDataServiceAsync = GWT.create(GlobalConversationDataService.class);

        globalConversationDataServiceAsync.get(conversationPresenter.getMessageCount(), callback);

    }


    public void sendTextMessage(String messageText) {
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

}

