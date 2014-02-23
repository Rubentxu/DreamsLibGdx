package com.rubentxu.juegos.core.factorias;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.rubentxu.juegos.core.managers.game.ResourcesManager;
import com.rubentxu.juegos.core.modelo.Coin;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Hero;
import com.rubentxu.juegos.core.modelo.Item;
import com.rubentxu.juegos.core.modelo.MovingPlatform;
import com.rubentxu.juegos.core.modelo.Water;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DMapObjectParser;

import java.util.ArrayList;
import java.util.List;

import static com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPO;

public class Box2dObjectFactory {


    private World world;
    private ResourcesManager resourcesManager;
    private float unitScale;

    public Box2dObjectFactory(World world, ResourcesManager resourcesManager) {
        this.world = world;
        this.resourcesManager = resourcesManager;
    }

    public <T> T getEntity(GRUPO tipo, MapObject object) {

        switch (tipo) {
            case HERO:
                return (T) createHero(object);
            case ENEMY:
                return (T) createEnemy(object);
            case PLATFORM:
                break;
            case MOVING_PLATFORM:
                return (T) createMovingPlatform(object);
            case ITEMS:
                return (T) createItem(object);
            case SENSOR:
                break;
            case STATIC:
                break;
            case FLUID:
                return (T) createWater(object);
        }
        return null;
    }

    private Rectangle getRectangle(RectangleMapObject object) {
        Rectangle rectangle = new Rectangle(((RectangleMapObject) object).getRectangle());
        System.out.println("Rectangle Water " + rectangle);
        rectangle.x *= unitScale;
        rectangle.y *= unitScale;
        rectangle.width *= unitScale;
        rectangle.height *= unitScale;
        return rectangle;
    }

    @SuppressWarnings("unchecked")
    private <T> T getProperty(MapProperties properties, String property, T defaultValue) {
        if (properties.get(property) == null)
            return defaultValue;
        if (defaultValue.getClass() == Float.class)
            if (properties.get(property).getClass() == Integer.class)
                return (T) new Float(properties.get(property, Integer.class));
            else
                return (T) new Float(Float.parseFloat(properties.get(property, String.class)));
        else if (defaultValue.getClass() == Short.class)
            return (T) new Short(Short.parseShort(properties.get(property, String.class)));
        else if (defaultValue.getClass() == Boolean.class)
            return (T) new Boolean(Boolean.parseBoolean(properties.get(property, String.class)));
        else
            return (T) properties.get(property, defaultValue.getClass());
    }

    public Hero createHero(MapObject object) {
        Rectangle rectangle = getRectangle((RectangleMapObject) object);
        Hero hero = new Hero(world, rectangle.x, rectangle.y, 0.45f, 1f);
        hero.setEffect((ParticleEffect) resourcesManager.get(ResourcesManager.PARTICLE_EFFECT));
        return hero;
    }

    public Water createWater(MapObject object) {
        Water water = null;
        MapProperties properties = object.getProperties();
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(getProperty(properties, "x", def.position.x) * unitScale, getProperty(properties, "y", def.position.y) * unitScale);
        Body box = world.createBody(def);

        if (object instanceof RectangleMapObject && !properties.get(Box2DMapObjectParser.Aliases.type).equals(Box2DMapObjectParser.Aliases.typeModelObject)) {
            PolygonShape shape = new PolygonShape();
            Rectangle rectangle = getRectangle((RectangleMapObject) object);
            System.out.println("Rectangle Water2 " + rectangle);
            shape.setAsBox(rectangle.width / 2, rectangle.height / 2, new Vector2(rectangle.x - box.getPosition().x
                    + rectangle.width / 2, rectangle.y - box.getPosition().y + rectangle.height / 2), box.getAngle());

            FixtureDef fixDef = new FixtureDef();
            fixDef.shape = shape;
            fixDef.friction = 1f;
            fixDef.isSensor = true;
            fixDef.density = getProperty(properties, Box2DMapObjectParser.Aliases.density, 2);
            fixDef.filter.categoryBits = GRUPO.FLUID.getCategory();
            fixDef.filter.maskBits = Box2DPhysicsObject.MASK_FLUID;
            Fixture fixBox = box.createFixture(fixDef);
            shape.dispose();

            water = new Water(object.getName(), box);

            box.setUserData(water);
            fixBox.setUserData(water);


        } else {
            throw new IllegalArgumentException("type of " + object + " is  \"" + properties.get(Box2DMapObjectParser.Aliases.type) + "\" instead of \"" + Box2DMapObjectParser.Aliases.typeModelObject + "\"");
        }
        return water;
    }

