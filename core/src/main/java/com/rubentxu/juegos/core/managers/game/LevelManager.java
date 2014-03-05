package com.rubentxu.juegos.core.managers.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.modelo.Level;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    private final DreamsGame game;
    private List<Level> levels;
    private Level currentLevel;
    public static final String TREE_BACKGROUND = "imagenes/fondos/arboles.png";
    public static final String LEVEL1_BACKGROUND = "imagenes/fondos/fondo.jpg";
    public static final String CLOUD_BACKGROUND = "imagenes/fondos/nubes.png";
    public static final String MUSIC = "sounds/music/BusyDayAtTheMarket.mp3";

    public LevelManager(DreamsGame game) {
        this.game = game;
        levels = new ArrayList<Level>();
        levels = game.getProfileManager().retrieveProfile().getLevels();

        loadLevels();
    }

    public void loadAssetLevel(Level level){
        game.getResourcesManager().load(level.getMap(), TiledMap.class);
        game.getResourcesManager().load(level.getBackground_03(), Texture.class);
        game.getResourcesManager().load(level.getBackground_02(), Texture.class);
        game.getResourcesManager().load(level.getBackground_01(), Texture.class);
        game.getResourcesManager().load(level.getMusic(), Music.class);
        game.getResourcesManager().finishLoading();
    }

    public void loadLevels() {

    }

    public void saveState() {
        game.getProfileManager().persist();
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level currentLevel) {
        loadAssetLevel(currentLevel);
        Gdx.app.log(Constants.LOG,"Se cargo el mapa: "+ currentLevel.getMap());
        this.currentLevel = currentLevel;
    }

    public List<Level> getLevels() {
        return levels;
    }
}
