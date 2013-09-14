package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


public class Rubentxu extends Entity {


    public enum State {
        IDLE, WALKING, JUMPING, DYING
    }

    State state = State.IDLE;
    boolean facingLeft = true;

    private Fixture rubenPhysicsFixture;
    private Fixture rubenSensorFixture;
    public final float width, height;
    private Vector2 velocity = new Vector2();
    public final static float MAX_VELOCITY = 7f;
    private float stateTime=0;
    private boolean jump = false;
    private boolean grounded=true;



    public Rubentxu(World world, float x, float y, float width, float height) {
        super("Heroe", 1);
        this.width = width;
        this.height = height;
        this.setPhysics(world.getPhysics());
        createRubenxu(world, x, y, width, height);
    }

    public void createRubenxu(World world, float x, float y, float width, float height) {

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation=true ;
        body =  world.getPhysics().createBody(def);

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width, height);
        rubenPhysicsFixture = body.createFixture(poly, 1);
        poly.dispose();

        PolygonShape sensor = new PolygonShape();
        sensor.setAsBox(width, height/5,new Vector2(0, -height),0);
        rubenSensorFixture = body.createFixture(sensor, 0);

        sensor.dispose();

        body.setBullet(true);
        body.setTransform(x, y, 0);

    }


    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public Vector2 getVelocity() {
        return velocity;
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

    public void update(float delta) {



    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public Fixture getRubenPhysicsFixture() {
        return rubenPhysicsFixture;
    }

    public Fixture getRubenSensorFixture() {
        return rubenSensorFixture;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }

    @Override
    public void draw(SpriteBatch spriteBatch){
        update(Gdx.graphics.getDeltaTime());
        super.draw(spriteBatch);
    }



}


