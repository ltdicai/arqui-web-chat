package com.chat.client.Views;

import com.chat.client.Presenters.ConversationPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;

public class ConversationView extends Composite implements HasWidgets, ConversationPresenter.Display {
    Panel container;
    Panel messageBox;
    Panel sendsContainer;
    Panel Title;
    TextBox message;
    Button newMessage;
    Button newFile;
    FormPanel fileUploadPanel;
    FileUpload fileUpload;
    Label error;
    Label title;

    private ConversationPresenter presenter;

    public ConversationView() {
        container = new VerticalPanel();
        container.setStyleName("chat-container");
        Title = new HorizontalPanel();
        Title.setStyleName("title");
        title = new Label();
        Title.add(title);

        newMessage = new Button("Enviar");
        newFile = new Button("Cargar imagen o audio");
        message = new TextBox();
        messageBox = new VerticalPanel();
        messageBox.setStyleName("messagesList");
        sendsContainer = new HorizontalPanel();
        sendsContainer.setStyleName("send-options");
        error = new Label();
        container.add(error);

        container.add(Title);
        container.add(messageBox);
        container.add(sendsContainer);
        sendsContainer.add(message);
        sendsContainer.add(newMessage);
        sendsContainer.add(newFile);

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
                if(presenter != null){
                    presenter.uploadFileInit();
                }
            }
        });
        fileUpload.setName("fileuploadchat");
        fileUploadPanel.add(fileUpload);

        newFile.addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                if(presenter != null){
                    presenter.uploadFile();
                }
            }
        });

        newMessage.addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                if(presenter != null){
                    String messageText = message.getText();
                    message.setText("");
                    presenter.sendTextMessage(messageText);
                }
            }
        });
        fileUploadPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler(){
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                if(presenter != null){
                    presenter.uploadFileComplete();
                }
            }
        });

    }

    private void setAction(){
        fileUploadPanel.setAction(GWT.getModuleBaseURL() + "fileupload?userid=" + Cookies.getCookie("UserID") +
               "&conversationid=" + presenter.getIdConversation());
    }

    @Override
    public void setError(String error){
        this.error.setText(error);
    }

    @Override
    public void setPresenter(ConversationPresenter presenter){
        this.presenter = presenter;
        setAction();
    }

    @Override
    public void setTitle(String title){
        this.title.setText(title);
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
    public void clearFiles(){
        fileUpload.getElement().setPropertyString("value", "");
    }

    @Override
    public void clear() {
        container.clear();
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
    public ConversationView getViewInstance() {
        return this;
    }

    @Override
    public void setVisibleFileUploadPanel(boolean visibility) {
        fileUploadPanel.setVisible(visibility);
    }

    @Override
    public  void newTextMessageForOthers(String message, String etiqueta) {
        newTextMessage(message, etiqueta, "him");
    }

    @Override
    public  void newTextMessageForMe(String message) {
        newTextMessage(message, "Tu", "me");
    }

    private void newTextMessage(String message, String etiqueta, String style) {
        String recuadro = etiqueta + ": " + message;
        Label newTextMessageLabel = new Label();
        newTextMessageLabel.setText(recuadro);
        newTextMessageLabel.setStyleName(style);
        messageBox.add(newTextMessageLabel);
    }

    @Override
    public  void newImageMessageForOthers(String message, String etiqueta) {
        newImageMessage(message, etiqueta, "him");
    }

    @Override
    public  void newImageMessageForMe(String message) {
        newImageMessage(message, "Tu", "me");
    }

    private void newImageMessage(String message, String etiqueta, String style) {
        Image newImageMessage = new Image(message);
        newImageMessage.setUrl(message);
        newImageMessage.setAltText(etiqueta + ":");
        newImageMessage.setStyleName(style);
        Panel panel = new AbsolutePanel();
        Label etiquetaLabel = new Label(etiqueta + ": ");
        panel.add(etiquetaLabel);
        panel.add(newImageMessage);
        panel.setStyleName(style + " me-him-container");

        messageBox.add(panel);
    }

    @Override
    public void newAudioMessageForOthers(String message, String etiqueta) {
        newAudioMessage(message, etiqueta, "him");
    }

    @Override
    public  void newAudioMessageForMe(String message) {
        newAudioMessage(message, "Tu", "me");
    }

    private void newAudioMessage(String message, String etiqueta, String style) {
        Audio newAudioMessage = Audio.createIfSupported();
        newAudioMessage.setSrc(message);
        newAudioMessage.setStyleName(style);
        messageBox.add(newAudioMessage);
        Button audioMessageButton = new Button();
        audioMessageButton.setText("Reproducir");
        newAudioMessage.addEndedHandler(new EndedHandler() {
            @Override
            public void onEnded(EndedEvent event) {
                audioMessageButton.setText("Reproducir");
                newAudioMessage.setCurrentTime(0);
            }
        });

        audioMessageButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                if(audioMessageButton.getText() == "Reproducir"){
                    audioMessageButton.setText("Detener");
                    newAudioMessage.getAudioElement().play();
                }
                else{
                    audioMessageButton.setText("Reproducir");
                    newAudioMessage.getAudioElement().pause();
                }
            }
        });
        Panel panel = new AbsolutePanel();
        Label etiquetaLabel = new Label(etiqueta + ": ");
        panel.add(etiquetaLabel);
        panel.add(audioMessageButton);
        panel.setStyleName(style + " me-him-container");
        messageBox.add(panel);
    }

}