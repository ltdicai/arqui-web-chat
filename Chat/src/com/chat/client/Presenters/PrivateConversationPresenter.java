package com.chat.client.Presenters;

import com.chat.client.Models.Message;
import com.chat.client.Models.PrivateConversation;
import com.chat.client.Models.User;
import com.chat.client.Services.ConversationService;
import com.chat.client.Services.ConversationServiceAsync;
import com.chat.client.Views.PrivateConversationView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;
import java.util.Stack;

public class PrivateConversationPresenter {
    public interface Display{
        HasClickHandlers getSendTextMessageBotton();
        HasClickHandlers getSendImageMessageBotton();
        HasClickHandlers getSendAudioMessageBotton();
        Widget asWidget();
        PrivateConversationView getViewInstance();
        String sendTextMessage();
        void updateMessages(List<Message> listMessage);
        void clearText();
        void setVisibleFileUploadPanel(boolean visibility);
        FormPanel getFileUploadPanel();
        void setPresenter(PrivateConversationPresenter presenter);
    }

    private Timer timer;
    final PrivateConversationPresenter.Display view;
    private final ConversationServiceAsync rpcService = GWT.create(ConversationService.class);
    private PrivateConversation conversation = null;
    private User loggedUser;

    public PrivateConversationPresenter(
            Display view,
            User loggedUser,
            User inviteUser){
        this.view = view;
        view.setPresenter(this);
        /*this.timer = new Timer() {
            @Override
            public void run() {
                updateMessage();
            }
        };
        this.timer.scheduleRepeating(2000);*/
        bindEvents();

        AsyncCallback<PrivateConversation> callback = new AsyncCallback<PrivateConversation>() {
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                // TODO: Do something with errors.
            }

            public void onSuccess(PrivateConversation conv) {
                if (conv == null) {
                    Window.alert("Conv is null!");
                    return;
                }
                Window.alert(conv.getId().toString());
                conversation = conv;
                getView().updateMessages(conv.getMessages());
            }
        };

        //rpcService.getPrivateConversationBetween(loggedUser, inviteUser, callback);

        //rpcService.getPrivateConversation(conversationId, callback);
    }

    public void bindEvents(){
        Window.alert("Binding Events");
        /*view.getSendTextMessageBotton().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                String messageText = getView().sendTextMessage();
                getView().clearText();
                String userid = Cookies.getCookie("UserID");
                User user = new User(userid);
                TextMessage newMessage = new TextMessage(user, messageText);

                AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                    public void onFailure(Throwable caught) {
                        // TODO: Do something with errors.
                    }

                    public void onSuccess(Void v) {
                        updateMessage();
                    }
                };

                GlobalConversationDataServiceAsync globalConversationDataServiceAsync = GWT.create(GlobalConversationDataService.class);

                globalConversationDataServiceAsync.addMessage(newMessage, callback);
            }
        });
        view.getSendImageMessageBotton().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                getView().setVisibleFileUploadPanel(true);
            }

        });
        view.getFileUploadPanel().addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                updateMessage();
            }
        });


        view.getSendAudioMessageBotton().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                String messageText = getView().sendTextMessage();
                getView().clearText();
                String userid = Cookies.getCookie("UserID");
                User user = new User(userid);
                AudioMessage newMessage = new AudioMessage(user, messageText);

                AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                    public void onFailure(Throwable caught) {
                        // TODO: Do something with errors.
                    }

                    public void onSuccess(Void v) {
                        updateMessage();
                    }
                };

                GlobalConversationDataServiceAsync globalConversationDataServiceAsync = GWT.create(GlobalConversationDataService.class);

                globalConversationDataServiceAsync.addMessage(newMessage, callback);
            }
        });*/
    }

    public void go(final HasWidgets container){
        container.clear();
        container.add(view.getViewInstance().asWidget());
    }

    public Display getView(){
        return view;
    }

    public void updateMessage(){
        if (conversation == null) {
            return;
        }
        AsyncCallback<PrivateConversation> callback = new AsyncCallback<PrivateConversation>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(PrivateConversation conv) {
                Window.alert(conv.toString());
                conversation = conv;

                getView().updateMessages(conv.getMessages());
            }
        };

        //rpcService.getPrivateConversation(conversation.getId(), callback);

    }

}

