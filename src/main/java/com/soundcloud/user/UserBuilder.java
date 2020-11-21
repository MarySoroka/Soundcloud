package com.soundcloud.user;

import com.soundcloud.bean.Bean;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Class, that implements build method of EntityBuilder interface
 */
@Bean(beanName = "UserBuilder")
public class UserBuilder implements EntityBuilder<User> {
    @Override
    public User build(ResultSet data) throws BuildException {
        try {
            Long id = data.getLong(UserField.ID.getField());
            String password = data.getString(UserField.PASSWORD.getField());
            String login = data.getString(UserField.LOGIN.getField());
            String email = data.getString(UserField.EMAIL.getField());
            Integer userFollows = data.getInt(UserField.USER_FOLLOWS.getField());
            Integer userFollowers = data.getInt(UserField.USER_FOLLOWERS.getField());
            InputStream image = data.getBlob(UserField.USER_ICON.name()).getBinaryStream();
            Long walletId = data.getLong(UserField.USER_WALLET.getField());
            return new User(
                    id,
                    login,
                    password,
                    email,
                    image,
                    userFollows,
                    userFollowers,
                    walletId
            );
        } catch (SQLException e) {
            throw new BuildException(e.getMessage());
        }
    }
}
