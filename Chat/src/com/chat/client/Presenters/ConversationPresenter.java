package com.chat.client.Presenters;

import com.chat.client.Models.*;
import com.chat.client.Views.ConversationView;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;

import java.util.List;
import java.util.Stack;

public class ConversationPresenter {
    public interface Display {
        Widget asWidget();

        ConversationView getViewInstance();

        void clearText();

        void clearFiles();

        void setVisibleFileUploadPanel(boolean visibility);

        void setPresenter(ConversationPresenter presenter);

        void newTextMessageForMe(String message);
        void newImageMessageForMe(String message);
        void newAudioMessageForMe(String message);

        void newTextMessageForOthers(String message, String etiqueta);
        void newImageMessageForOthers(String message, String etiqueta);
        void newAudioMessageForOthers(String message, String etiqueta);

        void setError(String error);
    }

    public interface SpecificConversation{
        void sendTextMessage(String messageText);
    }

    private int messageCount;
    final ConversationPresenter.Display view;
    private User user;
    private SpecificConversation specificConversation;

    private int conversationid;

    public ConversationPresenter(ConversationPresenter.Display view, User user) {
        this.view = view;
        this.user = user;

        messageCount = 0;

        //this.conversationid = conversationid;
    }

    public int getIdConversation(){
        return this.conversationid;
    }

    public void setSpecificConversation(SpecificConversation specificConversation){
        this.specificConversation = specificConversation;
    }

    public void bind() {
        view.setPresenter(this);
    }

    public void go(final HasWidgets container) {
        bind();
        container.clear();
        container.add(view.getViewInstance().asWidget());
    }

    public void sendTextMessage(String messageText){
        specificConversation.sendTextMessage(messageText);
    }

    public Display getView() {
        return view;
    }

    public int getMessageCount(){
        return messageCount;
    }

    public void updateMessageInView(List<Message> messageList){
        messageCount += messageList.size();
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


    public void uploadFile() {
        getView().setVisibleFileUploadPanel(true);
    }


    public void uploadFileInit() {

    }

    public void uploadFileComplete()
    {
        getView().setVisibleFileUploadPanel(false);
        getView().clearFiles();
        //updateMessage();
    }

}

