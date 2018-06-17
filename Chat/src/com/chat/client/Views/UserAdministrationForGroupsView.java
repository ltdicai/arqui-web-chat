package com.chat.client.Views;

import com.chat.client.Models.User;
import com.chat.client.Presenters.UserAdministrationForGroupsPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;
import java.util.List;

public class UserAdministrationForGroupsView extends Composite implements HasWidgets, UserAdministrationForGroupsPresenter.Display {

    private Panel mainContainer;
    Panel container;
    private FlexTable allUsersTable;

    private UserAdministrationForGroupsPresenter presenter;

    public UserAdministrationForGroupsView(){
        container = new AbsolutePanel();
        mainContainer = new FlowPanel();
        mainContainer.setStyleName("menu-view");

        allUsersTable = new FlexTable();

        mainContainer.add(allUsersTable);
    }

    @Override
    public Widget asWidget() {
        return mainContainer;
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
    public void setPresenter(UserAdministrationForGroupsPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public UserAdministrationForGroupsView getViewInstance() {
        return this;
    }

    @Override
    public void showUsers(List<User> users) {
        allUsersTable.removeAllRows();

        Button close = new Button("Cerrar");
        close.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (presenter != null) {
                    presenter.goBackToGroupAdministration();
                }
            }
        });
        allUsersTable.setWidget(0, 0,  close);

        for(int idx = 0; idx < users.size(); ++idx) {
            User user = users.get(idx);
            if (user.equals(presenter.getLoggedUser())) {
                continue;
            }
            allUsersTable.setText(idx+1, 0,  user.getUserID());
            allUsersTable.setText(idx+1, 1,  "offline");
            Button openGroupConversation = new Button("Agregar");
            openGroupConversation.setLayoutData(users.get(idx).getUserID());
            openGroupConversation.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (presenter != null) {
                        presenter.addUser(user);
                    }
                }
            });

            allUsersTable.setWidget(idx+1, 2, openGroupConversation);

        }
    }
}