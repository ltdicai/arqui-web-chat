package com.chat.client.errors;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SendMessageException extends Exception implements IsSerializable {
    public SendMessageException() {
    }

    private String symbol;

    public SendMessageException(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
