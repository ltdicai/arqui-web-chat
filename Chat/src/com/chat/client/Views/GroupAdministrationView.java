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
    private GroupAdministrationPresenter presenter;
    private Button newGroup;
    private Button showGroupList;
    HorizontalPanel groupButtons;
    VerticalPanel allContainer;
    FlowPanel content;

    public GroupAdministrationView(){

        mainContainer = new FlowPanel();
        allContainer = new VerticalPanel();


        newGroup = new Button("Nuevo grupo");
        newGroup.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                presenter.goToCreateGroup();


            }
        });

        showGroupList = new Button("Lista");

        showGroupList.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                presenter.updateGroups();
            }
        });

        content = new FlowPanel();
        groupButtons = new HorizontalPanel();
        groupButtons.add(showGroupList);
        groupButtons.add(newGroup);
        allContainer.add(groupButtons);
        allContainer.add(content);
    }

    @Override
    public Widget asWidget() {
        return allContainer;
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
    public HasWidgets getContentContainer() {
        return content;
    }

    @Override
    public void selectChatButton(Button button){

    }

    @Override
    public void showGroups(List<GroupConversation> groups) {
        content.clear();
        if(groups.isEmpty()){
            Label noGroups = new Label("No hay grupos");
            content.add(noGroups);
            return;
        }
        FlexTable allGroupsTable = new FlexTable();
        content.add(allGroupsTable);
        int rowIndex = 0;
        for (GroupConversation group:
             groups) {

            allGroupsTable.setText(rowIndex, 0,  group.getName());
            Button openGroupConversation = new Button("Chat");
            openGroupConversation.setLayoutData(group.getId());
            openGroupConversation.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
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