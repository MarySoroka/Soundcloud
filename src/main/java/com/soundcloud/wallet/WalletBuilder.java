package com.soundcloud.wallet;

import com.soundcloud.bean.Bean;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Class, that implements build method of EntityBuilder interface
 */
@Bean(beanName = "walletBuilder")
public class WalletBuilder implements EntityBuilder<Wallet> {
    @Override
    public Wallet build(ResultSet data) throws BuildException {
        try {
            return new Wallet(data.getLong(WalletField.ID.getField()),data.getBigDecimal(WalletField.AMOUNT.getField()));
        } catch (SQLException e) {
            throw new BuildException(e.getMessage());
        }
    }
}