    public MovingPlatform createMovingPlatform(MapObject object) {
        MovingPlatform movingPlatform=null;
        MapProperties properties = object.getProperties();
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.KinematicBody;
        def.position.set(getProperty(properties, "x", def.position.x) * unitScale, getProperty(properties, "y", def.position.y) * unitScale);
        Body box = world.createBody(def);

        if (object instanceof RectangleMapObject && !properties.get(Box2DMapObjectParser.Aliases.type).equals(Box2DMapObjectParser.Aliases.typeModelObject)) {
            PolygonShape shape = new PolygonShape();
            Rectangle rectangle = new Rectangle(((RectangleMapObject) object).getRectangle());
            rectangle.x *= unitScale;
            rectangle.y *= unitScale;
            rectangle.width *= unitScale;
            rectangle.height *= unitScale;
            shape.setAsBox(rectangle.width / 2, rectangle.height / 2, new Vector2(rectangle.x - box.getPosition().x
                    + rectangle.width / 2, rectangle.y - box.getPosition().y + rectangle.height / 2), box.getAngle());

            FixtureDef fixDef = new FixtureDef();
            fixDef.shape = shape;
            fixDef.filter.categoryBits = GRUPO.MOVING_PLATFORM.getCategory();
            fixDef.filter.maskBits = Box2DPhysicsObject.MASK_MOVING_PLATFORM;
            Fixture fixBox = box.createFixture(fixDef);
            shape.dispose();
            box.setBullet(true);

            String name = object.getName();

            movingPlatform = new MovingPlatform(name, GRUPO.MOVING_PLATFORM, box, Float.parseFloat(properties.get(Box2DMapObjectParser.Aliases.movingPlatformDistX, String.class))
                    , Float.parseFloat(properties.get(Box2DMapObjectParser.Aliases.movingPlatformDistY, String.class)), Float.parseFloat(properties.get(Box2DMapObjectParser.Aliases.movingPlatformSpeed, String.class)));

            box.setUserData(movingPlatform);
            fixBox.setUserData(movingPlatform);


        } else {
            throw new IllegalArgumentException("type of " + object + " is  \"" + properties.get(Box2DMapObjectParser.Aliases.type) + "\" instead of \"" + Box2DMapObjectParser.Aliases.typeModelObject + "\"");
        }
        return movingPlatform;
    }

