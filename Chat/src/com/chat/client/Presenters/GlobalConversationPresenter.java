package com.chat.client.Presenters;

import com.chat.client.Models.*;
import com.chat.client.Services.GlobalConversationDataService;
import com.chat.client.Services.GlobalConversationDataServiceAsync;
import com.chat.client.Views.GlobalConversationView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.Stack;

public class GlobalConversationPresenter {
    public interface Display {
        Widget asWidget();

        GlobalConversationView getViewInstance();

        void clearText();

        void setVisibleFileUploadPanel(boolean visibility);

        void setPresenter(GlobalConversationPresenter presenter);

        void newTextMessageForMe(String message);
        void newImageMessageForMe(String message);
        void newAudioMessageForMe(String message);

        void newTextMessageForOthers(String message, String etiqueta);
        void newImageMessageForOthers(String message, String etiqueta);
        void newAudioMessageForOthers(String message, String etiqueta);

        void setError(String error);
    }

    private int messageCount;
    private Timer timer;
    final GlobalConversationPresenter.Display view;
    private User user;

    public GlobalConversationPresenter(GlobalConversationPresenter.Display view, User user) {
        this.view = view;
        this.user = user;
        this.timer = new Timer() {
            @Override
            public void run() {
                updateMessage();
            }
        };
        this.timer.scheduleRepeating(2000);
        messageCount = 0;
    }

    public void bind() {
        view.setPresenter(this);
    }

    public void go(final HasWidgets container) {
        bind();
        container.clear();
        container.add(view.getViewInstance().asWidget());
    }

    public Display getView() {
        return view;
    }

    public void updateMessage() {
        AsyncCallback<GlobalConversation> callback = new AsyncCallback<GlobalConversation>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(GlobalConversation globalConversation) {
                Stack<Message> messageList = globalConversation.getMessages();
                messageCount += messageList.size();

                getView().setError(String.valueOf(messageCount));
                updateMessageInView(messageList);
            }
        };

        GlobalConversationDataServiceAsync globalConversationDataServiceAsync = GWT.create(GlobalConversationDataService.class);

        globalConversationDataServiceAsync.get(messageCount, callback);

    }

    private void updateMessageInView(Stack<Message> messageList){
        for (Message item : messageList) {
            if (item.getClass() == TextMessage.class) {
                TextMessage messageText = (TextMessage) item;
                if (item.getUser().getUserID() == Cookies.getCookie("UserID")) {
                    getView().newTextMessageForMe(messageText.getMessage());
                } else {
                    getView().newTextMessageForOthers(messageText.getMessage(), user.getUserID());
                }

            } else if (item.getClass() == ImageMessage.class) {
                ImageMessage messageText = (ImageMessage) item;
                if (item.getUser().getUserID() == Cookies.getCookie("UserID")) {
                    getView().newImageMessageForMe(messageText.getImage());
                } else {
                    getView().newImageMessageForOthers(messageText.getImage(), user.getUserID());
                }
            } else if (item.getClass() == AudioMessage.class) {
                AudioMessage messageText = (AudioMessage) item;
                if (item.getUser().getUserID() == Cookies.getCookie("UserID")) {
                    getView().newAudioMessageForMe(messageText.getAudio());
                } else {
                    getView().newAudioMessageForOthers(messageText.getAudio(), user.getUserID());
                }

            }
        }
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


    public void uploadFile() {
        getView().setVisibleFileUploadPanel(true);
    }


    public void uploadFileInit() {
        getView().setVisibleFileUploadPanel(true);
    }

    public void uploadFileComplete()
    {
        updateMessage();
    }

}

