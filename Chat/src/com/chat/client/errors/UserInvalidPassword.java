package com.chat.client.errors;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserInvalidPassword extends Exception implements IsSerializable {
        public UserInvalidPassword() {
        }

        private String symbol;

        public UserInvalidPassword(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return this.symbol;
        }


}
