package com.rubentxu.juegos.core.managers.game;


import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.rubentxu.juegos.core.modelo.Level;
import com.rubentxu.juegos.core.servicios.Assets;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    private List<Level> levels;
    private Level currentLevel;
    public static final String TREE_BACKGROUND = "imagenes/fondos/arboles.png";
    public static final String LEVEL1_BACKGROUND = "imagenes/fondos/fondo.jpg";
    public static final String CLOUD_BACKGROUND = "imagenes/fondos/nubes.png";
    public static final String MUSIC = "sounds/music/BusyDayAtTheMarket.mp3";

    public LevelManager(){
        levels=new ArrayList<Level>();
        Assets.getInstance().load("maps/EscenarioDePruebas.tmx", TiledMap.class);
        Assets.getInstance().load(CLOUD_BACKGROUND, Texture.class);
        Assets.getInstance().load(TREE_BACKGROUND, Texture.class);
        Assets.getInstance().load(LEVEL1_BACKGROUND, Texture.class);
        Assets.getInstance().load(MUSIC, Music.class);
        Assets.getInstance().finishLoading();


        levels.add( new Level((TiledMap)Assets.getInstance().get("maps/EscenarioDePruebas.tmx"),"Madrid no es Olimpica",
                "El malvado Borcena esta intentando destruir la imagen de Madrid...", Assets.getInstance().<Music>get(MUSIC),(Texture) Assets.getInstance().get(LEVEL1_BACKGROUND),
                (Texture) Assets.getInstance().get(CLOUD_BACKGROUND),(Texture) Assets.getInstance().get(TREE_BACKGROUND)));
        currentLevel=levels.get(0);
    }


    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }
}
