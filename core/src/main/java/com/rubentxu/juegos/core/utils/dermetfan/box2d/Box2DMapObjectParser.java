/**
 * Copyright 2013 Robin Stumm (serverkorken@googlemail.com, http://dermetfan.bplaced.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rubentxu.juegos.core.utils.dermetfan.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import com.badlogic.gdx.physics.box2d.joints.GearJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.PulleyJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Hero;
import com.rubentxu.juegos.core.modelo.Item;
import com.rubentxu.juegos.core.modelo.Item.TYPE;
import com.rubentxu.juegos.core.modelo.MovingPlatform;
import com.rubentxu.juegos.core.modelo.Water;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPO;
import com.rubentxu.juegos.core.utils.dermetfan.math.BayazitDecomposer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.rubentxu.juegos.core.utils.dermetfan.math.GeometryUtils.areVerticesClockwise;
import static com.rubentxu.juegos.core.utils.dermetfan.math.GeometryUtils.isConvex;
import static com.rubentxu.juegos.core.utils.dermetfan.math.GeometryUtils.toFloatArray;
import static com.rubentxu.juegos.core.utils.dermetfan.math.GeometryUtils.toPolygonArray;
import static com.rubentxu.juegos.core.utils.dermetfan.math.GeometryUtils.toVector2Array;

/**
 * An utility class that parses {@link MapObjects} from a {@link Map} and generates Box2D {@link Body Bodies}, {@link Fixture Fixtures} and {@link Joint Joints} from it.<br/>
 * Just create a new {@link Box2DMapObjectParser} in any way you like and call {@link #load(World, MapLayer)} to load all compatible objects (defined by the {@link Aliases}) into your {@link World}.<br/>
 * <br/>
 * If you only want specific Fixtures or Bodies, you can use the {@link #createBody(World, MapObject)} and {@link #createFixture(MapObject)} methods.<br/>
 * <br/>
 * How you define compatible objects in the TiledMap editor:<br/>
 * In your object layer, right-click an object and set its properties to those of the Body / Fixture / both (in case you're creating an {@link Aliases#object object}) you'd like, as defined in the used {@link Aliases} object.<br/>
 * For type, you have to choose {@link Aliases#body}, {@link Aliases#fixture} or {@link Aliases#object}.<br/>
 * To add Fixtures to a Body, add a {@link Aliases#body} property with the same value to each Fixture of a Body.<br/>
 * To create {@link Joint Joints}, add any object to the layer and just put everything needed in its properties. Note that you use the editors unit here which will be converted to Box2D meters automatically using {@link Aliases#unitScale}.
 *
 * For more information visit the <a href="https://bitbucket.org/dermetfan/libgdx-utils/wiki/Box2DMapObjectParser">wiki</a>.
 *
 * @author dermetfan
 */
public class Box2DMapObjectParser {

    private com.rubentxu.juegos.core.modelo.World worldEntity;
    /** @see Aliases */
    private Aliases aliases;

    /** the unit scale to convert from editor units to Box2D meters */
    private float unitScale = 1;

    /** if the unit scale found in the map and it's layers should be ignored */
    private boolean ignoreMapUnitScale = false;

    /** the dimensions of a tile, used to transform positions (ignore / set to 1 if the used map is not a tile map) */
    private float tileWidth = 1, tileHeight = 1;

    /** if concave polygons should be triangulated instead of being decomposed into convex polygons */
    private boolean triangulate;

    /** the parsed {@link Body Bodies} */
    private ObjectMap<String, Body> bodies = new ObjectMap<String, Body>();

    /** the parsed {@link Fixture Fixtures} */
    private ObjectMap<String, Fixture> fixtures = new ObjectMap<String, Fixture>();

    /** the parsed {@link Joint Joints} */
    private ObjectMap<String, Joint> joints = new ObjectMap<String, Joint>();



    /** creates a new {@link Box2DMapObjectParser} with the default {@link Aliases} */
    public Box2DMapObjectParser(com.rubentxu.juegos.core.modelo.World worldEntity) {
        this(new Aliases(),worldEntity);


    }


    /**
     * creates a new {@link Box2DMapObjectParser} using the given {@link Aliases}
     * @param aliases the {@link Aliases} to use
     */
    public Box2DMapObjectParser(Aliases aliases,com.rubentxu.juegos.core.modelo.World worldEntity) {
        this.aliases = aliases;
        this.worldEntity=worldEntity;

    }

    /**
     * creates a new {@link Box2DMapObjectParser} using the given {@link #unitScale unitScale} and sets {@link #ignoreMapUnitScale} to true
     * @param unitScale the {@link #unitScale unitScale} to use
     */
    public Box2DMapObjectParser(float unitScale) {
        this(unitScale, 1, 1);
    }

    /**
     * creates a new {@link Box2DMapObjectParser} using the given {@link #unitScale}, {@link #tileWidth}, {@link #tileHeight} and sets {@link #ignoreMapUnitScale} to true
     * @param unitScale the {@link #unitScale} to use
     * @param tileWidth the {@link #tileWidth} to use
     * @param tileHeight the {@link #tileHeight} to use
     */
    public Box2DMapObjectParser(float unitScale, float tileWidth, float tileHeight) {
        this(new Aliases(), unitScale, tileWidth, tileHeight);
    }

    /**
     * creates a new {@link Box2DMapObjectParser} using the given {@link Aliases} and {@link #unitScale} and sets {@link #ignoreMapUnitScale} to true
     * @param aliases the {@link Aliases} to use
     * @param unitScale the {@link #unitScale} to use
     */
    public Box2DMapObjectParser(Aliases aliases, float unitScale) {
        this(aliases, unitScale, 1, 1);
    }

