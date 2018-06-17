package com.chat.client.Presenters;

import com.chat.client.Models.GroupConversation;
import com.chat.client.Models.Message;
import com.chat.client.Models.TextMessage;
import com.chat.client.Models.User;
import com.chat.client.Services.GroupConversationService;
import com.chat.client.Services.GroupConversationServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import java.util.List;

public class GroupConversationPresenter implements ConversationPresenter.SpecificConversationPresenter {

    private ConversationPresenter conversationPresenter;
    private Timer timer;
    private GroupConversation conversation;
    private User loggedUser;

    public GroupConversationPresenter(ConversationPresenter.Display view, User user, GroupConversation GroupConversation) {
        this.conversation = GroupConversation;
        this.conversationPresenter = new ConversationPresenter(view, user, GroupConversation);
        this.loggedUser = user;
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

        AsyncCallback<GroupConversation> callback = new AsyncCallback<GroupConversation>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(GroupConversation GroupConversation) {
                List<Message> messageList = GroupConversation.getMessages();
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

        GroupConversationServiceAsync conversationServiceAsync = GWT.create(GroupConversationService.class);

        conversationServiceAsync.getGroupConversation(conversation.getId(), conversationPresenter.getMessageCount(),  callback);
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

        GroupConversationServiceAsync conversationServiceAsync = GWT.create(GroupConversationService.class);

        conversationServiceAsync.addMessage(conversation, newMessage, callback);

    }

}

