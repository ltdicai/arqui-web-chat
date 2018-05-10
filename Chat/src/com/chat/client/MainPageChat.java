package com.chat.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.*;



public class MainPageChat implements EntryPoint {


    public void onModuleLoad() {
        HandlerManager eventBus = new HandlerManager(null);
        AppController app = new AppController(eventBus);
        app.goTo(RootPanel.get());
    }
}
