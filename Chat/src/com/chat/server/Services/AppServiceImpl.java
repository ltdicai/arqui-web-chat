package com.chat.server.Services;

import com.chat.client.Models.AppModel;
import com.chat.client.Services.AppServiceAsync;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.chat.client.Services.AppService;

public class AppServiceImpl extends RemoteServiceServlet implements AppService {
    private AppModel context;
    private AppServiceAsync service;

    @Override
    public void AppStar(){
        this.context = new AppModel();

    }

}