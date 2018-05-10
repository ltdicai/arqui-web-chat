package com.chat.client.errors;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserNotFoundException extends Exception implements IsSerializable {
    public UserNotFoundException() {
    }

    private String symbol;

    public UserNotFoundException(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
