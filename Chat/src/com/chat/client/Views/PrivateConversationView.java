package com.chat.client.Views;

import com.chat.client.Models.ImageMessage;
import com.chat.client.Models.Message;
import com.chat.client.Models.TextMessage;
import com.chat.client.Models.User;
import com.chat.client.Presenters.PrivateConversationPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class PrivateConversationView implements PrivateConversationPresenter.Display {
    private Panel mainContainer;
    private Panel messagesContainer;
    private FlexTable messagesList;
    private Panel sendOptions;
    private Button sendButton;
    private PrivateConversationPresenter presenter = null;

    public PrivateConversationView(){
        mainContainer = new FlowPanel();
        mainContainer.setStyleName("chat-container");
        messagesContainer = new FlowPanel();
        messagesContainer.setStyleName("messages");
        messagesList = new FlexTable();
        messagesList.setStyleName("messages-list");

        messagesContainer.add(messagesList);


        sendOptions = new FlowPanel();
        sendOptions.setStyleName("send-options");
        sendButton = new Button("Send");
        TextArea textInput = new TextArea();

        sendOptions.add(textInput);
        sendOptions.add(sendButton);

        mainContainer.add(messagesContainer);
        mainContainer.add(sendOptions);

        sendButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {


            }
        });
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
    public void updateMessages(List<Message> listMessage) {
        Window.alert("Received messages");
        messagesList.removeAllRows();
        int messageCounter = 0;
        for (Message message : listMessage) {
            if (message instanceof TextMessage) {
                messagesList.setHTML(
                        messageCounter,
                        0,
                        "<p>" + ((TextMessage) message).getMessage() + "</p>"
                );
            } else if (message instanceof ImageMessage) {
                messagesList.setHTML(
                        messageCounter,
                        0,
                        "<img src=\"data:image/png;base64, "
                                + ((ImageMessage) message).getImage()
                                + "\" />"
                );
            } else {
                messagesList.setHTML(
                        messageCounter,
                        0,
                        "<i>AUDIO</i>"
                );
            }
            messageCounter++;
        }
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

    @Override
    public void setPresenter(PrivateConversationPresenter presenter) {
        this.presenter = presenter;
        presenter.updateMessage();
    }

}
