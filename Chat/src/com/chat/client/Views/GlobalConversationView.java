package com.chat.client.Views;

import com.chat.client.Models.AudioMessage;
import com.chat.client.Models.ImageMessage;
import com.chat.client.Models.Message;
import com.chat.client.Models.TextMessage;
import com.chat.client.Presenters.GlobalConversationPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;
import java.util.Stack;

public class GlobalConversationView extends Composite implements HasWidgets, GlobalConversationPresenter.Display {
    Panel container;
    Panel messageBox;
    Panel sendsContainer;
    TextBox message;
    Button newMessage;
    Button newImage;
    Button newAudio;
    FormPanel fileUploadPanel;
    FileUpload fileUpload;

    public GlobalConversationView() {
        container = new AbsolutePanel();
        newMessage = new Button("Enviar");
        newImage = new Button("Cargar imagen");
        newAudio = new Button("Cargar audio");
        message = new TextBox();
        messageBox = new VerticalPanel();
        sendsContainer = new HorizontalPanel();

        container.add(messageBox);
        container.add(sendsContainer);
        sendsContainer.add(message);
        sendsContainer.add(newMessage);
        sendsContainer.add(newImage);

        fileUploadPanel = new FormPanel();
        fileUploadPanel.setVisible(false);
        container.add(fileUploadPanel);
        fileUploadPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
        fileUploadPanel.setMethod(FormPanel.METHOD_POST);
        fileUpload = new FileUpload();
        fileUpload.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                fileUploadPanel.submit();
            }
        });
        fileUpload.setName("fileuploadchat");
        fileUploadPanel.add(fileUpload);
        fileUploadPanel.setAction(GWT.getModuleBaseURL() + "fileupload?userid=" + Cookies.getCookie("UserID") + "&conversationid=" + 1);
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    @Override
    public void add(Widget w) {
        container.add(w);
    }

    @Override
    public void clear() {
        container.clear();
    }

    @Override
    public void clearText() {
        message.setText("");
    }

    @Override
    public Iterator<Widget> iterator() {
        return container.iterator();
    }

    @Override
    public boolean remove(Widget w) {
        return container.remove(w);
    }

    @Override
    public HasClickHandlers getSendTextMessageBotton() {
        //return button to implement its events in the Presenter
        return newMessage;
    }

    @Override
    public HasClickHandlers getSendImageMessageBotton() {
        //return button to implement its events in the Presenter
        return newImage;
    }

    @Override
    public HasClickHandlers getSendAudioMessageBotton() {
        //return button to implement its events in the Presenter
        return newAudio;
    }

    @Override
    public GlobalConversationView getViewInstance() {
        return this;
    }

    @Override
    public String sendTextMessage() {
        String messageText = message.getText();
        return messageText;
    }

    @Override
    public void setVisibleFileUploadPanel(boolean visibility) {
        fileUploadPanel.setVisible(visibility);
    }

    @Override
    public FormPanel getFileUploadPanel() {
        return fileUploadPanel;
    }

    @Override
    public void updateMessages(Stack<Message> listMessages) {
        newMessage.setText(listMessages.toString());
        for (Message item : listMessages) {
            if (item.getClass() == TextMessage.class) {
                TextMessage messageText = (TextMessage) item;
                Label newTextMessageLabel;
                if (item.getUser().getUserID() == Cookies.getCookie("UserID")) {
                    newTextMessageLabel = newTextMessageForMe(messageText);
                } else {
                    newTextMessageLabel = newTextMessageForOthers(messageText);
                }
                messageBox.add(newTextMessageLabel);
            } else if (item.getClass() == ImageMessage.class) {
                ImageMessage messageText = (ImageMessage) item;
                Image newTextMessageLabel;
                if (item.getUser().getUserID() == Cookies.getCookie("UserID")) {
                    newTextMessageLabel = newImageMessageForMe(messageText);
                } else {
                    newTextMessageLabel = newImageMessageForOthers(messageText);
                }
                messageBox.add(newTextMessageLabel);
            } else if (item.getClass() == AudioMessage.class) {
                AudioMessage messageText = (AudioMessage) item;
                Audio newTextMessageLabel;
                if (item.getUser().getUserID() == Cookies.getCookie("UserID")) {
                    newTextMessageLabel = newAudioMessageForMe(messageText);
                } else {
                    newTextMessageLabel = newAudioMessageForOthers(messageText);
                }
                messageBox.add(newTextMessageLabel);
                Button audioMessageButton = new Button();
                audioMessageButton.setText("Reproducir");
                audioMessageButton.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        newTextMessageLabel.getAudioElement().play();
                    }
                });
                messageBox.add(audioMessageButton);
            }
        }
    }

    private Label newTextMessageForOthers(TextMessage message) {
        return newTextMessage(message, message.getUser().getUserID(), "him");
    }

    private Label newTextMessageForMe(TextMessage message) {
        return newTextMessage(message, "Tu", "me");
    }

    private Label newTextMessage(TextMessage message, String etiqueta, String style) {
        String recuadro = etiqueta + ": " + message.getMessage();
        Label newTextMessageLabel = new Label();
        newTextMessageLabel.setText(recuadro);
        newTextMessageLabel.setStyleName(style);
        return newTextMessageLabel;
    }

    private Image newImageMessageForOthers(ImageMessage message) {
        return newImageMessage(message, message.getUser().getUserID(), "him");
    }

    private Image newImageMessageForMe(ImageMessage message) {
        return newImageMessage(message, "Tu", "me");
    }

    private Image newImageMessage(ImageMessage message, String etiqueta, String style) {
        Image newImageMessage = new Image();
        newImageMessage.setUrl(GWT.getHostPageBaseURL() + message.getImage());
        newImageMessage.setStyleName(style);
        return newImageMessage;
    }

    private Audio newAudioMessageForOthers(AudioMessage message) {
        return newAudioMessage(message, message.getUser().getUserID(), "him");
    }

    private Audio newAudioMessageForMe(AudioMessage message) {
        return newAudioMessage(message, "Tu", "me");
    }

    private Audio newAudioMessage(AudioMessage message, String etiqueta, String style) {
        Audio newAudioMessage = Audio.createIfSupported();
        newAudioMessage.setSrc(GWT.getHostPageBaseURL() + message.getAudio());
        newAudioMessage.setStyleName(style);

        return newAudioMessage;
    }

}