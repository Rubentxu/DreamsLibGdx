package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DMapObjectParser;

public class World {

    private TiledMap map;
    private com.badlogic.gdx.physics.box2d.World physics;
    private AssetManager assets;
    private Rubentxu ruben;
    private Box2DMapObjectParser parser;
    private BitmapFont font;

    public World() {
        createDreamsWorld();

    }

    private void createDreamsWorld() {

        physics = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);
        map = new TmxMapLoader().load("maps/map.tmx");
        parser = new Box2DMapObjectParser();
        System.out.println(getParser().getHierarchy(map));
        getParser().load(getPhysics(), map);
        ruben = new Rubentxu(this, 10, 6, 0.7f, 1.8f);
        assets = new AssetManager();
        font = new BitmapFont();
        //font.setScale(0.01f,0.01f);

//        getAssets().load("imagenes/test/ball.png", Texture.class);
//        getAssets().load("imagenes/test/japanischeFlagge.jpg", Texture.class);

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

    public BitmapFont getFont() {
        return font;
    }
}
