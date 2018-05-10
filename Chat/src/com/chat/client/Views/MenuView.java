package com.chat.client.Views;

import com.chat.client.Presenters.MenuPresenter;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;

public class MenuView implements HasWidgets, MenuPresenter.Display {

    VerticalPanel container;
    HorizontalPanel subConteiner;
    Button logout;
    Button globalConversation;
    Button privateComversation;
    Button groups;
    Label label;

    public MenuView(){
        label = new Label();
        subConteiner = new HorizontalPanel();
        container = new VerticalPanel();
        logout = new Button("Logout");
        globalConversation = new Button("chat Global");
        privateComversation = new Button("Chats Privados");
        groups= new Button("Chats Grupales");

        container.add(label);
        container.add(logout);
        container.add(subConteiner);
        container.add(globalConversation);
        container.add(privateComversation);
        container.add(groups);

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