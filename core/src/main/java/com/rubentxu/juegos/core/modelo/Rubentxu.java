package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.rubentxu.juegos.core.modelo.interfaces.IBox2DPhysicsObject;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.*;


public class Rubentxu extends Box2DPhysicsObject {


    public enum State {
        IDLE, WALKING, JUMPING, DYING, FALL
    }

    State state = State.IDLE;
    boolean facingLeft = true;

    private Fixture rubenPhysicsFixture;
    private Fixture rubenSensorFixture;


    public final static float MAX_VELOCITY = 7f;
    public final static float JUMP_FORCE = 58f;
    private boolean grounded=true;


    public Rubentxu(World world, float x, float y, float width, float height) {
        super("Heroe", grupos.HEROES,world.getPhysics(),x,y,width,height,0);
        createRubenxu(world, x, y, width, height);
        preContactCallEnabled = true;
        beginContactCallEnabled = true;
        endContactCallEnabled = true;
    }

    public void createRubenxu(World world, float x, float y, float width, float height) {
        defineBody();
        createBody();
        body.setFixedRotation(true);
        body.setSleepingAllowed(false);

        PolygonShape poly= (PolygonShape) createShape(width,height,0);
        rubenPhysicsFixture = createFixture(defineFixture(poly));
        rubenPhysicsFixture.setRestitution(0);
        poly.dispose();

        PolygonShape sensor = (PolygonShape) createShape(width*0.9f, height/5,new Vector2(0, -height*0.9f),0);
        rubenSensorFixture = createFixture(defineFixture(sensor));
        rubenSensorFixture.setRestitution(0);
        sensor.dispose();

        body.setBullet(true);

    }


    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public Vector2 getVelocity() {
        return body.getLinearVelocity();
    }


    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }

    public Body getBody() {
        return body;
    }

    public Fixture getRubenPhysicsFixture() {
        return rubenPhysicsFixture;
    }

    public Fixture getRubenSensorFixture() {
        return rubenSensorFixture;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }

    @Override
    public void handlePreSolve(Contact contact, Manifold oldManifold) {


        IBox2DPhysicsObject otro= (IBox2DPhysicsObject) ((this == contact.getFixtureA().getBody().getUserData()) ?
                        contact.getFixtureB().getBody().getUserData() : contact.getFixtureA().getBody().getUserData());

        float heroTop= getY();
        float objectBottom= otro.getY() + (otro.getHeight()/2);

        if(objectBottom<heroTop) contact.setEnabled(false);

    }




}


