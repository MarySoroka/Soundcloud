package com.soundcloud.wallet;

import java.util.Optional;
import java.util.stream.Stream;
/**
 * Enum, that represents field in wallet table
 */
public enum WalletField {
    ID("wallet_id"),
    AMOUNT("amount");
    private final String field;

    public String getField() {
        return field;
    }

    WalletField(String field) {
        this.field =field;
    }
    static Optional<WalletField> of(String name){
        return Stream.of(WalletField.values()).filter(t->t.field.equalsIgnoreCase(name)).findFirst();
    }
}
