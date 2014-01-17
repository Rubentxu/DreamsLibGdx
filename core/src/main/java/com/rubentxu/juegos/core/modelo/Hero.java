package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;

import java.util.HashSet;


public class Hero extends Box2DPhysicsObject implements Disposable {

    public enum State {
        IDLE, WALKING, JUMPING, DYING, FALL,SWIMMING, HURT
    }

    public final static float MAX_VELOCITY = 4f;
    public final static float JUMP_FORCE = 14.5f;


    private HashSet<Fixture> grounContacts;
    private Fixture heroPhysicsFixture;
    private Fixture heroSensorFixture;

    // Status
    private boolean onGround = false;
    private State state = State.IDLE;
    private Boolean hurt;
    boolean facingLeft = true;

    private Profile profile;

    public Hero(World physics) {
        super("Heroe", GRUPO.HERO, physics);
        setGrounContacts(new HashSet<Fixture>());
    }

    public Hero(World physics, float x, float y, float width, float height) {
        super("Heroe", GRUPO.HERO, physics, x, y, width, height, 0);
        setGrounContacts(new HashSet<Fixture>());
        createHero(x, y, width, height);
    }

    public void createHero(float x, float y, float width, float height) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.x = x;
        def.position.y = y;
        setBody(box2D.createBody(def));
        getBody().setFixedRotation(true);
        getBody().setUserData(this);


        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width,height);


        FixtureDef fixDef = new FixtureDef();
        fixDef.shape=poly;
        fixDef.filter.categoryBits= GRUPO.HERO.getCategory();
        fixDef.filter.maskBits=Box2DPhysicsObject.MASK_HERO;
        heroPhysicsFixture = super.getBody().createFixture(fixDef);
        heroPhysicsFixture.setUserData(this);
        poly.dispose();

        CircleShape circle = new CircleShape();
        circle.setRadius(width);
        circle.setPosition(new Vector2(0, -height*0.9f));
        fixDef.shape=circle;
        heroSensorFixture = super.getBody().createFixture(fixDef);
        heroSensorFixture.setSensor(true);
        heroSensorFixture.setUserData(this);
        circle.dispose();

        super.getBody().setBullet(true);

        setProfile(new Profile());

    }


    public void velocityLimit() {
        Vector2 vel = this.getBody().getLinearVelocity();

        if (Math.abs(vel.x) > this.MAX_VELOCITY){
            vel.x = Math.signum(vel.x) * this.MAX_VELOCITY;
            this.setVelocity(new Vector2(vel.x, vel.y));
        }else if(Math.abs(vel.y) > this.MAX_VELOCITY*2) {
            vel.y=  Math.signum(vel.y) * this.MAX_VELOCITY*2;
            this.setVelocity(new Vector2(vel.x, vel.y));
        }
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public Vector2 getVelocity() {
        return super.getBody().getLinearVelocity();
    }

    public void setVelocity(Vector2 velocity) {
         super.getBody().setLinearVelocity(velocity);
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }

    public Fixture getHeroPhysicsFixture() {
        return heroPhysicsFixture;
    }

    public Fixture getHeroSensorFixture() {
        return heroSensorFixture;
    }

    public boolean isGround() {
        return onGround;
    }

    public void setGround(boolean onGround) {
        this.onGround = onGround;
    }

    public HashSet<Fixture> getGrounContacts() {
        return grounContacts;
    }

    public void setGrounContacts(HashSet<Fixture> grounContacts) {
        this.grounContacts = grounContacts;
    }


    public void hurt() {
        hurt = true;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return
                "onGround=" + onGround +
                "\nstate=" + state +
                "\nfacingLeft=" + facingLeft +
                "\nisActive= " + getBody().isActive() +
                "\nisSleepingAllowed= " + getBody().isSleepingAllowed() +
                "\nisAwake=" + getBody().isAwake()+
                "\nAngle=" + getBody().getAngle()+
                "\nAngularDamping=" + getBody().getAngularDamping()+
                "\nAngularVelocity=" + getBody().getAngularVelocity()+
                "\nGravityScale=" + getBody().getGravityScale()+
                "\nInertia=" + getBody().getInertia()+
                "\nMasa=" + getBody().getMass()+
                "\nPeso=" + getBody().getMass()*9.8f+
                "\nisBullet=" + getBody().isBullet()+
                "\nisFixedRotation=" + getBody().isFixedRotation()+
                "\nLinearDamping=" + getBody().getLinearDamping()+
                "\nLinearVelocity=" + getBody().getLinearVelocity().toString()+
                "\nPosition=" + getBody().getPosition().toString()+
                "\nLocalCenter=" + getBody().getLocalCenter().toString()+
                "\nWidth=" +getWidth()+
                "\nHeight=" + getHeight()+
                "\nWorldCenter=" + getBody().getWorldCenter().toString();
    }

    @Override
    public void dispose() {
        super.dispose();
        grounContacts=null;
        heroPhysicsFixture=null;
        heroSensorFixture=null;
        profile=null;
    }
}


