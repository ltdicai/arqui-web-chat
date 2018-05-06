package com.Chat.client.Views;

import com.Chat.client.Models.GlobalConversation;
import com.Chat.client.Models.Message;
import com.Chat.client.Models.TextMessage;
import com.Chat.client.Presenters.GlobalConversationPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;
import java.util.List;

public class GlobalConversationView extends Composite implements HasWidgets, GlobalConversationPresenter.Display {
    HorizontalPanel container;
    TextBox message;
    Button newMessage;
    VerticalPanel messageBox;


    public GlobalConversationView(){
        container = new HorizontalPanel();
        newMessage = new Button("Enviar");
        message = new TextBox();
        messageBox = new VerticalPanel();

        container.add(message);
        container.add(newMessage);
        container.add(messageBox);
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
    public GlobalConversationView getViewInstance() {
        return this;
    }

    @Override
    public String sendTextMessage(){
        String messageText = message.getText();
        Label messageLabel = newTextMessageLabel(messageText);
        messageBox.add(messageLabel);
        return messageText;
    }
    
    @Override
    public void updateMessages(List<Message> listMessages){
        for (Message item : listMessages) {
            if(item.getClass() == TextMessage.class){
                TextMessage messageText = (TextMessage)item;
                messageBox.add(newTextMessageLabel(messageText.getMessage()));
            }
        }
    }

    private Label newTextMessageLabel(String messageText){
        String recuadro = ": " + messageText;
        Label newTextMessageLabel = new Label();
        return newTextMessageLabel;
    }

}