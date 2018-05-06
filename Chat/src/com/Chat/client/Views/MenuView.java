package com.Chat.client.Views;

import com.Chat.client.AppController;
import com.Chat.client.Models.AppModel;
import com.Chat.client.Presenters.GlobalConversationPresenter;
import com.Chat.client.Presenters.MenuPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;

public class MenuView implements HasWidgets, MenuPresenter.Display {

    VerticalPanel container;
    HorizontalPanel subConteiner;
    Button logout;
    Button globalConversation;
    Button privateComversation;
    Button groupsConversation;
    Label label;

    public MenuView(){
        label = new Label();
        subConteiner = new HorizontalPanel();
        container = new VerticalPanel();
        logout = new Button("Logout");
        globalConversation = new Button("Chat Global");
        privateComversation = new Button("Chats Privados");
        groupsConversation = new Button("Chats Grupales");

        container.add(label);
        container.add(logout);
        container.add(subConteiner);
        container.add(globalConversation);
        container.add(privateComversation);
        container.add(groupsConversation);

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
    public Button getLogout() {
        return logout;
    }

    @Override
    public Button getButtonGlobalConversation() {
        return globalConversation;
    }

    @Override
    public Button getButtonPrivateConversation() {
        return privateComversation;
    }

    @Override
    public Button getButtonGroupsConversation() {
        return groupsConversation;
    }

    @Override
    public MenuView getViewInstance(){

        if(this == null)
            return new MenuView();
        else
            return this;
    }

    @Override
    public void setLabel(String text){
        label.setText(text);
    }

    @Override
    public HasWidgets getSubContainer(){
        return this.subConteiner;
    }

}