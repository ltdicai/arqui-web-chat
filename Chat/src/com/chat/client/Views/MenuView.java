package com.chat.client.Views;

import com.chat.client.Models.User;
import com.chat.client.Presenters.MenuPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;
import java.util.List;

public class MenuView extends Composite implements HasWidgets, MenuPresenter.Display {

    private Panel mainContainer;
    Panel container;
    Panel subContainerUser;
    Panel subConteinerChats;
    Panel subConteinerButtons;
    Button logout;
    Button globalConversation;
    Button privateConversation;
    Button testPrivateConversation;
    Button groups;
    Label userNameLabel;
    private FlexTable allUsersTable;

    private MenuPresenter presenter;
    private Timer timer;

    public MenuView(){
        container = new AbsolutePanel();

        mainContainer = new FlowPanel();
        mainContainer.setStyleName("menu-view");
        FlowPanel actionsColumn = new FlowPanel();
        actionsColumn.setStyleName("actions");
        FlowPanel chatColumn = new FlowPanel();
        chatColumn.setStyleName("chat");

        mainContainer.add(actionsColumn);
        mainContainer.add(chatColumn);

        FlowPanel userInfo = new FlowPanel();
        userInfo.setStyleName("user-info");
        FlowPanel conversationPicker = new FlowPanel();
        conversationPicker.setStyleName("conv-picker");
        FlowPanel allUsers = new FlowPanel();
        allUsers.setStyleName("all-users");

        userNameLabel = new Label();
        logout = new Button("Logout");

        userInfo.add(userNameLabel);
        userInfo.add(logout);

        globalConversation = new Button("Global");
        privateConversation = new Button("Privado");
        groups = new Button("Grupos");

        // Conversation picker
        conversationPicker.add(globalConversation);
        conversationPicker.add(privateConversation);
        conversationPicker.add(groups);

        // All users table
        allUsersTable = new FlexTable();

        allUsers.add(allUsersTable);

        actionsColumn.add(userInfo);
        actionsColumn.add(conversationPicker);
        actionsColumn.add(allUsers);

        FlowPanel messages = new FlowPanel();
        messages.setStyleName("messages");
        FlowPanel sendActions = new FlowPanel();
        sendActions.setStyleName("send-actions");

        subContainerUser = new HorizontalPanel();
        subConteinerButtons = new HorizontalPanel();
        subConteinerChats = chatColumn;

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


        // Update user list
        timer = new Timer() {
            @Override
            public void run() {
                if (presenter != null) {
                    presenter.updateUserList();
                }
            }
        };
        timer.scheduleRepeating(200);

    }

    @Override
    public Widget asWidget() {
        return mainContainer;
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
    public void doLogout() {
        timer.cancel();
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