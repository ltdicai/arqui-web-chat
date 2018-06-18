package com.chat.client.Views;

import com.chat.client.Models.GroupConversation;
import com.chat.client.Presenters.GroupAdministrationPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;
import java.util.List;

public class GroupAdministrationView extends Composite implements HasWidgets, GroupAdministrationPresenter.Display {

    private Panel mainContainer;
    Panel container;
    private FlexTable allGroupsTable;
    private GroupAdministrationPresenter presenter;
    private Label noGroups;
    private Button NewGroup;
    private TextBox groupName;
    VerticalPanel allUsers;
    VerticalPanel allGroups;
    Label error;
    public GroupAdministrationView(){
        container = new AbsolutePanel();
        container.setStyleName("groups-list");
        noGroups = new Label();
        noGroups.setText("Sin Grupos");
        mainContainer = new FlowPanel();
        FlexTable actionsColumn = new FlexTable();
        allUsers = new VerticalPanel();
        allUsers.setStyleName("groups-list-col");
        allGroups = new VerticalPanel();
        allGroups.setStyleName("groups-list-col");
        mainContainer.add(actionsColumn);

        groupName = new TextBox();
        NewGroup = new Button("Nuevo grupo");
        NewGroup.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                setError("");
                presenter.NewGroup(groupName.getText());
            }
        });

        allGroupsTable = new FlexTable();

        error = new Label();
        error.setStyleName("error");

        allGroups.add(error);
        allGroups.add(noGroups);
        allGroups.add(NewGroup);
        allGroups.add(groupName);
        allGroups.add(allGroupsTable);

        actionsColumn.setWidget(0,0, allGroups);
        actionsColumn.setWidget(0,1, allUsers);
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
    public void setPresenter(GroupAdministrationPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public GroupAdministrationView getViewInstance() {
        return this;
    }

    @Override
    public void unableAddUser() {
        allUsers.clear();
    }

    @Override
    public HasWidgets getAddUserContainer(){
        return allUsers;
    }

    @Override
    public void setError(String error){
        this.error.setText(error);
    }

    @Override
    public void selectChatButton(Button button){
        for(int rowIndex = 0; rowIndex < allGroupsTable.getRowCount(); rowIndex++){
            //allGroupsTable.getWidget(rowIndex, 1).setStyleName("");
        }

        button.setStyleName("active");
    }

    @Override
    public void showGroups(List<GroupConversation> groups) {
        allGroupsTable.removeAllRows();
        noGroups.setVisible(false);
        if(groups.isEmpty()){
            noGroups.setVisible(true);
        }
        int rowIndex = 0;
        for (GroupConversation group:
             groups) {

            allGroupsTable.setText(rowIndex, 0,  group.getName());
            Button openGroupConversation = new Button("Chat");
            openGroupConversation.setLayoutData(group.getId());
            openGroupConversation.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    setError("");
                    if (presenter != null) {
                        presenter.goToGroupConversation(group.getId(), openGroupConversation);
                    }
                }
            });

            Button addNewUser = new Button("Nuevo Usuario");
            addNewUser.setLayoutData(group.getId());
            addNewUser.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    setError("");
                    if (presenter != null) {
                        presenter.enableUserAdministration(group.getId());
                    }
                }
            });

            allGroupsTable.setWidget(rowIndex, 1, openGroupConversation);
            allGroupsTable.setWidget(rowIndex, 2, addNewUser);
            rowIndex++;
        }
    }
}