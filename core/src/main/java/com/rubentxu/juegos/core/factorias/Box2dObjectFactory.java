package com.rubentxu.juegos.core.factorias;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.managers.game.ResourcesManager;
import com.rubentxu.juegos.core.modelo.CheckPoint;
import com.rubentxu.juegos.core.modelo.Coin;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Hero;
import com.rubentxu.juegos.core.modelo.Item;
import com.rubentxu.juegos.core.modelo.Mill;
import com.rubentxu.juegos.core.modelo.MovingPlatform;
import com.rubentxu.juegos.core.modelo.Water;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DMapObjectParser;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DUtils;

import java.util.ArrayList;
import java.util.List;

import static com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPO;

public class Box2dObjectFactory {


    private com.rubentxu.juegos.core.modelo.World world;
    private World physics;
    private ResourcesManager resourcesManager;
    private float unitScale;

    public Box2dObjectFactory(World physics, com.rubentxu.juegos.core.modelo.World world, ResourcesManager resourcesManager) {
        this.physics = physics;
        this.world=world;
        this.resourcesManager = resourcesManager;
    }

    public void createEntity(GRUPO tipo, MapObject object) {
        switch (tipo) {
            case HERO:
                Hero hero=createHero(object);
                world.getEntities().add(hero);
                world.setHero(hero);
                break;
            case ENEMY:
                world.getEntities().add(createEnemy(object));
                break;
            case PLATFORM:
                break;
            case MOVING_PLATFORM:
                world.getEntities().add(createMovingPlatform(object));
                break;
            case ITEMS:
                world.getEntities().add(createItem(object));
                break;
            case SENSOR:
                break;
            case STATIC:
                break;
            case FLUID:
                world.getEntities().add(createWater(object));
                break;
            case MILL:
                world.getEntities().add(createMill(object));
                break;
            case CHECKPOINT:
                world.getEntities().add(createCheckPoint(object));
                break;
        }

    }

