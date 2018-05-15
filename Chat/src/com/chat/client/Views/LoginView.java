package com.chat.client.Views;

import com.chat.client.Presenters.LoginPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;

public class LoginView extends Composite implements HasWidgets, LoginPresenter.Display {

    HorizontalPanel container;
    TextBox userId;
    Button loginButton;
    LoginPresenter presenter;

    public LoginView(){
        container = new HorizontalPanel();
        loginButton = new Button("Login");
        userId = new TextBox();

        container.add(userId);
        container.add(loginButton);

        loginButton.addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                if(presenter != null){
                    presenter.Login(userId.getText());
                }
            }
        });
    }

    @Override
    public void setPresenter(LoginPresenter presenter){
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
    public Iterator<Widget> iterator() {
        return container.iterator();
    }

    @Override
    public boolean remove(Widget w) {
        return container.remove(w);
    }

    @Override
    public LoginView getViewInstance() {
        return this;
    }


}