package com.chat.client.Services;

import com.chat.client.Models.GlobalConversation;
import com.chat.client.Models.Message;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

@RemoteServiceRelativePath("GlobalConversationDataService")
public interface GlobalConversationDataService extends RemoteService {
    /**
     * Utility/Convenience class.
     * Use GlobalConversationDataService.App.getInstance() to access static instance of GlobalConversationDataServiceAsync
     */
    public static class App {
        private static final GlobalConversationDataServiceAsync ourInstance = (GlobalConversationDataServiceAsync) GWT.create(GlobalConversationDataService.class);

        public static GlobalConversationDataServiceAsync getInstance() {
            return ourInstance;
        }
    }

    GlobalConversation get(int lastmessagenumber);
    void addMessage(Message message);
}
