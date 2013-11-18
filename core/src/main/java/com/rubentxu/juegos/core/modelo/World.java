package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.rubentxu.juegos.core.modelo.interfaces.MovingPlatform;
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

    public World() {
        createDreamsWorld();
        createMovingPlatform();
    }
    private void createMovingPlatform(){
        Body body1 = createBox(BodyDef.BodyType.KinematicBody, 4, 0.5f, 1);
        Body body2 = createBox(BodyDef.BodyType.KinematicBody, 2, 1, 1);
        MovingPlatform m1= new MovingPlatform("M1", Box2DPhysicsObject.GRUPOS.PLATAFORMAS_MOVILES, body1,
                68,5,64,9,4);

        MovingPlatform m2= new MovingPlatform("M2", Box2DPhysicsObject.GRUPOS.PLATAFORMAS_MOVILES,body2,
                78,4,80,9 ,4);
        MovingPlatformplatforms= new HashSet<MovingPlatform>();
        //MovingPlatformplatforms.add(m1);
        MovingPlatformplatforms.add(m2);
        platforms = new HashSet<Platform>();


    }
    private void createDreamsWorld() {

        physics = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);
        map = new TmxMapLoader().load("maps/map.tmx");
        parser = new Box2DMapObjectParser();
        System.out.println(getParser().getHierarchy(map));
        getParser().load(getPhysics(), map);
        ruben = new Rubentxu(this.physics, 91, 3.6f, 0.7f, 1.8f);
        assets = new AssetManager();
        getAssets().load("imagenes/texturas/debug.jpg", Texture.class);
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

    private Body createBox(BodyDef.BodyType type, float width, float height, float density){
        BodyDef def= new BodyDef();
        def.type= type;
        Body box = physics.createBody(def);

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width, height);
        FixtureDef fixDef = new FixtureDef();
        fixDef.shape=poly;
        fixDef.friction= 0.3f;
        box.createFixture(fixDef);
        poly.dispose();

        return box;
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
}
