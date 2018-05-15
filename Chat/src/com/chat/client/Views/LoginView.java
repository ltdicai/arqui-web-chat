package com.chat.client.Views;

import com.chat.client.Presenters.LoginPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;

public class LoginView extends Composite implements HasWidgets, LoginPresenter.Display {

    FlowPanel container;
    VerticalPanel loginContainer;
    FlowPanel loginBox;
    TextBox userId;
    Button loginButton;
    LoginPresenter presenter;

    public LoginView(){
        container = new FlowPanel();
        container.addStyleName("login-view");

        loginBox = new FlowPanel();
        loginBox.addStyleName("box");

        loginContainer = new VerticalPanel();
        loginContainer.addStyleName("form");
        loginContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        HTMLPanel loginInfo = new HTMLPanel("<h2>Ingresa tu alias</h2>");

        loginButton = new Button("Login");
        userId = new TextBox();

        loginContainer.add(loginInfo);
        loginContainer.add(userId);
        loginContainer.add(loginButton);

        loginBox.add(loginContainer);

        container.add(loginBox);

        userId.setFocus(true);

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