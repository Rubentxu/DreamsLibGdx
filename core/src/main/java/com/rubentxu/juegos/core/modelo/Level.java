package com.rubentxu.juegos.core.modelo;


import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.rubentxu.juegos.core.servicios.Assets;

public class Level {

    private TiledMap map;
    private String levelName;
    private String description;
    private String music;
    private Texture background_01;
    private Texture background_02;
    private Texture background_03;
    private boolean active;
    private int achievements;

    public Level(TiledMap map, String levelName, String description, String music, Texture background_01, Texture background_02, Texture background_03,
                 boolean active,int achievements) {
        this.map = map;
        this.levelName = levelName;
        this.description = description;
        this.music=music;
        this.background_01 = background_01;
        this.background_02 = background_02;
        this.background_03 = background_03;
        this.active=active;
        this.achievements= achievements;
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

    public String getMusic() {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getAchievements() {
        return achievements;
    }

    public void setAchievements(int achievements) {
        this.achievements = achievements;
    }
}
