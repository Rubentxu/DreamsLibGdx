package com.rubentxu.juegos.core.modelo;


import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class Level {

    private TiledMap map;
    private String levelName;
    private String description;
    private Music music;
    private Texture background_01;
    private Texture background_02;
    private Texture background_03;

    public Level(TiledMap map, String levelName, String description, Music music, Texture background_01, Texture background_02, Texture background_03) {
        this.map = map;
        this.levelName = levelName;
        this.description = description;
        this.music = music;
        this.background_01 = background_01;
        this.background_02 = background_02;
        this.background_03 = background_03;
    }

    public TiledMap getMap() {
        return map;
    }

    public String getLevelName() {
        return levelName;
    }

    public String getDescription() {
        return description;
    }

    public Music getMusic() {
        return music;
    }

    public Texture getBackground_01() {
        return background_01;
    }

    public Texture getBackground_02() {
        return background_02;
    }

    public Texture getBackground_03() {
        return background_03;
    }
}