    private Rectangle getRectangle(RectangleMapObject object) {
        Rectangle rectangle = new Rectangle(((RectangleMapObject) object).getRectangle());
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

    public Water createWater(MapObject object) {
        Water water = null;
        MapProperties properties = object.getProperties();
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(getProperty(properties, "x", def.position.x) * unitScale, getProperty(properties, "y", def.position.y) * unitScale);
        Body box = physics.createBody(def);

        if (object instanceof RectangleMapObject && !properties.get(Box2DMapObjectParser.Aliases.type).equals(Box2DMapObjectParser.Aliases.typeModelObject)) {
            PolygonShape shape = new PolygonShape();
            Rectangle rectangle = getRectangle((RectangleMapObject) object);
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
            water.setWidthBodyA(rectangle.width);
            water.setHeightBodyA(rectangle.height);
            box.setUserData(water);
            fixBox.setUserData(water);


        } else {
            throw new IllegalArgumentException("type of " + object + " is  \"" + properties.get(Box2DMapObjectParser.Aliases.type) + "\" instead of \"" + Box2DMapObjectParser.Aliases.typeModelObject + "\"");
        }
        return water;
    }

    public MovingPlatform createMovingPlatform(MapObject object) {
        MovingPlatform movingPlatform = null;
        MapProperties properties = object.getProperties();
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.KinematicBody;
        def.position.set(getProperty(properties, "x", def.position.x) * unitScale, getProperty(properties, "y", def.position.y) * unitScale);
        Body box = physics.createBody(def);

        if (object instanceof RectangleMapObject && !properties.get(Box2DMapObjectParser.Aliases.type).equals(Box2DMapObjectParser.Aliases.typeModelObject)) {
            PolygonShape shape = new PolygonShape();
            Rectangle rectangle = getRectangle((RectangleMapObject) object);
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
            movingPlatform.setWidthBodyA(rectangle.width);
            movingPlatform.setHeightBodyA(rectangle.height);
            box.setUserData(movingPlatform);
            fixBox.setUserData(movingPlatform);


        } else {
            throw new IllegalArgumentException("type of " + object + " is  \"" + properties.get(Box2DMapObjectParser.Aliases.type) + "\" instead of \"" + Box2DMapObjectParser.Aliases.typeModelObject + "\"");
        }
        return movingPlatform;
    }

    public Enemy createEnemy(MapObject object) {
        Enemy enemy = null;
        MapProperties properties = object.getProperties();
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(getProperty(properties, "x", def.position.x) * unitScale, getProperty(properties, "y", def.position.y) * unitScale);
        Body box = physics.createBody(def);
        box.setFixedRotation(true);

        if (object instanceof RectangleMapObject && !properties.get(Box2DMapObjectParser.Aliases.type).equals(Box2DMapObjectParser.Aliases.typeModelObject)) {
            PolygonShape shape = new PolygonShape();
            Rectangle rectangle = getRectangle((RectangleMapObject) object);
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
            enemy.setWidthBodyA(rectangle.width);
            enemy.setHeightBodyA(rectangle.height);
            box.setUserData(enemy);
            enemyPhysicsFixture.setUserData(enemy);
            enemySensorFixture.setUserData(enemy);


        } else {
            throw new IllegalArgumentException("type of " + object + " is  \"" + properties.get(Box2DMapObjectParser.Aliases.type) + "\" instead of \"" + Box2DMapObjectParser.Aliases.typeModelObject + "\"");
        }
        return enemy;
    }

    public Hero createHero(MapObject object) {
        Hero hero = null;
        MapProperties properties = object.getProperties();
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(getProperty(properties, "x", def.position.x) * unitScale, getProperty(properties, "y", def.position.y) * unitScale);
        Body bodyA = physics.createBody(def);
        bodyA.setFixedRotation(true);

        if (object instanceof RectangleMapObject) {
            PolygonShape shape = new PolygonShape();
            float width= 1f;
            float height=2f;
            Rectangle rectangle = getRectangle((RectangleMapObject) object);
            shape.setAsBox(width / 2, height / 2, new Vector2(rectangle.x - bodyA.getPosition().x + width / 2, rectangle.y - bodyA.getPosition().y + height / 2), bodyA.getAngle());

            FixtureDef fixDef = new FixtureDef();
            fixDef.shape = shape;
            fixDef.filter.categoryBits = GRUPO.HERO.getCategory();
            fixDef.filter.maskBits = Box2DPhysicsObject.MASK_HERO;
            Fixture heroPhysicsFixture = bodyA.createFixture(fixDef);

            CircleShape circle = new CircleShape();
            circle.setRadius(width / 2);
            circle.setPosition(new Vector2(width / 2, 0 ));
            fixDef.shape = circle;
            Fixture heroSensorFixture = bodyA.createFixture(fixDef);
            heroSensorFixture.setSensor(true);

            bodyA.setBullet(true);

            hero = new Hero("hero", bodyA);
            hero.setHeroPhysicsFixture(heroPhysicsFixture);
            hero.setHeroSensorFixture(heroSensorFixture);
            hero.setWidthBodyA(width);
            hero.setHeightBodyA(height);
            bodyA.setUserData(hero);
            float scaleX=1.7f;
            float scaleY=1.15f;
            hero.getScaleBodyA().set(scaleX,scaleY);
            /*    NuevoOrigen=((NuevoAncho/2)-(ViejoAncho/2)) / 2    */
            hero.getOriginBodyA().set(((width*scaleX/2)-(width/2))/2f,((height*scaleY/2)-(height/2))/2f);
            heroPhysicsFixture.setUserData(hero);
            heroSensorFixture.setUserData(hero);
            hero.setParticleEffectDust((ParticleEffect) resourcesManager.get(ResourcesManager.PARTICLE_EFFECT));
            hero.setParticleEffectContact((ParticleEffect) resourcesManager.get(ResourcesManager.PARTICLE_EFFECT_CONTACT));
            shape.dispose();
            circle.dispose();

        } else {
            throw new IllegalArgumentException("type of " + object + " is  \"" + properties.get(Box2DMapObjectParser.Aliases.type) + "\" instead of \"" + Box2DMapObjectParser.Aliases.typeModelObject + "\"");
        }
        return hero;
    }

    private Item createItem(MapObject object) {
        Item item = null;
        MapProperties properties = object.getProperties();
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(getProperty(properties, "x", def.position.x) * unitScale, getProperty(properties, "y", def.position.y) * unitScale);
        Body box = physics.createBody(def);

        if (object instanceof RectangleMapObject && !properties.get(Box2DMapObjectParser.Aliases.type).equals(Box2DMapObjectParser.Aliases.typeModelObject)) {
            PolygonShape shape = new PolygonShape();
            float width=1;
            float height=1;
            Rectangle rectangle = getRectangle((RectangleMapObject) object);
            shape.setAsBox(width / 2, height/2, new Vector2(rectangle.x - box.getPosition().x
                    + width / 2, rectangle.y - box.getPosition().y + height / 2), box.getAngle());

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
            item.setWidthBodyA(width);
            item.setHeightBodyA(height);
            box.setUserData(item);
            fixBox.setUserData(item);


        } else {
            throw new IllegalArgumentException("type of " + object + " is  \"" + properties.get(Box2DMapObjectParser.Aliases.type) + "\" instead of \"" + Box2DMapObjectParser.Aliases.typeModelObject + "\"");
        }
        return item;

    }

    private Mill createMill(MapObject object) {
        Gdx.app.log(Constants.LOG, "Creando Mill");
        RevoluteJointDef revoluteJoint;
        MapProperties properties = object.getProperties();
        Mill mill = null;
        if (object instanceof RectangleMapObject) { 
            Vector2[] vertices = new Vector2[6];
            vertices[0] = new Vector2(0.3256686329841614f, 0f);
            vertices[1] = new Vector2(0.1628349423408508f, 3.026409149169922f);
            vertices[2] = new Vector2(-0.1628349423408508f, 3.026409149169922f);
            vertices[3] = new Vector2(-0.3256686329841614f,-3.055059494272427e-07f);
            vertices[4] = new Vector2(-0.1628349423408508f, -3.026409149169922f);
            vertices[5] = new Vector2(0.1628349423408508f, -3.026409149169922f);

            BodyDef def = new BodyDef();
            def.type = BodyType.StaticBody;
            Vector2 position = new Vector2(getProperty(properties, "x", def.position.x) * unitScale, getProperty(properties, "y", def.position.y) * unitScale);

            Gdx.app.log(Constants.LOG, "Creando Mill");
            CircleShape circle = new CircleShape();
            circle.setRadius(0.5f);
            Body bodyA = physics.createBody(def);
            bodyA.setTransform(position, 0);

            Fixture fixtA = bodyA.createFixture(circle, 1);
            Gdx.app.log(Constants.LOG, "Creando2 Mill");

            PolygonShape shape = new PolygonShape();
            shape.set(vertices);

            FixtureDef fd = new FixtureDef();
            fd.shape = shape;
            fd.friction = 0.2f;
            fd.density = 1;
            fd.filter.categoryBits = GRUPO.MILL.getCategory();
            fd.filter.maskBits = Box2DPhysicsObject.MASK_INTERACTIVE;
            BodyDef bd = new BodyDef();
            bd.type = BodyType.DynamicBody;
            bd.position.set(position);
            Body bodyB = physics.createBody(bd);

            Fixture fixtB = bodyB.createFixture(fd);
            Gdx.app.log(Constants.LOG, "Creando3 Mill");

            revoluteJoint = new RevoluteJointDef();
            revoluteJoint.bodyA = bodyA;
            revoluteJoint.bodyB = bodyB;
            revoluteJoint.localAnchorA.set(new Vector2(0, 0));
            revoluteJoint.localAnchorB.set(new Vector2(0, 0));
            revoluteJoint.collideConnected = false;
            revoluteJoint.enableMotor = true;
            revoluteJoint.maxMotorTorque = 10000.0f;
            revoluteJoint.motorSpeed = 1f;
            RevoluteJoint rj = (RevoluteJoint) physics.createJoint(revoluteJoint);

            bodyB.resetMassData();
            mill = new Mill(object.getName(), bodyA, bodyB, rj);
            mill.getOriginBodyA().set(0.5f,0.5f);
            mill.getOriginBodyB().set(Box2DUtils.width(bodyB)/2,Box2DUtils.height(bodyB)/2);
            mill.setWidthBodyA(1);
            mill.setHeightBodyA(1);
            mill.setWidthBodyB(Box2DUtils.width(bodyB));
            mill.setHeightBodyB(Box2DUtils.height(bodyB));
            bodyA.setUserData(mill);
            bodyB.setUserData(mill);
            fixtA.setUserData(mill);
            fixtB.setUserData(mill);

            shape.dispose();
            circle.dispose();

        } else {
            throw new IllegalArgumentException("type of " + object + " is  \"" + properties.get(Box2DMapObjectParser.Aliases.type) + "\" instead of \"" + Box2DMapObjectParser.Aliases.typeModelObject + "\"");
        }
        Gdx.app.log(Constants.LOG, "Terminada la Creacion de Mill");
        return mill;
    }

    private CheckPoint createCheckPoint(MapObject object) {
        CheckPoint checkPoint = null;
        if (object instanceof RectangleMapObject) {
            Rectangle rectangle = getRectangle((RectangleMapObject) object);

            PrismaticJoint m_joint;
            BodyDef def = new BodyDef();
            def.type = BodyType.StaticBody;
            Body bodyA = physics.createBody(def);
            bodyA.setTransform(rectangle.x, rectangle.y + 5f, 0);
            PolygonShape poly = new PolygonShape();
            poly.setAsBox(0.25f, 5);
            FixtureDef fixBox = new FixtureDef();
            fixBox.isSensor = true;
            fixBox.shape = poly;
            fixBox.filter.categoryBits = GRUPO.CHECKPOINT.getCategory();
            fixBox.filter.maskBits = Box2DPhysicsObject.MASK_INTERACTIVE;
            Fixture fixtA = bodyA.createFixture(fixBox);


            PolygonShape shape = new PolygonShape();
            shape.setAsBox(1.1f, 0.8f);

            BodyDef bd = new BodyDef();
            bd.type = BodyType.DynamicBody;
            bd.position.set(rectangle.x, rectangle.y + 1.5f);
            Body bodyB = physics.createBody(bd);
            FixtureDef fixBd = new FixtureDef();
            fixBd.isSensor = true;
            fixBd.shape = shape;


            PrismaticJointDef pjd = new PrismaticJointDef();
            pjd.localAxisA.set(0, 1);
            pjd.bodyA = bodyA;
            pjd.bodyB = bodyB;
            pjd.collideConnected = false;
            pjd.localAnchorA.set(0, -4f);
            pjd.localAnchorB.set(-1, 0.25f);
            pjd.motorSpeed = 3.0f;
            pjd.maxMotorForce = 10000.0f;
            pjd.enableMotor = false;
            pjd.lowerTranslation = 0.0f;
            pjd.upperTranslation = 9.0f;
            Fixture fixtB = bodyB.createFixture(fixBd);
            pjd.enableLimit = true;

            m_joint = (PrismaticJoint) physics.createJoint(pjd);

            checkPoint = new CheckPoint(object.getName(), bodyA, bodyB, m_joint);
            checkPoint.setWidthBodyA(0.25f*2);
            checkPoint.setHeightBodyA(5f*2);
            checkPoint.setWidthBodyB(1.1f*2);
            checkPoint.setHeightBodyB(0.8f*2);
            checkPoint.getOriginBodyA().set(Box2DUtils.width(bodyA)/2,Box2DUtils.height(bodyA)/2);
            checkPoint.getOriginBodyB().set(Box2DUtils.width(bodyB)/2,Box2DUtils.height(bodyB)/2);


            bodyA.setUserData(checkPoint);
            bodyB.setUserData(checkPoint);
            fixtA.setUserData(checkPoint);
            fixtB.setUserData(checkPoint);
            poly.dispose();
            shape.dispose();
        }
        return checkPoint;
    }


    public void setUnitScale(float unitScale) {
        this.unitScale = unitScale;
    }
}
