package com.chat.client.Views;

import com.chat.client.Models.User;
import com.chat.client.Presenters.PrivateAdministrationPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;
import java.util.List;

public class PrivateAdministrationView extends Composite implements HasWidgets, PrivateAdministrationPresenter.Display {

    Panel container;
    private FlexTable allUsersTable;

    private PrivateAdministrationPresenter presenter;

    public PrivateAdministrationView(){
        container = new AbsolutePanel();
        container.setStyleName("users-list");
        allUsersTable = new FlexTable();
        allUsersTable.setStyleName("users-list-col");
        container.add(allUsersTable);
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
    public void setPresenter(PrivateAdministrationPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public Widget getViewInstance() {
        return this;
    }

    @Override
    public void selectChatButton(Button button){
        for(int rowIndex = 0; rowIndex < allUsersTable.getRowCount()-1; rowIndex++){
            //allUsersTable.getWidget(rowIndex, 2).setStyleName("");
        }

        button.setStyleName("active");
    }

    @Override
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
                    if (presenter != null) {
                        User user = new User(openPrivateConversation.getLayoutData().toString());
                        presenter.goToPrivateConversation(user, openPrivateConversation);
                    }
                }
            });

            allUsersTable.setWidget(idx, 2, openPrivateConversation);

        }
    }
}