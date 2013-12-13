package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Path;

import java.util.HashSet;


public class Enemy extends Box2DPhysicsObject {

    public enum State {
        IDLE, WALKING, JUMPING, DYING, FALL
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
    private Fixture enemyPhysicsFixture;
    private Fixture enemySensorFixture;
    private Path path;

    public Enemy(World physics) {
        super("Enemigo", GRUPOS.ENEMIGOS, physics);
        setGrounContacts(new HashSet<Fixture>());
    }

    public Enemy(World physics, float x, float y, float width, float height) {
        super("Enemigo", GRUPOS.ENEMIGOS, physics, x, y, width, height, 0);
        setGrounContacts(new HashSet<Fixture>());
        createEnemy( x, y, width, height);
    }

    public void createEnemy( float x, float y, float width, float height) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.x = x;
        def.position.y = y;
        setBody(box2D.createBody(def));
        getBody().setFixedRotation(true);
        getBody().setUserData(this);

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width,height);

        enemyPhysicsFixture = super.getBody().createFixture(poly,1);
        enemyPhysicsFixture.setUserData(this);
        poly.dispose();

        CircleShape circle = new CircleShape();
        circle.setRadius(width);
        circle.setPosition(new Vector2(0, -height*0.9f));
        enemySensorFixture = super.getBody().createFixture(circle, 0);
        enemySensorFixture.setSensor(true);
        enemySensorFixture.setUserData(this);
        circle.dispose();

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

    public Fixture getEnemyPhysicsFixture() {
        return enemyPhysicsFixture;
    }

    public Fixture getEnemySensorFixture() {
        return enemySensorFixture;
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

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
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
                "\nMass=" + getBody().getMass()+
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


