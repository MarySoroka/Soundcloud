package com.soundcloud.artist;

import com.soundcloud.bean.Bean;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class, that implements build method of EntityBuilder interface
 */
@Bean(beanName = "artistBuilder")
public class ArtistBuilder implements EntityBuilder<Artist> {
    /**
     * Build new artist object from result set, that we get from database
     * @param data set, that is used to build artist object
     * @return artist object
     * @throws BuildException if we couldn't find any field by field type or blob file
     */
    @Override
    public Artist build(ResultSet data) throws BuildException {
        try {
            return new Artist(data.getLong(ArtistField.ARTIST_ID.getField()), data.getLong(ArtistField.USER_ID.getField()));
        } catch (SQLException e) {
            throw new BuildException(e.getMessage());
        }
    }
}
