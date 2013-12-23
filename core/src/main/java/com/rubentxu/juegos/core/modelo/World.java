package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.rubentxu.juegos.core.servicios.Assets;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DMapObjectParser;

import java.util.HashSet;

public class World {


    private TiledMap map;
    private com.badlogic.gdx.physics.box2d.World physics;
    public static Assets assets = null;
    private Rubentxu ruben;
    private Box2DMapObjectParser parser;
    private HashSet<Platform> platforms;
    private HashSet<MovingPlatform> MovingPlatformplatforms;
    private HashSet<Water> waterSensors;
    private HashSet<Enemy> enemies;
    private Sprite background;

    public World(Assets assets) {
        this.assets= assets;
        createDreamsWorld();
        MovingPlatformplatforms = parser.getMovingPlatforms();
        waterSensors= parser.getWaterSensors();
        enemies=parser.getEnemies();
    }

    private void createDreamsWorld() {
        assets.loadAssetsScreen(assets.SCREEN_GAME);
        physics = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);
        map =  assets.get("maps/EscenarioDePruebas.tmx");
        parser = new Box2DMapObjectParser();
        System.out.println(getParser().getHierarchy(map));
        parser.load(getPhysics(), map);
        ruben = new Rubentxu(this.physics, 21, 28, 0.45f, 1);

        background=new Sprite((Texture) assets.get("maps/background.png"));
        background.setSize(40, 20);
        background.setOrigin(0, 0);
    }

    public void dispose() {
        map.dispose();
        assets.dispose();
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

    public HashSet<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(HashSet<Enemy> enemies) {
        this.enemies = enemies;
    }
}
