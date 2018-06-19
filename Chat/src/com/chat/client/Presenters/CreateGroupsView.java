package com.chat.client.Presenters;

import com.chat.client.Models.User;
import com.chat.client.Views.UserAdministrationForGroupsView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class CreateGroupsView implements CreateGroupsPresenter.Display {
    private CreateGroupsPresenter presenter;
    private VerticalPanel mainPanel;
    private Button create;
    private HorizontalPanel name;
    private Label nameLabel;
    private TextBox nameInput;
    private FlexTable allUsers;

    public CreateGroupsView(){
        mainPanel = new VerticalPanel();
        create = new Button("Crear");
        name = new HorizontalPanel();
        nameLabel = new Label("Nombre:");
        nameInput = new TextBox();
        name.add(nameLabel);
        name.add(nameInput);
        name.add(create);
        allUsers = new FlexTable();
        mainPanel.add(name);
        mainPanel.add(allUsers);

        create.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String groupName = nameInput.getValue();
                if (groupName.length() > 0) {
                    presenter.createGroup(groupName);

                }


            }
        });
    }

    @Override
    public void setPresenter(CreateGroupsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showUsers(List<User> users) {
        allUsers.removeAllRows();
        for(int idx = 0; idx < users.size(); ++idx) {
            User user = users.get(idx);
            if (user.equals(presenter.getLoggedUser())) {
                continue;
            }
            allUsers.setText(idx, 0,  user.getUserID());
            CheckBox checkBox = new CheckBox();
            allUsers.setWidget(idx, 1, checkBox);
            checkBox.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    boolean checked = ((CheckBox) event.getSource()).getValue();
                    if (checked) {
                        presenter.addUser(user);
                    } else {
                        presenter.removeUser(user);
                    }
                }
            });
        }
    }

    @Override
    public Widget asWidget() {
        return mainPanel;
    }

    @Override
    public CreateGroupsView getViewInstance() {
        return this;
    }
}
