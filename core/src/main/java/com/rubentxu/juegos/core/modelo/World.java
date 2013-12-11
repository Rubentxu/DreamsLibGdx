package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DMapObjectParser;

import java.util.HashSet;

public class World {

    private TiledMap map;
    private com.badlogic.gdx.physics.box2d.World physics;
    public static AssetManager assets = null;
    private Rubentxu ruben;
    private Box2DMapObjectParser parser;
    private HashSet<Platform> platforms;
    private HashSet<MovingPlatform> MovingPlatformplatforms;
    private HashSet<Water> waterSensors;
    private Sprite background;

    public World() {
        createDreamsWorld();
        MovingPlatformplatforms = parser.getMovingPlatforms();
        waterSensors= parser.getWaterSensors();
    }

    private void createDreamsWorld() {

        physics = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);
        map = new TmxMapLoader().load("maps/EscenarioDePruebas.tmx");
        parser = new Box2DMapObjectParser();
        System.out.println(getParser().getHierarchy(map));
        parser.load(getPhysics(), map);
        ruben = new Rubentxu(this.physics, 8, 6, 0.45f, 1);
        assets = new AssetManager();
        assets.load("imagenes/texturas/debug.jpg", Texture.class);
        assets.load("maps/background.png", Texture.class);
        assets.finishLoading();
        background=new Sprite((Texture) assets.get("maps/background.png"));
        background.setSize(40, 20);
        background.setOrigin(0, 0);
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

    public HashSet<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(HashSet<Platform> platforms) {
        this.platforms = platforms;
    }

    public HashSet<MovingPlatform> getMovingPlatformplatforms() {
        return MovingPlatformplatforms;
    }

    public void setMovingPlatformplatforms(HashSet<MovingPlatform> movingPlatformplatforms) {
        MovingPlatformplatforms = movingPlatformplatforms;
    }

    public HashSet<Water> getWaterSensors() {
        return waterSensors;
    }

    public void setWaterSensors(HashSet<Water> waterSensors) {
        this.waterSensors = waterSensors;
    }

    public Sprite getBackground() {
        return background;
    }

    public void setBackground(Sprite background) {
        this.background = background;
    }
}
