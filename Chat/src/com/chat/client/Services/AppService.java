package com.chat.client.Services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

@RemoteServiceRelativePath("AppService")
public interface AppService extends RemoteService {
    /**
     * Utility/Convenience class.
     * Use AppService.App.getInstance() to access static instance of AppServiceAsync
     */
    public static class App {
        private static final AppServiceAsync ourInstance = (AppServiceAsync) GWT.create(AppService.class);

        public static AppServiceAsync getInstance() {
            return ourInstance;
        }
    }

    void AppStar();

}
