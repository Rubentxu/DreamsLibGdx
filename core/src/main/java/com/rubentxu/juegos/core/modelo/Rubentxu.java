package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import java.util.HashSet;


public class Rubentxu extends Box2DPhysicsObject  {


    public enum State {
        IDLE, WALKING, JUMPING, DYING, FALL
    }

    public final static float MAX_VELOCITY = 7f;
    public final static float JUMP_FORCE = 58f;
    private boolean onGround = true;
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

    public Rubentxu(World world, float x, float y, float width, float height) {
        super("Heroe", GRUPOS.HEROES, world.getPhysics(), x, y, width, height, 0);
        setGrounContacts(new HashSet<Fixture>());
        createRubenxu(world, x, y, width, height);
    }

    public void createRubenxu(World world, float x, float y, float width, float height) {
        defineBody();
        createBody();
        super.getBody().setFixedRotation(true);
        super.getBody().setSleepingAllowed(false);

        PolygonShape poly = (PolygonShape) createShape(width, height, 0);
        rubenPhysicsFixture = createFixture(defineFixture(poly));
        rubenPhysicsFixture.setRestitution(0);
        poly.dispose();

        PolygonShape sensor = (PolygonShape) createShape(width * 0.9f, height / 5, new Vector2(0, -height * 0.9f), 0);
        rubenSensorFixture = createFixture(defineFixture(sensor));
        rubenSensorFixture.setRestitution(0);
        rubenSensorFixture.setSensor(true);
        sensor.dispose();

        super.getBody().setBullet(true);

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

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }

    public Body getBody() {
        return super.getBody();
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

    public void updateCombinedGroundAngle() {
        combinedGroundAngle = 0;
        if (getGrounContacts().size() == 0) return;

        for (Fixture contact : getGrounContacts()) {
            float angle = contact.getBody().getAngle();
            float turn = (float) (45 * Math.PI / 180);
            angle = angle % turn;
            combinedGroundAngle += angle;
        }
        combinedGroundAngle /= getGrounContacts().size();
    }

    public void hurt() {
        hurt = true;

    }



}


