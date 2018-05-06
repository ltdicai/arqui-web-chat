package com.Chat.server.Services;

import com.Chat.client.Models.AppModel;
import com.Chat.client.Services.AppServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.Chat.client.Services.AppService;

public class AppServiceImpl extends RemoteServiceServlet implements AppService {
    private AppModel context;
    private AppServiceAsync service;

    @Override
    public void AppStar(){
        this.context = new AppModel();
    }

}