package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;

import java.util.HashSet;


public class Rubentxu extends Box2DPhysicsObject {


    public enum State {
        IDLE, WALKING, JUMPING, DYING, FALL,SWIMMING
    }

    public final static float MAX_VELOCITY = 4f;
    public final static float JUMP_FORCE = 14.5f;
    private boolean onGround = false;
    private State state = State.IDLE;
    boolean facingLeft = true;
    private float killVelocity;
    private Boolean hurt;
    private float hurtVelocityY = 10f;
    private float hurtVelocityX = 6f;
    private float springOffEnemy = -1f;
    private HashSet<Fixture> grounContacts;
    private float combinedGroundAngle = 0f;
    private Fixture rubenPhysicsFixture;
    private Fixture rubenSensorFixture;

    public Rubentxu(World physics) {
        super("Heroe", GRUPOS.HEROES, physics);
        setGrounContacts(new HashSet<Fixture>());
    }

    public Rubentxu(World physics, float x, float y, float width, float height) {
        super("Heroe", GRUPOS.HEROES, physics, x, y, width, height, 0);
        setGrounContacts(new HashSet<Fixture>());
        createRubenxu( x, y, width, height);
    }

    public void createRubenxu( float x, float y, float width, float height) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.x = x;
        def.position.y = y;
        setBody(box2D.createBody(def));
        getBody().setFixedRotation(true);
        getBody().setUserData(this);


        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width,height);

        rubenPhysicsFixture = super.getBody().createFixture(poly,1);
        rubenPhysicsFixture.setUserData(this);
        poly.dispose();


        CircleShape circle = new CircleShape();
        circle.setRadius(width);
        circle.setPosition(new Vector2(0, -height*0.9f));
        rubenSensorFixture = super.getBody().createFixture(circle, 0);
        rubenSensorFixture.setSensor(true);
        rubenSensorFixture.setUserData(this);
        circle.dispose();

        super.getBody().setBullet(true);

    }


    public void velocityLimit() {
        Vector2 vel = this.getBody().getLinearVelocity();

        if (Math.abs(vel.x) > this.MAX_VELOCITY){
            vel.x = Math.signum(vel.x) * this.MAX_VELOCITY;
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

    public Fixture getRubenPhysicsFixture() {
        return rubenPhysicsFixture;
    }

    public Fixture getRubenSensorFixture() {
        return rubenSensorFixture;
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
}


