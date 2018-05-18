package com.chat.client.errors;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserInvalidIDOrPassword extends Exception implements IsSerializable {
        public UserInvalidIDOrPassword() {
        }

        private String symbol;

        public UserInvalidIDOrPassword(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return this.symbol;
        }


}