    public Enemy createEnemy( MapObject object) {
        Enemy enemy=null;
        MapProperties properties = object.getProperties();
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(getProperty(properties, "x", def.position.x) * unitScale, getProperty(properties, "y", def.position.y) * unitScale);
        Body box = world.createBody(def);
        box.setFixedRotation(true);

        if (object instanceof RectangleMapObject && !properties.get(Box2DMapObjectParser.Aliases.type).equals(Box2DMapObjectParser.Aliases.typeModelObject)) {
            PolygonShape shape = new PolygonShape();
            Rectangle rectangle = new Rectangle(((RectangleMapObject) object).getRectangle());
            rectangle.x *= unitScale;
            rectangle.y *= unitScale;
            rectangle.width *= unitScale;
            rectangle.height *= unitScale;
            shape.setAsBox(rectangle.width / 2, rectangle.height / 2, new Vector2(rectangle.x - box.getPosition().x
                    + rectangle.width / 2, rectangle.y - box.getPosition().y + rectangle.height / 2), box.getAngle());

            FixtureDef fixDef = new FixtureDef();
            fixDef.shape = shape;
            fixDef.filter.categoryBits = GRUPO.ENEMY.getCategory();
            fixDef.filter.maskBits = Box2DPhysicsObject.MASK_ENEMY;
            Fixture enemyPhysicsFixture = box.createFixture(fixDef);
            shape.dispose();
            CircleShape circle = new CircleShape();
            circle.setRadius(rectangle.width / 2);
            circle.setPosition(new Vector2(rectangle.width / 2, rectangle.height / 5));
            Fixture enemySensorFixture = box.createFixture(circle, 0);
            enemySensorFixture.setSensor(true);
            circle.dispose();

            String name = object.getName();
            List<Vector2> points = new ArrayList<Vector2>();
            points.add(box.getPosition().cpy());
            points.add(new Vector2(Float.parseFloat(properties.get(Box2DMapObjectParser.Aliases.pointX, String.class)),
                    Float.parseFloat(properties.get(Box2DMapObjectParser.Aliases.pointY, String.class))));


            enemy = new Enemy(name, box, points);
            enemy.setEnemyPhysicsFixture(enemyPhysicsFixture);
            enemy.setEnemySensorFixture(enemySensorFixture);
            box.setUserData(enemy);
            enemyPhysicsFixture.setUserData(enemy);
            enemySensorFixture.setUserData(enemy);


        } else {
            throw new IllegalArgumentException("type of " + object + " is  \"" + properties.get(Box2DMapObjectParser.Aliases.type) + "\" instead of \"" + Box2DMapObjectParser.Aliases.typeModelObject + "\"");
        }
        return enemy;
    }

    private Item createItem(MapObject object) {
        Item item=null;
        MapProperties properties = object.getProperties();
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(getProperty(properties, "x", def.position.x) * unitScale, getProperty(properties, "y", def.position.y) * unitScale);
        Body box = world.createBody(def);

        if (object instanceof RectangleMapObject && !properties.get(Box2DMapObjectParser.Aliases.type).equals(Box2DMapObjectParser.Aliases.typeModelObject)) {
            PolygonShape shape = new PolygonShape();
            Rectangle rectangle = new Rectangle(((RectangleMapObject) object).getRectangle());
            rectangle.x *= unitScale;
            rectangle.y *= unitScale;
            rectangle.width *= unitScale;
            rectangle.height *= unitScale;
            shape.setAsBox(rectangle.width / 2, rectangle.height, new Vector2(rectangle.x - box.getPosition().x
                    + rectangle.width / 2, rectangle.y - box.getPosition().y + rectangle.height / 2), box.getAngle());

            FixtureDef fixDef = new FixtureDef();
            fixDef.shape = shape;
            fixDef.filter.categoryBits = GRUPO.ITEMS.getCategory();
            fixDef.filter.maskBits = Box2DPhysicsObject.MASK_ITEMS;
            Fixture fixBox = box.createFixture(fixDef);
            fixBox.setSensor(true);
            shape.dispose();

            String name = object.getName();

            if (object.getProperties().get(Box2DMapObjectParser.Aliases.typeItem).equals(Box2DMapObjectParser.Aliases.coin)) {
                item = new Coin(name, GRUPO.ITEMS, Item.TYPE.COIN, box, 1);

            } else if (object.getProperties().get(Box2DMapObjectParser.Aliases.typeItem).equals(Box2DMapObjectParser.Aliases.powerup)) {
                item = new Item(name, GRUPO.ITEMS, Item.TYPE.POWERUP, box, 1);
            } else if (object.getProperties().get(Box2DMapObjectParser.Aliases.typeItem).equals(Box2DMapObjectParser.Aliases.key)) {
                item = new Item(name, GRUPO.ITEMS, Item.TYPE.KEY, box, 1);
            }

            box.setUserData(item);
            fixBox.setUserData(item);



        } else {
            throw new IllegalArgumentException("type of " + object + " is  \"" + properties.get(Box2DMapObjectParser.Aliases.type) + "\" instead of \"" + Box2DMapObjectParser.Aliases.typeModelObject + "\"");
        }
        return item;

    }


    public void setUnitScale(float unitScale) {
        this.unitScale = unitScale;
    }
}
