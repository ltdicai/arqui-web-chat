package com.chat.client.errors;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GeneralException extends Exception implements IsSerializable {
    public GeneralException() {
    }

    private String symbol;

    public GeneralException(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
