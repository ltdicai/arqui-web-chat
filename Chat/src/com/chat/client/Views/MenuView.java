package com.chat.client.Views;

import com.chat.client.Models.User;
import com.chat.client.Presenters.MenuPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;
import java.util.List;

public class MenuView extends Composite implements HasWidgets, MenuPresenter.Display {

    Panel container;
    Panel subContainerUser;
    Panel subConteinerChats;
    Panel subConteinerButtons;
    Button logout;
    Button globalConversation;
    Button privateComversation;
    Button testPrivateConversation;
    Button groups;
    Label userNameLabel;
    private FlexTable allUsersTable;

    private MenuPresenter presenter;

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

        allUsersTable = new FlexTable();

        subConteinerButtons.add(globalConversation);
        subConteinerButtons.add(privateComversation);
        subConteinerButtons.add(groups);

        subConteinerChats.add(globalConversation);

        container.add(subContainerUser);
        container.add(subConteinerButtons);
        container.add(subConteinerChats);
        container.add(allUsersTable);

        logout.addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                if(presenter != null){
                    presenter.Logout();
                }
            }
        });

        globalConversation.addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                if(presenter != null){
                    presenter.goToGlobalConversation();
                }
            }
        });

    }

    @Override
    public Widget asWidget() {
        return container;
    }

    @Override
    public void setPresenter(MenuPresenter presenter){
        this.presenter = presenter;
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

    public HTMLTable getAllUsersTable() {
        return allUsersTable;
    }

    public void showUsers(List<User> users) {
        allUsersTable.removeAllRows();
        for(int idx = 0; idx < users.size(); ++idx) {
            User user = users.get(idx);
            if (user.equals(presenter.getLoggedUser())) {
                continue;
            }
            allUsersTable.setText(idx, 0,  user.getUserID());
            allUsersTable.setText(idx, 1,  "offline");
            Button openPrivateConversation = new Button("Privado");
            openPrivateConversation.setLayoutData(users.get(idx).getUserID());
            openPrivateConversation.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    Window.alert((String) openPrivateConversation.getLayoutData());
                    if (presenter != null) {
                        presenter.goToPrivateConversation(user);
                    }
                }
            });

            allUsersTable.setWidget(idx, 2, openPrivateConversation);

        }
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