    /**
     * creates a new {@link Box2DMapObjectParser} with the given parameters and sets {@link #ignoreMapUnitScale} to true
     * @param aliases the {@link Aliases} to use
     * @param unitScale the {@link #unitScale unitScale} to use
     * @param tileWidth the {@link #tileWidth} to use
     * @param tileHeight the {@link #tileHeight} to use
     */
    public Box2DMapObjectParser(Aliases aliases, float unitScale, float tileWidth, float tileHeight) {
        this.aliases = aliases;
        this.unitScale = unitScale;
        ignoreMapUnitScale = true;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    /**
     * creates the given {@link Map Map's} {@link MapObjects} in the given {@link World}
     * @param world the {@link World} to create the {@link MapObjects} of the given {@link Map} in
     * @param map the {@link Map} which {@link MapObjects} to create in the given {@link World}
     * @return the given {@link World} with the parsed {@link MapObjects} of the given {@link Map} created in it
     */
    public World load(World world, Map map) {
        if(!ignoreMapUnitScale)
            unitScale = getProperty(map.getProperties(), aliases.unitScale, unitScale);
        tileWidth = getProperty(map.getProperties(), "tilewidth", (int) tileWidth);
        tileHeight = getProperty(map.getProperties(), "tileheight", (int) tileHeight);

        for(MapLayer mapLayer : map.getLayers())
            load(world, mapLayer);

        return world;
    }

    /**
     * creates the given {@link MapLayer MapLayer's} {@link MapObjects} in the given {@link World}
     * @param world the {@link World} to create the {@link MapObjects} of the given {@link MapLayer} in
     * @param layer the {@link MapLayer} which {@link MapObjects} to create in the given {@link World}
     * @return the given {@link World} with the parsed {@link MapObjects} of the given {@link MapLayer} created in it
     */
    public World load(World world, MapLayer layer) {
        System.out.println("UNIT SCALE...........:"+unitScale);
        for(MapObject object : layer.getObjects()) {
            if(!ignoreMapUnitScale)
                unitScale = getProperty(layer.getProperties(), aliases.unitScale, unitScale);
            if(object.getProperties().get("type", "", String.class).equals(aliases.modelObject)) {
                createModelObject(world,object);

            }
        }

        for(MapObject object : layer.getObjects()) {
            if(!ignoreMapUnitScale)
                unitScale = getProperty(layer.getProperties(), aliases.unitScale, unitScale);
            if(object.getProperties().get("type", "", String.class).equals(aliases.object)) {
                createBody(world, object);
                createFixtures(object);
            }
        }

        for(MapObject object : layer.getObjects()) {
            if(!ignoreMapUnitScale)
                unitScale = getProperty(layer.getProperties(), aliases.unitScale, unitScale);
            if(object.getProperties().get("type", "", String.class).equals(aliases.body))
                createBody(world, object);
        }

        for(MapObject object : layer.getObjects()) {
            if(!ignoreMapUnitScale)
                unitScale = getProperty(layer.getProperties(), aliases.unitScale, unitScale);
            if(object.getProperties().get("type", "", String.class).equals(aliases.fixture))
                createFixtures(object);
        }

        for(MapObject object : layer.getObjects()) {
            if(!ignoreMapUnitScale)
                unitScale = getProperty(layer.getProperties(), aliases.unitScale, unitScale);
            if(object.getProperties().get("type", "", String.class).equals(aliases.joint))
                createJoint(object);
        }

        return world;
    }

    private void createModelObject(World world, MapObject object) {
        if(object.getProperties().get(aliases.typeModelObject).equals(aliases.hero))
            createHero(world, object);
        if(object.getProperties().get(aliases.typeModelObject).equals(aliases.movingPlatform))
            createMovingPlatform(world,object);
        if(object.getProperties().get(aliases.typeModelObject).equals(aliases.water))
            createWater(world, object);
        if(object.getProperties().get(aliases.typeModelObject).equals(aliases.enemy))
            createEnemy(world, object);
        if(object.getProperties().get(aliases.typeModelObject).equals(aliases.item))
            createItem(world, object);


    }

    private void createHero(World world, MapObject object) {
        Rectangle rectangle = new Rectangle(((RectangleMapObject) object).getRectangle());
        rectangle.x *= unitScale;
        rectangle.y *= unitScale;
        rectangle.width *= unitScale;
        rectangle.height *= unitScale;
        worldEntity.setHero(new Hero(world, rectangle.x, rectangle.y, 0.45f, 1));
    }

    private void createWater(World world, MapObject object) {
        MapProperties properties = object.getProperties();
        BodyDef def= new BodyDef();
        def.type= BodyType.StaticBody;
        def.position.set(getProperty(properties, "x", def.position.x) * unitScale, getProperty(properties, "y", def.position.y) * unitScale);
        Body box = world.createBody(def);


        if(object instanceof RectangleMapObject && !properties.get(aliases.type).equals(aliases.typeModelObject)) {
            PolygonShape shape = new PolygonShape();
            Rectangle rectangle = new Rectangle(((RectangleMapObject) object).getRectangle());
            System.out.println("Rectangle Water "+rectangle);
            rectangle.x *= unitScale;
            rectangle.y *= unitScale;
            rectangle.width *= unitScale;
            rectangle.height *= unitScale;
            System.out.println("Rectangle Water2 "+rectangle);
            shape.setAsBox(rectangle.width / 2, rectangle.height / 2, new Vector2(rectangle.x - box.getPosition().x
                    + rectangle.width / 2, rectangle.y - box.getPosition().y + rectangle.height / 2), box.getAngle());

            FixtureDef fixDef = new FixtureDef();
            fixDef.shape=shape;
            fixDef.friction= 1f;
            fixDef.isSensor=true;
            fixDef.density=getProperty(properties, aliases.density, 2);
            fixDef.filter.categoryBits= GRUPO.FLUID.getCategory();
            fixDef.filter.maskBits=Box2DPhysicsObject.MASK_FLUID;
            Fixture fixBox=box.createFixture(fixDef);
            shape.dispose();

            String name = object.getName();
            if(bodies.containsKey(name)) {
                int duplicate = 1;
                while(bodies.containsKey(name + duplicate))
                    duplicate++;
                name += duplicate;
            }

            Water w1= new Water(name,box);
            worldEntity.getWaterSensors().add(w1);
            box.setUserData(w1);
            fixBox.setUserData(w1);
            bodies.put(name, box);

        } else {
            throw new IllegalArgumentException("type of " + object + " is  \"" + properties.get(aliases.type) + "\" instead of \""  + aliases.typeModelObject + "\"");
        }



    }

    private void createMovingPlatform(World world, MapObject object){
        MapProperties properties = object.getProperties();
        BodyDef def= new BodyDef();
        def.type= BodyType.KinematicBody;
        def.position.set(getProperty(properties, "x", def.position.x) * unitScale, getProperty(properties, "y", def.position.y) * unitScale);
        Body box = world.createBody(def);


        if(object instanceof RectangleMapObject && !properties.get(aliases.type).equals(aliases.typeModelObject)) {
            PolygonShape shape = new PolygonShape();
            Rectangle rectangle = new Rectangle(((RectangleMapObject) object).getRectangle());
            rectangle.x *= unitScale;
            rectangle.y *= unitScale;
            rectangle.width *= unitScale;
            rectangle.height *= unitScale;
            shape.setAsBox(rectangle.width / 2, rectangle.height / 2, new Vector2(rectangle.x - box.getPosition().x
                    + rectangle.width / 2, rectangle.y - box.getPosition().y + rectangle.height / 2), box.getAngle());

            FixtureDef fixDef = new FixtureDef();
            fixDef.shape=shape;
            fixDef.filter.categoryBits= GRUPO.MOVING_PLATFORM.getCategory();
            fixDef.filter.maskBits=Box2DPhysicsObject.MASK_MOVING_PLATFORM;
            Fixture fixBox=box.createFixture(fixDef);
            shape.dispose();
            box.setBullet(true);

            String name = object.getName();
            if(bodies.containsKey(name)) {
                int duplicate = 1;
                while(bodies.containsKey(name + duplicate))
                    duplicate++;
                name += duplicate;
            }

            MovingPlatform m1= new MovingPlatform(name, GRUPO.MOVING_PLATFORM,box,Float.parseFloat(properties.get(aliases.movingPlatformDistX, String.class))
                    ,Float.parseFloat(properties.get(aliases.movingPlatformDistY,String.class)),Float.parseFloat( properties.get(aliases.movingPlatformSpeed,String.class)));
            worldEntity.getMovingPlatforms().add(m1);
            box.setUserData(m1);
            fixBox.setUserData(m1);
            bodies.put(name, box);

        } else {
            throw new IllegalArgumentException("type of " + object + " is  \"" + properties.get(aliases.type) + "\" instead of \""  + aliases.typeModelObject + "\"");
        }

    }

    private void createEnemy (World world, MapObject object) {
        MapProperties properties = object.getProperties();
        BodyDef def= new BodyDef();
        def.type= BodyDef.BodyType.DynamicBody;
        def.position.set(getProperty(properties, "x", def.position.x) * unitScale, getProperty(properties, "y", def.position.y) * unitScale);
        Body box = world.createBody(def);
        box.setFixedRotation(true);

        if(object instanceof RectangleMapObject && !properties.get(aliases.type).equals(aliases.typeModelObject)) {
            PolygonShape shape = new PolygonShape();
            Rectangle rectangle = new Rectangle(((RectangleMapObject) object).getRectangle());
            rectangle.x *= unitScale;
            rectangle.y *= unitScale;
            rectangle.width *= unitScale;
            rectangle.height *= unitScale;
            shape.setAsBox(rectangle.width / 2, rectangle.height / 2, new Vector2(rectangle.x - box.getPosition().x
                    + rectangle.width / 2, rectangle.y - box.getPosition().y + rectangle.height / 2), box.getAngle());

            FixtureDef fixDef = new FixtureDef();
            fixDef.shape=shape;
            fixDef.filter.categoryBits= GRUPO.ENEMY.getCategory();
            fixDef.filter.maskBits=Box2DPhysicsObject.MASK_ENEMY;
            Fixture enemyPhysicsFixture = box.createFixture(fixDef);
            shape.dispose();
            CircleShape circle = new CircleShape();
            circle.setRadius(rectangle.width/2);
            circle.setPosition(new Vector2(rectangle.width/2, rectangle.height/5));
            Fixture enemySensorFixture = box.createFixture(circle, 0);
            enemySensorFixture.setSensor(true);
            circle.dispose();

            String name = object.getName();
            if(bodies.containsKey(name)) {
                int duplicate = 1;
                while(bodies.containsKey(name + duplicate))
                    duplicate++;
                name += duplicate;
            }
            List<Vector2> points= new ArrayList<Vector2>();
            points.add(box.getPosition().cpy());
            points.add(new Vector2(Float.parseFloat(properties.get(aliases.pointX, String.class)),
                    Float.parseFloat(properties.get(aliases.pointY, String.class))));


            Enemy enemy= new Enemy(name,box,points);
            enemy.setEnemyPhysicsFixture(enemyPhysicsFixture);
            enemy.setEnemySensorFixture(enemySensorFixture);
            worldEntity.getEnemies().add(enemy);
            box.setUserData(enemy);
            enemyPhysicsFixture.setUserData(enemy);
            enemySensorFixture.setUserData(enemy);
            bodies.put(name, box);

        } else {
            throw new IllegalArgumentException("type of " + object + " is  \"" + properties.get(aliases.type) + "\" instead of \""  + aliases.typeModelObject + "\"");
        }

    }

    private void createItem(World world, MapObject object){
        MapProperties properties = object.getProperties();
        BodyDef def= new BodyDef();
        def.type= BodyType.StaticBody;
        def.position.set(getProperty(properties, "x", def.position.x) * unitScale, getProperty(properties, "y", def.position.y) * unitScale);
        Body box = world.createBody(def);

        if(object instanceof RectangleMapObject && !properties.get(aliases.type).equals(aliases.typeModelObject)) {
            PolygonShape shape = new PolygonShape();
            Rectangle rectangle = new Rectangle(((RectangleMapObject) object).getRectangle());
            rectangle.x *= unitScale;
            rectangle.y *= unitScale;
            rectangle.width *= unitScale;
            rectangle.height *= unitScale;
            shape.setAsBox( rectangle.width / 2, rectangle.height, new Vector2(rectangle.x - box.getPosition().x
                    + rectangle.width / 2, rectangle.y - box.getPosition().y + rectangle.height / 2), box.getAngle());

            FixtureDef fixDef = new FixtureDef();
            fixDef.shape=shape;
            fixDef.filter.categoryBits= GRUPO.ITEMS.getCategory();
            fixDef.filter.maskBits=Box2DPhysicsObject.MASK_ITEMS;
            Fixture fixBox=box.createFixture(fixDef);
            fixBox.setSensor(true);
            shape.dispose();
            box.setBullet(true);

            String name = object.getName();
            if(bodies.containsKey(name)) {
                int duplicate = 1;
                while(bodies.containsKey(name + duplicate))
                    duplicate++;
                name += duplicate;
            }
            Item item=null;
            if(object.getProperties().get(aliases.typeItem).equals(aliases.coin)){
                item=new Item(name,GRUPO.ITEMS, TYPE.COIN,box,1);

            }else if(object.getProperties().get(aliases.typeItem).equals(aliases.powerup)) {
                item=new Item(name,GRUPO.ITEMS, TYPE.POWERUP,box,1);
            }else if(object.getProperties().get(aliases.typeItem).equals(aliases.key)) {
                item=new Item(name,GRUPO.ITEMS, TYPE.KEY,box,1);
            }

            worldEntity.getItems().add(item);
            box.setUserData(item);
            fixBox.setUserData(item);
            bodies.put(name, box);


        } else {
            throw new IllegalArgumentException("type of " + object + " is  \"" + properties.get(aliases.type) + "\" instead of \""  + aliases.typeModelObject + "\"");
        }

    }


    /**
     * creates a {@link Body} in the given {@link World} from the given {@link MapObject}
     * @param world the {@link World} to create the {@link Body} in
     * @param mapObject the {@link MapObject} to parse the {@link Body} from
     * @return the {@link Body} created in the given {@link World} from the given {@link MapObject}
     */
    public Body createBody(World world, MapObject mapObject) {
        MapProperties properties = mapObject.getProperties();

        String type = properties.get("type", String.class);
        if(!type.equals(aliases.body) && !type.equals(aliases.object))
            throw new IllegalArgumentException("type of " + mapObject + " is  \"" + type + "\" instead of \"" + aliases.body + "\" or \"" + aliases.object + "\"");

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = properties.get(aliases.bodyType, String.class) != null ? properties.get(aliases.bodyType, String.class).equals(aliases.dynamicBody) ? BodyType.DynamicBody : properties.get(aliases.bodyType, String.class).equals(aliases.kinematicBody) ? BodyType.KinematicBody : properties.get(aliases.bodyType, String.class).equals(aliases.staticBody) ? BodyType.StaticBody : bodyDef.type : bodyDef.type;
        bodyDef.active = getProperty(properties, aliases.active, bodyDef.active);
        bodyDef.allowSleep = getProperty(properties, aliases.allowSleep, bodyDef.allowSleep);
        bodyDef.angle = getProperty(properties, aliases.angle, bodyDef.angle);
        bodyDef.angularDamping = getProperty(properties, aliases.angularDamping, bodyDef.angularDamping);
        bodyDef.angularVelocity = getProperty(properties, aliases.angularVelocity, bodyDef.angularVelocity);
        bodyDef.awake = getProperty(properties, aliases.awake, bodyDef.awake);
        bodyDef.bullet = getProperty(properties, aliases.bullet, bodyDef.bullet);
        bodyDef.fixedRotation = getProperty(properties, aliases.fixedRotation, bodyDef.fixedRotation);
        bodyDef.gravityScale = getProperty(properties, aliases.gravityunitScale, bodyDef.gravityScale);
        bodyDef.linearDamping = getProperty(properties, aliases.linearDamping, bodyDef.linearDamping);
        bodyDef.linearVelocity.set(getProperty(properties, aliases.linearVelocityX, bodyDef.linearVelocity.x), getProperty(properties, aliases.linearVelocityY, bodyDef.linearVelocity.y));
        bodyDef.position.set(getProperty(properties, "x", bodyDef.position.x) * unitScale, getProperty(properties, "y", bodyDef.position.y) * unitScale);

        Body body = world.createBody(bodyDef);

        String name = mapObject.getName();
        if(bodies.containsKey(name)) {
            int duplicate = 1;
            while(bodies.containsKey(name + duplicate))
                duplicate++;
            name += duplicate;
        }
        Box2DPhysicsObject box2DPhysicsObject= new Box2DPhysicsObject(name, GRUPO.STATIC,body);
        body.setUserData(box2DPhysicsObject);
        bodies.put(name, body);

        return body;
    }

    /**
     * creates a {@link Fixture} from a {@link MapObject}
     * @param mapObject the {@link MapObject} to parse
     * @return the parsed {@link Fixture}
     */
    public Fixture createFixture(MapObject mapObject) {
        MapProperties properties = mapObject.getProperties();

        String type = properties.get("type", String.class);

        Body body = bodies.get(type.equals(aliases.object) ? mapObject.getName() : properties.get(aliases.body, String.class));

        if(!type.equals(aliases.fixture) && !type.equals(aliases.object))
            throw new IllegalArgumentException("type of " + mapObject + " is  \"" + type + "\" instead of \"" + aliases.fixture + "\" or \"" + aliases.object + "\"");

        FixtureDef fixtureDef = new FixtureDef();
        Shape shape = null;

        if(mapObject instanceof RectangleMapObject) {
            shape = new PolygonShape();
            Rectangle rectangle = new Rectangle(((RectangleMapObject) mapObject).getRectangle());
            rectangle.x *= unitScale;
            rectangle.y *= unitScale;
            rectangle.width *= unitScale;
            rectangle.height *= unitScale;
            ((PolygonShape) shape).setAsBox(rectangle.width / 2, rectangle.height / 2, new Vector2(rectangle.x - body.getPosition().x + rectangle.width / 2, rectangle.y - body.getPosition().y + rectangle.height / 2), body.getAngle());
        } else if(mapObject instanceof PolygonMapObject) {
            shape = new PolygonShape();
            Polygon polygon = ((PolygonMapObject) mapObject).getPolygon();
            polygon.setPosition(polygon.getX() * unitScale - body.getPosition().x, polygon.getY() * unitScale - body.getPosition().y);
            polygon.setScale(unitScale, unitScale);
            ((PolygonShape) shape).set(polygon.getTransformedVertices());
        } else if(mapObject instanceof PolylineMapObject) {
            shape = new ChainShape();
            Polyline polyline = ((PolylineMapObject) mapObject).getPolyline();
            polyline.setPosition(polyline.getX() * unitScale - body.getPosition().x, polyline.getY() * unitScale - body.getPosition().y);
            polyline.setScale(unitScale, unitScale);
            float[] vertices= polyline.getTransformedVertices();
            Vector2[] vectores= new Vector2[vertices.length / 2];
            for (int i = 0,j=0; i < vertices.length; i += 2,j++) {
                vectores[j].x = vertices[i];
                vectores[j].y  = vertices[i + 1];
            }
            ((ChainShape) shape).createChain(vectores);
        } else if(mapObject instanceof CircleMapObject) {
            shape = new CircleShape();
            Circle circle = ((CircleMapObject) mapObject).getCircle();
            circle.setPosition(circle.x * unitScale - body.getPosition().x, circle.y * unitScale - body.getPosition().y);
            circle.radius *= unitScale;
            ((CircleShape) shape).setPosition(new Vector2(circle.x, circle.y));
            ((CircleShape) shape).setRadius(circle.radius);
        } else if(mapObject instanceof EllipseMapObject) {
            Ellipse ellipse = ((EllipseMapObject) mapObject).getEllipse();

             /*
		b2ChainShape* chain = (b2ChainShape*)addr;
		b2Vec2* verticesOut = new b2Vec2[numVertices];
		for( int i = 0; i < numVertices; i++ )
			verticesOut[i] = b2Vec2(verts[i<<1], verts[(i<<1)+1]);
		chain->CreateChain( verticesOut, numVertices );
		delete verticesOut;
	*/

            if(ellipse.width == ellipse.height) {
                CircleMapObject circleMapObject = new CircleMapObject(ellipse.x, ellipse.y, ellipse.width / 2);
                circleMapObject.setName(mapObject.getName());
                circleMapObject.getProperties().putAll(mapObject.getProperties());
                circleMapObject.setColor(mapObject.getColor());
                circleMapObject.setVisible(mapObject.isVisible());
                circleMapObject.setOpacity(mapObject.getOpacity());
                return createFixture(circleMapObject);
            }

            IllegalArgumentException exception = new IllegalArgumentException("Cannot parse " + mapObject.getName() + " because  that are not circles are not supported");
            Gdx.app.error(getClass().getName(), exception.getMessage(), exception);
            throw exception;
        } else if(mapObject instanceof TextureMapObject) {
            IllegalArgumentException exception = new IllegalArgumentException("Cannot parse " + mapObject.getName() + " because s are not supported");
            Gdx.app.error(getClass().getName(), exception.getMessage(), exception);
            throw exception;
        } else
            assert false : mapObject + " is a not known subclass of " + MapObject.class.getName();


        fixtureDef.shape = shape;
        fixtureDef.density = getProperty(properties, aliases.density, fixtureDef.density);
        fixtureDef.filter.categoryBits = getProperty(properties, aliases.categoryBits, GRUPO.STATIC.getCategory());
        fixtureDef.filter.groupIndex = getProperty(properties, aliases.groupIndex, fixtureDef.filter.groupIndex);
        fixtureDef.filter.maskBits =getProperty(properties, aliases.maskBits,Box2DPhysicsObject.MASK_STATIC);
        fixtureDef.friction = getProperty(properties, aliases.friciton, fixtureDef.friction);
        fixtureDef.isSensor = getProperty(properties, aliases.isSensor, fixtureDef.isSensor);
        fixtureDef.restitution = getProperty(properties, aliases.restitution, fixtureDef.restitution);

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(body.getUserData());
        shape.dispose();

        String name = mapObject.getName();
        if(fixtures.containsKey(name)) {
            int duplicate = 1;
            while(fixtures.containsKey(name + duplicate))
                duplicate++;
            name += duplicate;
        }

        fixtures.put(name, fixture);

        return fixture;
    }

    /**
     * creates {@link Fixture Fixtures} from a {@link MapObject}
     * @param mapObject the {@link MapObject} to parse
     * @return an array of parsed {@link Fixture Fixtures}
     */
    public Fixture[] createFixtures(MapObject mapObject) {
        Polygon polygon;

        if(!(mapObject instanceof PolygonMapObject) || isConvex(polygon = ((PolygonMapObject) mapObject).getPolygon()))
            return new Fixture[] {createFixture(mapObject)};

        Polygon[] convexPolygons;
        if(triangulate) {
            if(areVerticesClockwise(polygon)) { // ensure the vertices are in counterclockwise order (not really necessary according to EarClippingTriangulator's javadoc, but sometimes better)
                Array<Vector2> vertices = new Array<Vector2>(toVector2Array(polygon.getVertices()));
                Vector2 first = vertices.removeIndex(0);
                vertices.reverse();
                vertices.insert(0, first);
                polygon.setVertices(toFloatArray(vertices.items));
            }
            convexPolygons = toPolygonArray(toVector2Array(new EarClippingTriangulator().computeTriangles(polygon.getTransformedVertices()).toArray()), 3);
        } else {
            Array<Array<Vector2>> convexPolys = BayazitDecomposer.convexPartition(new Array<Vector2>(toVector2Array(polygon.getTransformedVertices())));
            convexPolygons = new Polygon[convexPolys.size];
            for(int i = 0; i < convexPolygons.length; i++)
                convexPolygons[i] = new Polygon(toFloatArray((Vector2[]) convexPolys.get(i).toArray(Vector2.class)));
        }

        // create the fixtures using the convex polygons
        Fixture[] fixtures = new Fixture[convexPolygons.length];
        for(int i = 0; i < fixtures.length; i++) {
            PolygonMapObject convexObject = new PolygonMapObject(convexPolygons[i]);
            convexObject.setColor(mapObject.getColor());
            convexObject.setName(mapObject.getName());
            convexObject.setOpacity(mapObject.getOpacity());
            convexObject.setVisible(mapObject.isVisible());
            convexObject.getProperties().putAll(mapObject.getProperties());
            fixtures[i] = createFixture(convexObject);
        }

        return fixtures;
    }

    /**
     * creates a {@link Joint} from a {@link MapObject}
     * @param mapObject the {@link Joint} to parse
     * @return the parsed {@link Joint}
     */
    public Joint createJoint(MapObject mapObject) {
        MapProperties properties = mapObject.getProperties();

        JointDef jointDef = null;

        String type = properties.get("type", String.class);
        if(!type.equals(aliases.joint))
            throw new IllegalArgumentException("type of " + mapObject + " is  \"" + type + "\" instead of \"" + aliases.joint + "\"");

        String jointType = properties.get(aliases.jointType, String.class);

        // get all possible values
        if(jointType.equals(aliases.distanceJoint)) {
            DistanceJointDef distanceJointDef = new DistanceJointDef();
            distanceJointDef.dampingRatio = getProperty(properties, aliases.dampingRatio, distanceJointDef.dampingRatio);
            distanceJointDef.frequencyHz = getProperty(properties, aliases.frequencyHz, distanceJointDef.frequencyHz);
            distanceJointDef.length = getProperty(properties, aliases.length, distanceJointDef.length) * (tileWidth + tileHeight) / 2 * unitScale;
            distanceJointDef.localAnchorA.set(getProperty(properties, aliases.localAnchorAX, distanceJointDef.localAnchorA.x) * tileWidth * unitScale, getProperty(properties, aliases.localAnchorAY, distanceJointDef.localAnchorA.y) * tileHeight * unitScale);
            distanceJointDef.localAnchorB.set(getProperty(properties, aliases.localAnchorBX, distanceJointDef.localAnchorB.x) * tileWidth * unitScale, getProperty(properties, aliases.localAnchorBY, distanceJointDef.localAnchorB.y) * tileHeight * unitScale);

            jointDef = distanceJointDef;
        } else if(jointType.equals(aliases.frictionJoint)) {
            FrictionJointDef frictionJointDef = new FrictionJointDef();
            frictionJointDef.localAnchorA.set(getProperty(properties, aliases.localAnchorAX, frictionJointDef.localAnchorA.x) * tileWidth * unitScale, getProperty(properties, aliases.localAnchorAY, frictionJointDef.localAnchorA.y) * tileHeight * unitScale);
            frictionJointDef.localAnchorB.set(getProperty(properties, aliases.localAnchorBX, frictionJointDef.localAnchorB.x) * tileWidth * unitScale, getProperty(properties, aliases.localAnchorBY, frictionJointDef.localAnchorB.y) * tileHeight * unitScale);
            frictionJointDef.maxForce = getProperty(properties, aliases.maxForce, frictionJointDef.maxForce);
            frictionJointDef.maxTorque = getProperty(properties, aliases.maxTorque, frictionJointDef.maxTorque);

            jointDef = frictionJointDef;
        } else if(jointType.equals(aliases.gearJoint)) {
            GearJointDef gearJointDef = new GearJointDef();
            gearJointDef.joint1 = joints.get(properties.get(aliases.joint1, String.class));
            gearJointDef.joint2 = joints.get(properties.get(aliases.joint2, String.class));
            gearJointDef.ratio = getProperty(properties, aliases.ratio, gearJointDef.ratio);

            jointDef = gearJointDef;
        } else if(jointType.equals(aliases.mouseJoint)) {
            MouseJointDef mouseJointDef = new MouseJointDef();
            mouseJointDef.dampingRatio = getProperty(properties, aliases.dampingRatio, mouseJointDef.dampingRatio);
            mouseJointDef.frequencyHz = getProperty(properties, aliases.frequencyHz, mouseJointDef.frequencyHz);
            mouseJointDef.maxForce = getProperty(properties, aliases.maxForce, mouseJointDef.maxForce);
            mouseJointDef.target.set(getProperty(properties, aliases.targetX, mouseJointDef.target.x) * tileWidth * unitScale, getProperty(properties, aliases.targetY, mouseJointDef.target.y) * tileHeight * unitScale);

            jointDef = mouseJointDef;
        } else if(jointType.equals(aliases.prismaticJoint)) {
            PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
            prismaticJointDef.enableLimit = getProperty(properties, aliases.enableLimit, prismaticJointDef.enableLimit);
            prismaticJointDef.enableMotor = getProperty(properties, aliases.enableMotor, prismaticJointDef.enableMotor);
            prismaticJointDef.localAnchorA.set(getProperty(properties, aliases.localAnchorAX, prismaticJointDef.localAnchorA.x) * tileWidth * unitScale, getProperty(properties, aliases.localAnchorAY, prismaticJointDef.localAnchorA.y) * tileHeight * unitScale);
            prismaticJointDef.localAnchorB.set(getProperty(properties, aliases.localAnchorBX, prismaticJointDef.localAnchorB.x) * tileWidth * unitScale, getProperty(properties, aliases.localAnchorBY, prismaticJointDef.localAnchorB.y) * tileHeight * unitScale);
            prismaticJointDef.localAxisA.set(getProperty(properties, aliases.localAxisAX, prismaticJointDef.localAxisA.x), getProperty(properties, aliases.localAxisAY, prismaticJointDef.localAxisA.y));
            prismaticJointDef.lowerTranslation = getProperty(properties, aliases.lowerTranslation, prismaticJointDef.lowerTranslation) * (tileWidth + tileHeight) / 2 * unitScale;
            prismaticJointDef.maxMotorForce = getProperty(properties, aliases.maxMotorForce, prismaticJointDef.maxMotorForce);
            prismaticJointDef.motorSpeed = getProperty(properties, aliases.motorSpeed, prismaticJointDef.motorSpeed);
            prismaticJointDef.referenceAngle = getProperty(properties, aliases.referenceAngle, prismaticJointDef.referenceAngle);
            prismaticJointDef.upperTranslation = getProperty(properties, aliases.upperTranslation, prismaticJointDef.upperTranslation) * (tileWidth + tileHeight) / 2 * unitScale;

            jointDef = prismaticJointDef;
        } else if(jointType.equals(aliases.pulleyJoint)) {
            PulleyJointDef pulleyJointDef = new PulleyJointDef();
            pulleyJointDef.groundAnchorA.set(getProperty(properties, aliases.groundAnchorAX, pulleyJointDef.groundAnchorA.x) * tileWidth * unitScale, getProperty(properties, aliases.groundAnchorAY, pulleyJointDef.groundAnchorA.y) * tileHeight * unitScale);
            pulleyJointDef.groundAnchorB.set(getProperty(properties, aliases.groundAnchorBX, pulleyJointDef.groundAnchorB.x) * tileWidth * unitScale, getProperty(properties, aliases.groundAnchorBY, pulleyJointDef.groundAnchorB.y) * tileHeight * unitScale);
            pulleyJointDef.lengthA = getProperty(properties, aliases.lengthA, pulleyJointDef.lengthA) * (tileWidth + tileHeight) / 2 * unitScale;
            pulleyJointDef.lengthB = getProperty(properties, aliases.lengthB, pulleyJointDef.lengthB) * (tileWidth + tileHeight) / 2 * unitScale;
            pulleyJointDef.localAnchorA.set(getProperty(properties, aliases.localAnchorAX, pulleyJointDef.localAnchorA.x) * tileWidth * unitScale, getProperty(properties, aliases.localAnchorAY, pulleyJointDef.localAnchorA.y) * tileHeight * unitScale);
            pulleyJointDef.localAnchorB.set(getProperty(properties, aliases.localAnchorBX, pulleyJointDef.localAnchorB.x) * tileWidth * unitScale, getProperty(properties, aliases.localAnchorBY, pulleyJointDef.localAnchorB.y) * tileHeight * unitScale);
            pulleyJointDef.ratio = getProperty(properties, aliases.ratio, pulleyJointDef.ratio);

            jointDef = pulleyJointDef;
        } else if(jointType.equals(aliases.revoluteJoint)) {
            RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
            revoluteJointDef.enableLimit = getProperty(properties, aliases.enableLimit, revoluteJointDef.enableLimit);
            revoluteJointDef.enableMotor = getProperty(properties, aliases.enableMotor, revoluteJointDef.enableMotor);
            revoluteJointDef.localAnchorA.set(getProperty(properties, aliases.localAnchorAX, revoluteJointDef.localAnchorA.x) * tileWidth * unitScale, getProperty(properties, aliases.localAnchorAY, revoluteJointDef.localAnchorA.y) * tileHeight * unitScale);
            revoluteJointDef.localAnchorB.set(getProperty(properties, aliases.localAnchorBX, revoluteJointDef.localAnchorB.x) * tileWidth * unitScale, getProperty(properties, aliases.localAnchorBY, revoluteJointDef.localAnchorB.y) * tileHeight * unitScale);
            revoluteJointDef.lowerAngle = getProperty(properties, aliases.lowerAngle, revoluteJointDef.lowerAngle);
            revoluteJointDef.maxMotorTorque = getProperty(properties, aliases.maxMotorTorque, revoluteJointDef.maxMotorTorque);
            revoluteJointDef.motorSpeed = getProperty(properties, aliases.motorSpeed, revoluteJointDef.motorSpeed);
            revoluteJointDef.referenceAngle = getProperty(properties, aliases.referenceAngle, revoluteJointDef.referenceAngle);
            revoluteJointDef.upperAngle = getProperty(properties, aliases.upperAngle, revoluteJointDef.upperAngle);

            jointDef = revoluteJointDef;
        } else if(jointType.equals(aliases.ropeJoint)) {
            RopeJointDef ropeJointDef = new RopeJointDef();
            ropeJointDef.localAnchorA.set(getProperty(properties, aliases.localAnchorAX, ropeJointDef.localAnchorA.x) * tileWidth * unitScale, getProperty(properties, aliases.localAnchorAY, ropeJointDef.localAnchorA.y) * tileHeight * unitScale);
            ropeJointDef.localAnchorB.set(getProperty(properties, aliases.localAnchorBX, ropeJointDef.localAnchorB.x) * tileWidth * unitScale, getProperty(properties, aliases.localAnchorBY, ropeJointDef.localAnchorB.y) * tileHeight * unitScale);
            ropeJointDef.maxLength = getProperty(properties, aliases.maxLength, ropeJointDef.maxLength) * (tileWidth + tileHeight) / 2 * unitScale;

            jointDef = ropeJointDef;
        } else if(jointType.equals(aliases.weldJoint)) {
            WeldJointDef weldJointDef = new WeldJointDef();
            weldJointDef.localAnchorA.set(getProperty(properties, aliases.localAnchorAX, weldJointDef.localAnchorA.x) * tileWidth * unitScale, getProperty(properties, aliases.localAnchorAY, weldJointDef.localAnchorA.y) * tileHeight * unitScale);
            weldJointDef.localAnchorB.set(getProperty(properties, aliases.localAnchorBX, weldJointDef.localAnchorB.x) * tileWidth * unitScale, getProperty(properties, aliases.localAnchorBY, weldJointDef.localAnchorB.y) * tileHeight * unitScale);
            weldJointDef.referenceAngle = getProperty(properties, aliases.referenceAngle, weldJointDef.referenceAngle);

            jointDef = weldJointDef;
        } else if(jointType.equals(aliases.wheelJoint)) {
            WheelJointDef wheelJointDef = new WheelJointDef();
            wheelJointDef.dampingRatio = getProperty(properties, aliases.dampingRatio, wheelJointDef.dampingRatio);
            wheelJointDef.enableMotor = getProperty(properties, aliases.enableMotor, wheelJointDef.enableMotor);
            wheelJointDef.frequencyHz = getProperty(properties, aliases.frequencyHz, wheelJointDef.frequencyHz);
            wheelJointDef.localAnchorA.set(getProperty(properties, aliases.localAnchorAX, wheelJointDef.localAnchorA.x) * tileWidth * unitScale, getProperty(properties, aliases.localAnchorAY, wheelJointDef.localAnchorA.y) * tileHeight * unitScale);
            wheelJointDef.localAnchorB.set(getProperty(properties, aliases.localAnchorBX, wheelJointDef.localAnchorB.x) * tileWidth * unitScale, getProperty(properties, aliases.localAnchorBY, wheelJointDef.localAnchorB.y) * tileHeight * unitScale);
            wheelJointDef.localAxisA.set(getProperty(properties, aliases.localAxisAX, wheelJointDef.localAxisA.x), getProperty(properties, aliases.localAxisAY, wheelJointDef.localAxisA.y));
            wheelJointDef.maxMotorTorque = getProperty(properties, aliases.maxMotorTorque, wheelJointDef.maxMotorTorque);
            wheelJointDef.motorSpeed = getProperty(properties, aliases.motorSpeed, wheelJointDef.motorSpeed);

            jointDef = wheelJointDef;
        }

        jointDef.bodyA = bodies.get(properties.get(aliases.bodyA, String.class));
        jointDef.bodyB = bodies.get(properties.get(aliases.bodyB, String.class));
        jointDef.collideConnected = getProperty(properties, aliases.collideConnected, jointDef.collideConnected);

        Joint joint = jointDef.bodyA.getWorld().createJoint(jointDef);

        String name = mapObject.getName();
        if(joints.containsKey(name)) {
            int duplicate = 1;
            while(joints.containsKey(name + duplicate))
                duplicate++;
            name += duplicate;
        }

        joints.put(name, joint);

        return joint;
    }

    /**
     * internal method for easier access of {@link MapProperties}
     * @param properties the {@link MapProperties} from which to get a property
     * @param property the key of the desired property
     * @param defaultValue the default value to return in case the value of the given key cannot be returned
     * @return the property value associated with the given property key
     */
    @SuppressWarnings("unchecked")
    private <T> T getProperty(MapProperties properties, String property, T defaultValue) {
        if(properties.get(property) == null)
            return defaultValue;
        if(defaultValue.getClass() == Float.class)
            if(properties.get(property).getClass() == Integer.class)
                return (T) new Float(properties.get(property, Integer.class));
            else
                return (T) new Float(Float.parseFloat(properties.get(property, String.class)));
        else if(defaultValue.getClass() == Short.class)
            return (T) new Short(Short.parseShort(properties.get(property, String.class)));
        else if(defaultValue.getClass() == Boolean.class)
            return (T) new Boolean(Boolean.parseBoolean(properties.get(property, String.class)));
        else
            return (T) properties.get(property, defaultValue.getClass());
    }

    /**
     * @param map the {@link Map} which hierarchy to print
     * @return a human readable {@link String} containing the hierarchy of the {@link MapObjects} of the given {@link Map}
     */
    public String getHierarchy(Map map) {
        String hierarchy = map.getClass().getName() + "\n", key, layerHierarchy;

        Iterator<String> keys = map.getProperties().getKeys();
        while(keys.hasNext())
            hierarchy += (key = keys.next()) + ": " + map.getProperties().get(key) + "\n";

        for(MapLayer layer : map.getLayers()) {
            hierarchy += "\t" + layer.getName() + " (" + layer.getClass().getName() + "):\n";
            layerHierarchy = getHierarchy(layer).replace("\n", "\n\t\t");
            layerHierarchy = layerHierarchy.endsWith("\n\t\t") ? layerHierarchy.substring(0, layerHierarchy.lastIndexOf("\n\t\t")) : layerHierarchy;
            hierarchy += !layerHierarchy.equals("") ? "\t\t" + layerHierarchy : layerHierarchy;
        }

        return hierarchy;
    }

    /**
     * @param layer the {@link MapLayer} which hierarchy to print
     * @return a human readable {@link String} containing the hierarchy of the {@link MapObjects} of the given {@link MapLayer}
     */
    public String getHierarchy(MapLayer layer) {
        String hierarchy = "", key;

        for(MapObject object : layer.getObjects()) {
            hierarchy += object.getName() + " (" + object.getClass().getName() + "):\n";
            Iterator<String> keys = object.getProperties().getKeys();
            while(keys.hasNext())
                hierarchy += "\t" + (key = keys.next()) + ": " + object.getProperties().get(key) + "\n";
        }

        return hierarchy;
    }

    /** @return the {@link #unitScale} */
    public float getUnitScale() {
        return unitScale;
    }

    /** @param unitScale the {@link #unitScale} to set */
    public void setUnitScale(float unitScale) {
        this.unitScale = unitScale;
    }

    /** @return the {@link #ignoreMapUnitScale} */
    public boolean isIgnoreMapUnitScale() {
        return ignoreMapUnitScale;
    }

    /** @param ignoreMapUnitScale the {@link #ignoreMapUnitScale} to set */
    public void setIgnoreMapUnitScale(boolean ignoreMapUnitScale) {
        this.ignoreMapUnitScale = ignoreMapUnitScale;
    }

    /** @return the {@link #tileWidth} */
    public float getTileWidth() {
        return tileWidth;
    }

    /** @param tileWidth the {@link #tileWidth} to set */
    public void setTileWidth(float tileWidth) {
        this.tileWidth = tileWidth;
    }

    /** @return the {@link #tileHeight} */
    public float getTileHeight() {
        return tileHeight;
    }

    /** @param tileHeight the {@link #tileHeight} to set */
    public void setTileHeight(float tileHeight) {
        this.tileHeight = tileHeight;
    }

    /** @return the {@link #triangulate} */
    public boolean isTriangulate() {
        return triangulate;
    }

    /** @param triangulate the {@link #triangulate} to set */
    public void setTriangulate(boolean triangulate) {
        this.triangulate = triangulate;
    }

    /** @return the {@link Aliases} */
    public Aliases getAliases() {
        return aliases;
    }

    /** @param aliases the {@link Aliases} to set */
    public void setAliases(Aliases aliases) {
        this.aliases = aliases;
    }

    /** @return the parsed {@link #bodies} */
    public ObjectMap<String, Body> getBodies() {
        return bodies;
    }

    /** @return the parsed {@link #fixtures} */
    public ObjectMap<String, Fixture> getFixtures() {
        return fixtures;
    }

    /** @return the parsed {@link #joints} */
    public ObjectMap<String, Joint> getJoints() {
        return joints;
    }


    /** defines the {@link #aliases} to use when parsing */
    public static class Aliases {

        /** the aliases */
        public String
                type = "type",
                bodyType = "bodyType",
                dynamicBody = "DynamicBody",
                kinematicBody = "KinematicBody",
                staticBody = "StaticBody",
                active = "active",
                allowSleep = "allowSleep",
                angle = "angle",
                angularDamping = "angularDamping",
                angularVelocity = "angularVelocity",
                awake = "awake",
                bullet = "bullet",
                fixedRotation = "fixedRotation",
                gravityunitScale = "gravityunitScale",
                linearDamping = "linearDamping",
                linearVelocityX = "linearVelocityX",
                linearVelocityY = "linearVelocityY",
                density = "density",
                categoryBits = "categoryBits",
                groupIndex = "groupIndex",
                maskBits = "maskBits",
                friciton = "friction",
                isSensor = "isSensor",
                restitution = "restitution",
                body = "body",
                fixture = "fixture",
                joint = "joint",
                jointType = "jointType",
                distanceJoint = "DistanceJoint",
                frictionJoint = "FrictionJoint",
                gearJoint = "GearJoint",
                mouseJoint = "MouseJoint",
                prismaticJoint = "PrismaticJoint",
                pulleyJoint = "PulleyJoint",
                revoluteJoint = "RevoluteJoint",
                ropeJoint = "RopeJoint",
                weldJoint = "WeldJoint",
                wheelJoint = "WheelJoint",
                bodyA = "bodyA",
                bodyB = "bodyB",
                collideConnected = "collideConnected",
                dampingRatio = "dampingRatio",
                frequencyHz = "frequencyHz",
                length = "length",
                localAnchorAX = "localAnchorAX",
                localAnchorAY = "localAnchorAY",
                localAnchorBX = "localAnchorBX",
                localAnchorBY = "localAnchorBY",
                maxForce = "maxForce",
                maxTorque = "maxTorque",
                joint1 = "joint1",
                joint2 = "joint2",
                ratio = "ratio",
                targetX = "targetX",
                targetY = "targetY",
                enableLimit = "enableLimit",
                enableMotor = "enableMotor",
                localAxisAX = "localAxisAX",
                localAxisAY = "localAxisAY",
                lowerTranslation = "lowerTranslation",
                maxMotorForce = "maxMotorForce",
                motorSpeed = "motorSpeed",
                referenceAngle = "referenceAngle",
                upperTranslation = "upperTranslation",
                groundAnchorAX = "groundAnchorAX",
                groundAnchorAY = "groundAnchorAY",
                groundAnchorBX = "groundAnchorBX",
                groundAnchorBY = "groundAnchorBY",
                lengthA = "lengthA",
                lengthB = "lengthB",
                lowerAngle = "lowerAngle",
                maxMotorTorque = "maxMotorTorque",
                upperAngle = "upperAngle",
                maxLength = "maxLength",
                object = "object",
                modelObject = "modelObject",
                typeModelObject = "typeModelObject",
                movingPlatform = "MovingPlatform",
                movingPlatformDistY = "movingPlatformDistY",
                movingPlatformDistX = "movingPlatformDistX",
                movingPlatformSpeed = "movingPlatformSpeed",
                pointX = "pointX",
                pointY = "pointY",
                water = "Water",
                enemy = "Enemy",
                hero = "Hero",
                item = "Item",
                typeItem = "typeItem",
                coin=   "coin",
                powerup=   "powerup",
                key= "key",
                unitScale = "unitScale";
    }

}
