package com.Chat.client;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class UserNotFound extends Exception implements IsSerializable {
    private String symbol;

    public UserNotFound() {
    }

    public UserNotFound(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
