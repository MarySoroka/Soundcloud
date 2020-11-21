package com.soundcloud.album;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class AlbumGenreTest {
    @Test
    public void getByNameAlbumGenreDefaultIsEmpty() {
        String type = "DEFAULT";
        AlbumGenre albumGenre = AlbumGenre.of(type);
        assertEquals(AlbumGenre.ELECTRONIC, albumGenre);
    }
    @Test
    public void getByNameAlbumGenreElectronicIsEmpty() {
        String type = "ELECTRONIC";
        AlbumGenre albumGenre = AlbumGenre.of(type);
        assertEquals(AlbumGenre.ELECTRONIC, albumGenre);
    }
    @Test
    public void getByNameAlbumGenreRockIsEmpty() {
        String type = "rock";
        AlbumGenre albumGenre = AlbumGenre.of(type);
        assertEquals(AlbumGenre.ROCK, albumGenre);
    }
    @Test
    public void getByNameAlbumGenreSoulIsEmpty() {
        String type = "soul";
        AlbumGenre albumGenre = AlbumGenre.of(type);
        assertEquals(AlbumGenre.SOUL, albumGenre);
    }
    @Test
    public void getByNameAlbumGenreFunkIsEmpty() {
        String type = "funk";
        AlbumGenre albumGenre = AlbumGenre.of(type);
        assertEquals(AlbumGenre.FUNK, albumGenre);
    }
    @Test
    public void getByNameAlbumGenreCountryIsEmpty() {
        String type = "country";
        AlbumGenre albumGenre = AlbumGenre.of(type);
        assertEquals(AlbumGenre.COUNTRY, albumGenre);
    }
    @Test
    public void getByNameAlbumGenreReggaeIsEmpty() {
        String type = "reggae";
        AlbumGenre albumGenre = AlbumGenre.of(type);
        assertEquals(AlbumGenre.REGGAE, albumGenre);
    }
    @Test
    public void getByNameAlbumGenreLatinIsEmpty() {
        String type = "latin";
        AlbumGenre albumGenre = AlbumGenre.of(type);
        assertEquals(AlbumGenre.LATIN, albumGenre);
    }
    @Test
    public void getByNameAlbumGenreHipHopIsEmpty() {
        String type = "hip_hop";
        AlbumGenre albumGenre = AlbumGenre.of(type);
        assertEquals(AlbumGenre.HIP_HOP, albumGenre);
    }
}
