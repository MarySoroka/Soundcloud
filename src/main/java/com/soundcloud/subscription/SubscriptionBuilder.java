package com.soundcloud.subscription;

import com.soundcloud.bean.Bean;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Class, that implements build method of EntityBuilder interface
 */
@Bean(beanName = "subscriptionBuilder")
public class SubscriptionBuilder implements EntityBuilder<Subscription> {
    @Override
    public Subscription build(ResultSet data) throws BuildException {
        try {
            return new Subscription(
                    data.getLong(SubscriptionField.ID.getField()),
                    data.getDate(SubscriptionField.SUBSCRIPTION_DATE.getField()).toLocalDate(),
                    data.getLong(SubscriptionField.SUBSCRIPTION_USER_ID.getField()),
                    SubscriptionStatus.of(data.getString(SubscriptionField.SUBSCRIPTION_STATUS.getField())));
        } catch (SQLException e) {
            throw new BuildException(e.getMessage());
        }
    }
}
