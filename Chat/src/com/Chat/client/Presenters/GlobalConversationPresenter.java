package com.Chat.client.Presenters;

import com.Chat.client.Events.SendGlobalTextMessageEvent;
import com.Chat.client.Models.AppModel;
import com.Chat.client.Models.GlobalConversation;
import com.Chat.client.Models.Message;
import com.Chat.client.Services.GlobalConversationDataService;
import com.Chat.client.Services.GlobalConversationDataServiceAsync;
import com.Chat.client.Views.GlobalConversationView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class GlobalConversationPresenter {
    public interface Display{
        HasClickHandlers getSendTextMessageBotton();
        Widget asWidget();
        GlobalConversationView getViewInstance();
        String sendTextMessage();
        void updateMessages(List<Message> listMessage);
    }
    //event bus used to register events
    final HandlerManager eventBus;
    final GlobalConversationPresenter.Display view;
    GlobalConversation globalConversation;

    public GlobalConversationPresenter(GlobalConversationPresenter.Display view, HandlerManager eventBus){
        this.eventBus = eventBus;
        this.view = view;
    }

    public void bindEvents(){
        view.getSendTextMessageBotton().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                String messageText = getView().sendTextMessage();
                eventBus.fireEvent(new SendGlobalTextMessageEvent(messageText));
            }
        });
    }

    public void go(final HasWidgets container){
        bindEvents();
        container.clear();
        container.add(view.getViewInstance().asWidget());
    }

    public Display getView(){
        return view;
    }


    public void updateMessage(){
        AsyncCallback<GlobalConversation> callback = new AsyncCallback<GlobalConversation>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(GlobalConversation GC) {
                globalConversation = GC;
            }
        };

        GlobalConversationDataServiceAsync globalConversationDataServiceAsync = GWT.create(GlobalConversationDataService.class);

        globalConversationDataServiceAsync.get(callback);
        getView().updateMessages(globalConversation.getMessages());
    }

}

