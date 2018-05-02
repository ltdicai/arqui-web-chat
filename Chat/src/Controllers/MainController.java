package Controllers;

import Models.User;
import Views.client.MainPage;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class MainController implements ClickHandler {
    private MainPage view;
    private User model;

    public MainController(MainPage view, User model){
        this.view = view;
        this.model = model;
        this.view.LoginUser.addClickHandler(this);
    }

    @Override
    public void onClick(ClickEvent event) {

    }
}

