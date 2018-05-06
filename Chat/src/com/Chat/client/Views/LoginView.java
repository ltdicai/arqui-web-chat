package com.Chat.client.Views;

import com.Chat.client.Presenters.LoginPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;

public class LoginView extends Composite implements HasWidgets, LoginPresenter.Display {

    HorizontalPanel container;
    TextBox userId;
    Button loginButton;


    public LoginView(){
        container = new HorizontalPanel();
        loginButton = new Button("Login");
        userId = new TextBox();

        container.add(userId);
        container.add(loginButton);
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
    public HasClickHandlers getLoginButton() {
        //return button to implement its events in the Presenter
        return loginButton;
    }

    @Override
    public LoginView getViewInstance() {
        return this;
    }

    @Override
    public String getIdUser(){
        return userId.getText();
    }

}