package com.chat.client.Views;

import com.chat.client.Presenters.GlobalConversationPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;

public class GlobalConversationView extends Composite implements HasWidgets, GlobalConversationPresenter.Display {
    Panel container;
    Panel messageBox;
    Panel sendsContainer;
    TextBox message;
    Button newMessage;
    Button newFile;
    FormPanel fileUploadPanel;
    FileUpload fileUpload;
    Label error;

    private GlobalConversationPresenter presenter;

    public GlobalConversationView() {
        container = new AbsolutePanel();
        newMessage = new Button("Enviar");
        newFile = new Button("Cargar imagen o audio");
        message = new TextBox();
        messageBox = new VerticalPanel();
        sendsContainer = new HorizontalPanel();
        error = new Label();
        container.add(error);

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
        fileUploadPanel.setAction(GWT.getModuleBaseURL() + "fileupload?userid=" + Cookies.getCookie("UserID") + "&conversationid=" + 1);

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

    @Override
    public void setError(String error){
        this.error.setText(error);
    }

    @Override
    public void setPresenter(GlobalConversationPresenter presenter){
        this.presenter = presenter;
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
    public GlobalConversationView getViewInstance() {
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
        newImageMessage.setUrl(GWT.getHostPageBaseURL() + message);
        newImageMessage.setStyleName(style);
        messageBox.add(newImageMessage);
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
        newAudioMessage.setSrc(GWT.getHostPageBaseURL() + message);
        newAudioMessage.setStyleName(style);

        messageBox.add(newAudioMessage);
        Button audioMessageButton = new Button();
        audioMessageButton.setText("Reproducir");
        audioMessageButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                newAudioMessage.getAudioElement().play();
            }
        });
        messageBox.add(audioMessageButton);
    }

}