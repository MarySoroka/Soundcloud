package com.soundcloud.wallet;

import java.math.BigDecimal;
import java.util.Objects;

public class Wallet {
    private Long walletId;
    private BigDecimal amount;

    public Wallet(Long walletId, BigDecimal amount) {
        this.walletId = walletId;
        this.amount = amount;
    }

    public Wallet(Wallet wallet) {
      this.amount = wallet.getAmount();
      this.walletId = wallet.getWalletId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet that = (Wallet) o;
        return Objects.equals(walletId, that.walletId) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(walletId, amount);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }
}
