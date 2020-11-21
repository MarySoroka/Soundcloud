package com.soundcloud.wallet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class WalletFieldTest {
    @Test
    public void getByNameWalletFieldDefaultIsEmpty() {
        String type = "DEFAULT";
        Optional<WalletField> roleType = WalletField.of(type);
        assertFalse(roleType.isPresent());
    }

    @Test
    public void getByNameWalletFieldIdIsRight() {
        String type = "wallet_id";
        Optional<WalletField> roleType = WalletField.of(type);
        assertTrue(roleType.isPresent());
    }

    @Test
    public void getByNameWalletFieldAmountIsRight() {
        String type = "amount";
        Optional<WalletField> roleType = WalletField.of(type);
        assertTrue(roleType.isPresent());
    }
}
