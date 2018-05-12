package com.chat.client.Views;

import com.chat.client.Presenters.MenuPresenter;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;

public class MenuView extends Composite implements HasWidgets, MenuPresenter.Display {

    Panel container;
    Panel subContainerUser;
    Panel subConteinerChats;
    Panel subConteinerButtons;
    Button logout;
    Button globalConversation;
    Button privateComversation;
    Button groups;
    Label userNameLabel;

    public MenuView(){
        container = new AbsolutePanel();

        subContainerUser = new HorizontalPanel();
        subConteinerButtons = new HorizontalPanel();
        subConteinerChats = new HorizontalPanel();

        userNameLabel = new Label();
        logout = new Button("Logout");

        subContainerUser.add(userNameLabel);
        subContainerUser.add(logout);

        globalConversation = new Button("Chat Global");
        privateComversation = new Button("Chats Privados");
        groups = new Button("Chats Grupales");

        subConteinerButtons.add(globalConversation);
        subConteinerButtons.add(privateComversation);
        subConteinerButtons.add(groups);

        subConteinerChats.add(globalConversation);

        container.add(subContainerUser);
        container.add(subConteinerButtons);
        container.add(subConteinerChats);
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
    public Button getButtonGroups() {
        return groups;
    }

    @Override
    public MenuView getViewInstance(){
        return this;
    }

    @Override
    public void setLabel(String text){
        userNameLabel.setText(text);
    }

    @Override
    public HasWidgets getSubContainerChat(){
        return this.subConteinerChats;
    }

}