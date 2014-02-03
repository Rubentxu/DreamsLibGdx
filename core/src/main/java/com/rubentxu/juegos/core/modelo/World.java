package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.pantallas.BaseScreen;
import com.rubentxu.juegos.core.servicios.Assets;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DMapObjectParser;

import java.util.HashSet;

public class World implements Disposable{
   
    private TiledMap map;
    private com.badlogic.gdx.physics.box2d.World physics;    
    private Hero hero;
    private Box2DMapObjectParser parser;
    private HashSet<Platform> platforms=new HashSet<Platform>();
    private HashSet<MovingPlatform> movingPlatforms=new HashSet<MovingPlatform>();
    private HashSet<Water> waterSensors= new HashSet<Water>();
    private HashSet<Enemy> enemies=new HashSet<Enemy>();
    private HashSet<Item> items=new HashSet<Item>();
    private Array<Body> bodiesFlaggedDestroy=new Array<Body>();
    private Texture cloudBackground;
    private Texture treeBackground;
    private Texture levelBackground;

    public World() {         
        createDreamsWorld();
    }

    private void createDreamsWorld() {
        Assets.getInstance().loadAssetsScreen(BaseScreen.SCREEN.GAME);
        physics = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);
        map =  Assets.getInstance().get(Assets.getInstance().MAP_DEFAULT);
        parser = new Box2DMapObjectParser(this);
        // System.out.println(getParser().getHierarchy(map));
        parser.load(getPhysics(), map);

        levelBackground =((Texture) Assets.getInstance().get(Assets.getInstance().LEVEL1_BACKGROUND));
        cloudBackground =((Texture) Assets.getInstance().get(Assets.getInstance().CLOUD_BACKGROUND));
        treeBackground =((Texture) Assets.getInstance().get(Assets.getInstance().TREE_BACKGROUND));
    }

    public void destroyFlaggedEntities(){
        for(Body b: bodiesFlaggedDestroy) {
            Box2DPhysicsObject data = (Box2DPhysicsObject) b.getUserData();
            if(data!=null && data.isFlaggedForDelete()){
                b.setUserData(null);
                physics.destroyBody(b);
                switch (data.getGrupo()) {
                    case ENEMY:
                        enemies.remove(data);
                        break;
                    case MOVING_PLATFORM:
                        movingPlatforms.remove(data);
                        break;
                    case ITEMS:
                        items.remove(data);
                        break;

                }
                data=null;
                b=null;
            }
        }
    }

    @Override
    public void dispose() {
        map.dispose();
        physics.dispose();
        levelBackground =null;
        hero.dispose();
        bodiesFlaggedDestroy=null;
        for (MovingPlatform m:movingPlatforms){
            m.dispose();
            m=null;
        }
        for (Water w:waterSensors){
            w.dispose();
            w=null;
        }
        for (Enemy e:enemies){
            e.dispose();
            e=null;
        }
        for (Item e:items){
            e.dispose();
            e=null;
        }
    }

    public TiledMap getMap() {
        return map;
    }

    public com.badlogic.gdx.physics.box2d.World getPhysics() {
        return physics;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero){
        this.hero=hero;
    }

    public Box2DMapObjectParser getParser() {
        return parser;
    }

    public void removeParser(){parser=null;}

    public HashSet<Platform> getPlatforms() {
        return platforms;
    }

    public HashSet<MovingPlatform> getMovingPlatforms() {
        return movingPlatforms;
    }

    public HashSet<Water> getWaterSensors() {
        return waterSensors;
    }

    public HashSet<Item> getItems() {
        return items;
    }

    public Texture getLevelBackground() {
        return levelBackground;
    }

    public HashSet<Enemy> getEnemies() {
        return enemies;
    }

    public void addBodiesFlaggedDestroy(Body body) {
        bodiesFlaggedDestroy.add(body);
    }
    public Array<Body> getBodiesFlaggedDestroy(){
        return bodiesFlaggedDestroy;
    }

    public Texture getCloudBackground() {
        return cloudBackground;
    }

    public Texture getTreeBackground() {
        return treeBackground;
    }
}
