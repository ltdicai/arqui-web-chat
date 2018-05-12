package com.chat.client.Views;

import com.chat.client.Models.Message;
import com.chat.client.Models.TextMessage;
import com.chat.client.Presenters.GlobalConversationPresenter;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class GlobalConversationView extends Composite implements HasWidgets, GlobalConversationPresenter.Display {
    Panel container;
    Panel messageBox;
    Panel sendsContainer;
    TextBox message;
    Button newMessage;

    public GlobalConversationView(){
        container = new AbsolutePanel();
        newMessage = new Button("Enviar");
        message = new TextBox();
        messageBox = new VerticalPanel();
        sendsContainer = new HorizontalPanel();

        container.add(messageBox);
        container.add(sendsContainer);
        sendsContainer.add(message);
        sendsContainer.add(newMessage);
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
    public GlobalConversationView getViewInstance() {
        return this;
    }

    @Override
    public String sendTextMessage(){
        String messageText = message.getText();;
        return messageText;
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


}