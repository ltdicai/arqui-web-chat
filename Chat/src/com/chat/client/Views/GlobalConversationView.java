package com.chat.client.Views;

import com.chat.client.Models.ImageMessage;
import com.chat.client.Models.Message;
import com.chat.client.Models.TextMessage;
import com.chat.client.Presenters.GlobalConversationPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
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

    public GlobalConversationView(){
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
    public String sendTextMessage(){
        String messageText = message.getText();;
        return messageText;
    }

    @Override
    public void setVisibleFileUploadPanel(boolean visibility){
        fileUploadPanel.setVisible(visibility);
    }

    @Override
    public FormPanel getFileUploadPanel(){
        return fileUploadPanel;
    }

    @Override
    public void updateMessages(Stack<Message> listMessages){
        messageBox.clear();
        for (Message item : listMessages) {
            if(item.getClass() == TextMessage.class){
                TextMessage messageText = (TextMessage)item;
                Label newTextMessageLabel;
                if(item.getUser().getUserID() == Cookies.getCookie("UserID")){
                    newTextMessageLabel = newTextMessageLabelForMe(messageText);
                }
                else{
                    newTextMessageLabel = newTextMessageLabelForOthers(messageText);
                }
                messageBox.add(newTextMessageLabel);
            }
            else if(item.getClass() == ImageMessage.class){
                    ImageMessage messageText = (ImageMessage)item;
                    Image newTextMessageLabel;
                    if(item.getUser().getUserID() == Cookies.getCookie("UserID")){
                        newTextMessageLabel = newImageMessageLabelForMe(messageText);
                    }
                    else{
                        newTextMessageLabel = newImageMessageLabelForOthers(messageText);
                    }
                    messageBox.add(newTextMessageLabel);
            }
        }
    }

    private Label newTextMessageLabelForOthers(TextMessage message){
        return newTextMessageLabel(message, message.getUser().getUserID(), "him");
    }

    private Label newTextMessageLabelForMe(TextMessage message){
        return newTextMessageLabel(message, "Tu", "me");
    }

    private Label newTextMessageLabel(TextMessage message, String etiqueta, String style){
        String recuadro = etiqueta + ": " + message.getMessage();
        Label newTextMessageLabel = new Label();
        newTextMessageLabel.setText(recuadro);
        newTextMessageLabel.setStyleName(style);
        return newTextMessageLabel;
    }

    private Image newImageMessageLabelForOthers(ImageMessage message){
        return newImageMessageLabel(message, message.getUser().getUserID(), "him");
    }

    private Image newImageMessageLabelForMe(ImageMessage message){
        return newImageMessageLabel(message, "Tu", "me");
    }

    private Image newImageMessageLabel(ImageMessage message, String etiqueta, String style){
        Image newImageMessage = new Image();
        //newImageMessage.setUrl(message.getImage());
        newImageMessage.setStyleName(style);
        return newImageMessage;
    }

}