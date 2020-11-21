package com.soundcloud.album;

import com.soundcloud.artist.ArtistField;
import com.soundcloud.bean.Bean;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.soundcloud.builder.BuilderUtil.getBase64;

/**
 * Class, that implements build method of EntityBuilder interface
 */
@Bean(beanName = "albumBuilder")
public class AlbumBuilder implements EntityBuilder<Album> {
    /**
     * Build new Album object from result set, that we get from database
     * @param data set, that is used to build album object
     * @return Album object
     * @throws BuildException if we couldn't find any field by field type or blob file
     */
    @Override
    public Album build(ResultSet data) throws BuildException {
        try {
            InputStream image = data.getBlob(AlbumField.ALBUM_ICON.getField()).getBinaryStream();
            return new Album(
                    data.getLong(AlbumField.ID.getField()),
                    data.getString(AlbumField.NAME.getField()),
                    data.getDate(AlbumField.RELEASE_DATE.getField()).toLocalDate(),
                    data.getInt(AlbumField.LIKES_AMOUNT.getField()),
                    AlbumState.of(data.getString(AlbumField.ALBUM_STATE.getField())),
                    image,
                    AlbumGenre.of(data.getString(AlbumField.ALBUM_GENRE.getField())),
                    getBase64(image),data.getLong(ArtistField.ARTIST_ID.getField()));
        } catch (IOException | SQLException e) {
            throw new BuildException(e.getMessage());
        }
    }


}
