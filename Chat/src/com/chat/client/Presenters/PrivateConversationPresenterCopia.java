package com.chat.client.Presenters;

import com.chat.client.Models.*;
import com.chat.client.Services.ConversationService;
import com.chat.client.Services.ConversationServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.List;

public class PrivateConversationPresenterCopia implements ConversationPresenter.SpecificConversationPresenter {

    private ConversationPresenter conversationPresenter;
    private Timer timer;
    private PrivateConversation conversation;
    private User loggedUser;
    private User inviteUser;
    private ConversationServiceAsync rpcService = GWT.create(ConversationService.class);

    public PrivateConversationPresenterCopia(ConversationPresenter.Display view, User hostuser, User inviteuser, PrivateConversation privateConversation) {
        this.conversation = privateConversation;
        this.conversationPresenter = new ConversationPresenter(view, hostuser, privateConversation);
        this.loggedUser = hostuser;
        this.inviteUser = inviteuser;

    }

    public void go(final HasWidgets container) {
        conversationPresenter.setSpecificConversation(this);
        conversationPresenter.go(container);
        this.timer = new Timer() {
            @Override
            public void run() {
                cancel();
                updateMessage();
            }
        };
        this.timer.scheduleRepeating(500);
    }

    public void updateMessage() {

        AsyncCallback<PrivateConversation> callback = new AsyncCallback<PrivateConversation>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(PrivateConversation privateConversation) {
                List<Message> messageList = privateConversation.getMessages();
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

        ConversationServiceAsync conversationServiceAsync = GWT.create(ConversationService.class);

        conversationServiceAsync.getPrivateConversation(conversation.getId(), conversationPresenter.getMessageCount(),  callback);
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

        ConversationServiceAsync conversationServiceAsync = GWT.create(ConversationService.class);

        conversationServiceAsync.addMessage(conversation, newMessage, callback);

    }

}

