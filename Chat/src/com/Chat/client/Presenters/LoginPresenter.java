package com.Chat.client.Presenters;

import com.Chat.client.Events.LoginEvent;
import com.Chat.client.Models.AppModel;
import com.Chat.client.Models.GlobalConversation;
import com.Chat.client.Models.User;
import com.Chat.client.Services.GlobalConversationDataServiceAsync;
import com.Chat.client.Services.UserDataService;
import com.Chat.client.Services.UserDataServiceAsync;
import com.Chat.client.Views.LoginView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;


public class LoginPresenter {
    public interface Display{
        HasClickHandlers getLoginButton();
        Widget asWidget();
        LoginView getViewInstance();
        String getIdUser();
    }
    //event bus used to register events
    final HandlerManager eventBus;
    final Display view;

    public LoginPresenter(Display view, HandlerManager eventBus){
        this.eventBus = eventBus;
        this.view = view;
    }


    public void bindEvents(){
        view.getLoginButton().addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                String idUser = view.getIdUser();
                User user = new User(idUser);
                System.out.print("Prueba on click");
                UserDataServiceAsync userDataServiceAsync = GWT.create(UserDataService.class);

                AsyncCallback<User> callback = new AsyncCallback<User>() {
                    public void onFailure(Throwable caught) {
                        // TODO: Do something with errors.

                        if (caught.getMessage() == "El usuario no se encuentra"){
                            AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    Cookies.setCookie("UserID", idUser);
                                }
                            };
                            userDataServiceAsync.insert(user, callback);

                        }
                    }

                    public void onSuccess(User user) {
                        Cookies.setCookie("UserID", idUser);
                    }
                };

                userDataServiceAsync.get(idUser, callback);

                eventBus.fireEvent(new LoginEvent());
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

}