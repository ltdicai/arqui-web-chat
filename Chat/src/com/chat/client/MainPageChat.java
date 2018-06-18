package com.chat.client;

import com.chat.client.Presenters.LoginPresenter;
import com.chat.client.Presenters.MenuPresenter;
import com.chat.client.Views.LoginView;
import com.chat.client.Views.MenuView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.RootPanel;



public class MainPageChat implements EntryPoint {


    public void onModuleLoad() {
        String userId = Cookies.getCookie("UserID");
        if (userId == null) {
            LoginPresenter app = new LoginPresenter(new LoginView());
            app.go(RootPanel.get());
        } else {
            MenuPresenter app = new MenuPresenter(new MenuView());
            app.go(RootPanel.get());
        }
    }
}
