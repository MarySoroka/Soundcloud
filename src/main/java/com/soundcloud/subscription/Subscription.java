package com.soundcloud.subscription;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

public class Subscription {
    private Long userId;
    private LocalDate subscriptionDate;

    public SubscriptionStatus getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(SubscriptionStatus subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    private SubscriptionStatus subscriptionStatus;
    public Subscription(Subscription subscription) {
        this.subscriptionDate = subscription.getSubscriptionDate();
        this.subscriptionId = subscription.getSubscriptionId();
        this.userId = subscription.getUserId();
        this.subscriptionStatus = subscription.getSubscriptionStatus();
    }

    public Subscription(Long subscriptionId, LocalDate subscriptionDate, Long userId, SubscriptionStatus subscriptionStatus) {
        this.userId = userId;
        this.subscriptionDate = subscriptionDate;
        this.subscriptionId = subscriptionId;
        this.subscriptionStatus = subscriptionStatus;
    }

    public Subscription(LocalDate date, Long userId) {
        this.subscriptionDate = date;
        this.userId = userId;
        this.subscriptionStatus = SubscriptionStatus.ACTIVE;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    private Long subscriptionId;

    public Subscription(Long userId) {
        this.userId = userId;
        this.subscriptionDate = LocalDate.now(ZoneId.of("America/Montreal"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(subscriptionDate, that.subscriptionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, subscriptionDate);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(LocalDate subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }
}
