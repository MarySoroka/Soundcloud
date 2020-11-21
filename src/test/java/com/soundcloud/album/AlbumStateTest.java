package com.soundcloud.album;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class AlbumStateTest {
    @Test
    public void getByNameAlbumStateDefaultIsEmpty() {
        String type = "DEFAULT";
        AlbumState albumState = AlbumState.of(type);
        assertEquals(AlbumState.SINGLE, albumState);
    }
    @Test
    public void getByNameAlbumStateALbumDefaultIsEmpty() {
        String type = "album";
        AlbumState albumState = AlbumState.of(type);
        assertEquals(AlbumState.ALBUM, albumState);
    }
    @Test
    public void getByNameAlbumStateRemixesIsEmpty() {
        String type = "remixes";
        AlbumState albumState = AlbumState.of(type);
        assertEquals(AlbumState.REMIXES, albumState);
    }
    @Test
    public void getByNameAlbumStateMixtapeIsEmpty() {
        String type = "mixtape";
        AlbumState albumState = AlbumState.of(type);
        assertEquals(AlbumState.MIXTAPE, albumState);
    }
    @Test
    public void getByNameAlbumStateSoundtrackIsEmpty() {
        String type = "soundtrack";
        AlbumState albumState = AlbumState.of(type);
        assertEquals(AlbumState.SOUNDTRACK, albumState);
    }
    @Test
    public void getByNameAlbumStateLiveIsEmpty() {
        String type = "live";
        AlbumState albumState = AlbumState.of(type);
        assertEquals(AlbumState.LIVE, albumState);
    }
    @Test
    public void getByNameAlbumStateBootlegIsEmpty() {
        String type = "bootleg";
        AlbumState albumState = AlbumState.of(type);
        assertEquals(AlbumState.BOOTLEG, albumState);
    }
    @Test
    public void getByNameAlbumStatePromoIsEmpty() {
        String type = "promo";
        AlbumState albumState = AlbumState.of(type);
        assertEquals(AlbumState.PROMO, albumState);
    }
}
