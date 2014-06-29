package com.indignado.games.smariano.managers.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.indignado.games.smariano.SMariano;
import com.indignado.games.smariano.SMariano;
import com.indignado.games.smariano.constantes.Constants;
import com.indignado.games.smariano.modelo.Level;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    private final SMariano game;
    private List<Level> levels;
    private Level currentLevel;

    public LevelManager(SMariano game) {
        this.game = game;
        levels = new ArrayList<Level>();
        levels = game.getProfileManager().getProfile().getLevels();

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
