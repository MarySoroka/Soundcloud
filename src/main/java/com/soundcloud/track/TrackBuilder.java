package com.soundcloud.track;

import com.soundcloud.bean.Bean;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Class, that implements build method of EntityBuilder interface
 */
@Bean(beanName = "trackBuilder")
public class TrackBuilder implements EntityBuilder<Track> {
    @Override
    public Track build(ResultSet data) throws BuildException {
        try {
            return new Track(
                    data.getLong(TrackField.ID.getField()),
                    data.getString(TrackField.NAME.getField()),
                    data.getInt(TrackField.PLAYS_AMOUNT.getField()),
                    data.getLong(TrackField.ALBUM_ID.getField()),
                    data.getString(TrackField.TRACK_PATH.getField()));
        } catch (SQLException e) {
            throw new BuildException(e.getMessage());
        }
    }
}
