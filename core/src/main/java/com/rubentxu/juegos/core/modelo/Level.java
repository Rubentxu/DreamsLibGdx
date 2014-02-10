package com.rubentxu.juegos.core.modelo;


import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.rubentxu.juegos.core.servicios.Assets;

import java.util.Map;

public class Level {

    private String map;
    private String levelName;
    private String description;
    private String music;
    private String background_01;
    private String background_02;
    private String background_03;
    private boolean active=false;
    private int achievements=0;
    private int highScore=0;

    public Level(String map, String levelName, String description, String music, String background_01, String background_02, String background_03,
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

    public String getMap() {
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

    public String getBackground_01() {
        return background_01;
    }

    public String getBackground_02() {
        return background_02;
    }

    public String getBackground_03() {
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

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
}
