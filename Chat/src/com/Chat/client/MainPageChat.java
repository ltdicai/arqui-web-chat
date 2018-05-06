package com.Chat.client;

import com.Chat.client.Models.AppModel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.*;



public class MainPageChat implements EntryPoint {


    public void onModuleLoad() {
        AppModel context = new AppModel();
        HandlerManager eventBus = new HandlerManager(null);
        AppController app = new AppController(eventBus, context);
        app.goTo(RootPanel.get());
    }
}
