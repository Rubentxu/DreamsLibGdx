package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.utils.debug.DebugWindow;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DMapObjectParser;
import com.sun.java.swing.plaf.windows.WindowsFileChooserUI;

public class World {

    private TiledMap map;
    private com.badlogic.gdx.physics.box2d.World physics;
    public static AssetManager assets = null;
    private Rubentxu ruben;
    private Box2DMapObjectParser parser;
    private Window winDebug;

    public World() {
        createDreamsWorld();

    }

    private void createDreamsWorld() {

        physics = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);
        map = new TmxMapLoader().load("maps/map.tmx");
        parser = new Box2DMapObjectParser();
        System.out.println(getParser().getHierarchy(map));
        getParser().load(getPhysics(), map);
        ruben = new Rubentxu(this, 91, 3.6f, 0.7f, 1.8f);
        assets = new AssetManager();
        getAssets().load("imagenes/texturas/debug.jpg", Texture.class);
                getAssets().load("imagenes/test/ball.png", Texture.class);
        //        getAssets().load("imagenes/test/japanischeFlagge.jpg", Texture.class);
        getAssets().finishLoading();
    }

    public void dispose() {
        getMap().dispose();
        getAssets().dispose();
    }

    public TiledMap getMap() {
        return map;
    }

    public com.badlogic.gdx.physics.box2d.World getPhysics() {
        return physics;
    }

    public AssetManager getAssets() {
        return assets;
    }

    public Rubentxu getRuben() {
        return ruben;
    }

    public Box2DMapObjectParser getParser() {
        return parser;
    }

}
