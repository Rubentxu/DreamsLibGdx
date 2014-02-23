package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.State;

import java.util.HashSet;


public class Hero extends Box2DPhysicsObject implements Disposable {


    public enum StateHero implements State {
        IDLE, WALKING, JUMPING, DYING, FALL,SWIMMING, PROPULSION, HURT ,HIT, DEAD, WIN
    }

    public enum StatePos { ONGROUND, INWATER, ONAIR }


    // States
    private StatePos statePos = StatePos.ONGROUND;
    boolean facingLeft = true;

    public final static float MAX_VELOCITY = 4f;
    public final static float JUMP_FORCE = 14.5f;
    private HashSet<Fixture> grounContacts;
    private Fixture heroPhysicsFixture;
    private Fixture heroSensorFixture;
    private ParticleEffect particleEffect;

    private Profile profile;

    public Hero(World physics) {
        super("Heroe", GRUPO.HERO, physics);
        setGrounContacts(new HashSet<Fixture>());
    }

    public Hero(World physics, float x, float y, float width, float height) {
        super("Heroe", GRUPO.HERO, physics);
        setGrounContacts(new HashSet<Fixture>());
        createHero(x, y, width, height);
        setState(StateHero.IDLE);       }


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

    public Fixture getHeroPhysicsFixture() {
        return heroPhysicsFixture;
    }

    public Fixture getHeroSensorFixture() {
        return heroSensorFixture;
    }


    public HashSet<Fixture> getGrounContacts() {
        return grounContacts;
    }

    public void setGrounContacts(HashSet<Fixture> grounContacts) {
        this.grounContacts = grounContacts;
    }


    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public StatePos getStatePos() {
        return statePos;
    }

    public void setStatePos(StatePos statePos) {
        this.statePos = statePos;
    }

    @Override
    public ParticleEffect getEffect() {
        return particleEffect;
    }

    @Override
    public void setEffect(ParticleEffect effect) {
        this.particleEffect = effect;
    }


    @Override
    public String toString() {
        return
                "StatePos=" + statePos +
                "\nstate=" + getState() +
                "\nfacingLeft=" + facingLeft +
                "\nGravityScale=" + getBody().getGravityScale()+
                "\nInertia=" + getBody().getInertia()+
                "\nMasa=" + getBody().getMass()+
                "\nPeso=" + getBody().getMass()*9.8f+
                "\nisFixedRotation=" + getBody().isFixedRotation()+
                "\nLinearDamping=" + getBody().getLinearDamping()+
                "\nLinearVelocity=" + getBody().getLinearVelocity().toString()+
                "\nPosition=" + getBody().getPosition().toString()+
                "\nSizeGroundContact=" + grounContacts.size()+
                "\nWidth=" +getWidth()+
                "\nHeight=" + getHeight()+
                "\nTimeState=" + getStateTime();
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


