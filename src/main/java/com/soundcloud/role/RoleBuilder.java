package com.soundcloud.role;

import com.soundcloud.bean.Bean;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Class, that implements build method of EntityBuilder interface
 */
@Bean(beanName = "RoleBuilder")
public class RoleBuilder implements EntityBuilder<Role> {
    @Override
    public Role build(ResultSet data) throws BuildException {
        try {
            RoleType role = RoleType.of(data.getString(RoleField.NAME.getField()));
            return new Role(role, data.getLong(RoleField.ID.getField()), data.getLong(RoleField.USER_ID.getField()));
        } catch (SQLException e) {
            throw new BuildException(e.getMessage());
        }
    }
}
