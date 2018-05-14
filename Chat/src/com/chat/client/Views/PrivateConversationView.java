package com.chat.client.Views;

import com.chat.client.Models.Message;
import com.chat.client.Presenters.PrivateConversationPresenter;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Iterator;
import java.util.Stack;

public class PrivateConversationView implements PrivateConversationPresenter.Display {
    private Panel mainContainer;
    private Panel chatContainer;
    private Button sendButton;

    public PrivateConversationView(){
        mainContainer = new HorizontalPanel();
        chatContainer = new HorizontalPanel();
        sendButton = new Button("Send");
        Window.alert("Created this view!");
        mainContainer.add(chatContainer);
        mainContainer.add(sendButton);
    }

    @Override
    public HasClickHandlers getSendTextMessageBotton() {
        return null;
    }

    @Override
    public HasClickHandlers getSendImageMessageBotton() {
        return null;
    }

    @Override
    public HasClickHandlers getSendAudioMessageBotton() {
        return null;
    }

    @Override
    public Widget asWidget() {
        return mainContainer;
    }

    @Override
    public PrivateConversationView getViewInstance() {
        return this;
    }

    @Override
    public String sendTextMessage() {
        return null;
    }

    @Override
    public void updateMessages(Stack<Message> listMessage) {
        Window.alert("Updated messages");

    }

    @Override
    public void clearText() {

    }

    @Override
    public void setVisibleFileUploadPanel(boolean visibility) {

    }

    @Override
    public FormPanel getFileUploadPanel() {
        return null;
    }

}
