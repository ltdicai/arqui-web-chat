package com.chat.client;

import com.chat.client.Presenters.LoginPresenter;
import com.chat.client.Views.LoginView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.*;



public class MainPageChat implements EntryPoint {


    public void onModuleLoad() {
        LoginPresenter app = new LoginPresenter(new LoginView());
        app.go(RootPanel.get());
    }
}
