package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.rubentxu.juegos.core.pantallas.BaseScreen;
import com.rubentxu.juegos.core.servicios.Assets;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DMapObjectParser;

import java.util.HashSet;

public class World {
   
    private TiledMap map;
    private com.badlogic.gdx.physics.box2d.World physics;    
    private Rubentxu ruben;
    private Box2DMapObjectParser parser;
    private HashSet<Platform> platforms=new HashSet<Platform>();
    private HashSet<MovingPlatform> movingPlatforms=new HashSet<MovingPlatform>();
    private HashSet<Water> waterSensors= new HashSet<Water>();
    private HashSet<Enemy> enemies=new HashSet<Enemy>();
    private Texture background;

    public World() {         
        createDreamsWorld();
    }

    private void createDreamsWorld() {
        Assets.getInstance().loadAssetsScreen(BaseScreen.SCREEN.GAME);
        physics = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);
        map =  Assets.getInstance().get(Assets.getInstance().MAP_DEFAULT);
        parser = new Box2DMapObjectParser(this);
        System.out.println(getParser().getHierarchy(map));
        parser.load(getPhysics(), map);
        background=(Texture) Assets.getInstance().get(Assets.getInstance().GAME_BACKGROUND);

    }

    public void dispose() {
        map.dispose();
        Assets.getInstance().dispose();
    }

    public TiledMap getMap() {
        return map;
    }

    public com.badlogic.gdx.physics.box2d.World getPhysics() {
        return physics;
    }

    public Rubentxu getRuben() {
        return ruben;
    }

    public void setRuben(Rubentxu ruben){
        this.ruben=ruben;
    }

    public Box2DMapObjectParser getParser() {
        return parser;
    }

    public HashSet<Platform> getPlatforms() {
        return platforms;
    }

    public HashSet<MovingPlatform> getMovingPlatforms() {
        return movingPlatforms;
    }

    public HashSet<Water> getWaterSensors() {
        return waterSensors;
    }

    public Texture getBackground() {
        return background;
    }

    public void setBackground(Texture background) {
        this.background = background;
    }

    public HashSet<Enemy> getEnemies() {
        return enemies;
    }

}
