package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Rubentxu {

    public enum State {
        IDLE, WALKING, JUMPING, DYING
    }

    public static final float SIZE = 0.8f; // half a unit

    Vector2 position = new Vector2();
    Vector2 acceleration = new Vector2();
    Vector2 velocity = new Vector2();
    Rectangle bounds = new Rectangle();
    State state = State.IDLE;
    boolean facingLeft = true;
    float stateTime = 0;
    boolean longJump = false;

    public Rubentxu(Vector2 position) {
        this.position = position;
        this.bounds.height = SIZE;
        this.bounds.width = SIZE/3;
        this.bounds.x = position.x;
        this.bounds.y = position.y;
    }


    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }

    public float getStateTime() {
        return stateTime;
    }

    public boolean isLongJump() {
        return longJump;
    }


    public void setLongJump(boolean longJump) {
        this.longJump = longJump;
    }


    public void setPosition(Vector2 position) {
        this.position = position;
        this.bounds.setX(position.x-SIZE/2);
        this.bounds.setY(position.y);
    }


    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }


    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }


    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }


    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }


    public void update(float delta) {
//		position.add(velocity.tmp().mul(delta));
//		bounds.x = position.x;
//		bounds.y = position.y;
        stateTime += delta;
    }

}


