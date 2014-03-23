package com.rubentxu.juegos.core.modelo.base;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.rubentxu.juegos.core.modelo.interfaces.IBox2DPhysicsObject;

public class Box2DPhysicsObject implements IBox2DPhysicsObject, Disposable {

    public static enum GRUPO {
        HERO((short) 0x0001), ENEMY((short) 0x0002), PLATFORM((short) 0x0004), MOVING_PLATFORM((short) 0x0008),
        ITEMS((short) 0x0010), SENSOR((short) 0x0020), STATIC((short) 0x0040), FLUID((short) 0x0080), MILL((short) 0x0100), CHECKPOINT((short) 0x0200);
        //  1024=0x0400

        private final short category;

        GRUPO(short category) {
            this.category = category;

        }

        public short getCategory() {
            return category;
        }
    }

    public enum BaseState implements State {
        INITIAL, DEFAULT, DESTROY, HURT(1f), HIT(1f), DEAD(1f) ;

        protected float stateTimeLimit;

        BaseState(){
            this.stateTimeLimit = 0.0f;
        }

        BaseState(float stateTimeLimit){
            this.stateTimeLimit =stateTimeLimit;
        }

        @Override
        public float getStateTimeMin(){
            return this.stateTimeLimit;
        }
    }

    public static final short MASK_HERO = (short) ~GRUPO.HERO.category;
    public static final short MASK_ENEMY = (short) (GRUPO.HERO.category | GRUPO.STATIC.category);
    public static final short MASK_FLUID = (short) ~GRUPO.FLUID.category;
    public static final short MASK_MOVING_PLATFORM = (short) ~GRUPO.MOVING_PLATFORM.category;
    public static final short MASK_ITEMS = (short) (GRUPO.HERO.category | GRUPO.STATIC.category);
    public static final short MASK_INTERACTIVE = (short) (GRUPO.HERO.category);
    public static final short MASK_STATIC = -1;


    protected com.badlogic.gdx.physics.box2d.World box2D;
    protected Body bodyA;
    private final Vector2 originBodyA = new Vector2(0, 0);
    private final Vector2 scaleBodyA = new Vector2(1, 1);
    private float widthBodyA;
    private float heightBodyA;
    protected GRUPO grupo;
    protected String nombre;
    private float stateTime;
    private State state = BaseState.DEFAULT;

    private boolean facingLeft = false;

    public State getState() {
        return state;
    }

    public boolean setState(State state) {
        if (!this.state.equals(state)) {
            if (this.getStateTime() < this.state.getStateTimeMin()) {
                return false;
            } else {
                this.state = state;
                stateTime = 0;
                return true;
            }

        } else {
            return false;
        }
    }

    public float getStateTime() {
        return stateTime;
    }

    public boolean setStateTime(float stateTime) {
        boolean check=false;
        if(this.stateTime< this.state.getStateTimeMin() && stateTime > this.state.getStateTimeMin()) check= true;
        this.stateTime = stateTime;
        return check;
    }

    public Box2DPhysicsObject(String nombre, GRUPO grupo, Body bodyA) {
        this.nombre = nombre;
        this.grupo = grupo;
        this.bodyA = bodyA;

    }

    public Box2DPhysicsObject(String nombre, GRUPO grupo, World physics) {
        this.nombre = nombre;
        this.grupo = grupo;
        this.box2D = physics;
    }


    @Override
    public float getXBodyA() {
        return getBodyA().getPosition().x;
    }

    @Override
    public void setXBodyA(float value) {
        getBodyA().getPosition().x = value;
    }

    @Override
    public float getYBodyA() {
        return getBodyA().getPosition().y;

    }

    @Override
    public void setYBodyA(float value) {
        getBodyA().getPosition().y = value;
    }


    @Override
    public float getRotationBodyA() {
        return (float) (bodyA.getAngle() * 180 / Math.PI);

    }

    @Override
    public float getWidthBodyA() {
        return widthBodyA;
    }


    @Override
    public float getHeightBodyA() {
        return heightBodyA;
    }

    @Override
    public void setWidthBodyA(float widthBodyA) {
        this.widthBodyA = widthBodyA;
    }

    @Override
    public void setHeightBodyA(float heightBodyA) {
        this.heightBodyA = heightBodyA;
    }


    @Override
    public Body getBodyA() {
        return bodyA;
    }

    public void setBodyA(Body bodyA) {
        this.bodyA = bodyA;
    }

    public GRUPO getGrupo() {
        return grupo;
    }

    public void setGrupo(GRUPO grupo) {
        this.grupo = grupo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Vector2 getVelocity() {
        return bodyA.getLinearVelocity();

    }

    public void setVelocity(Vector2 velocity) {
        bodyA.setLinearVelocity(velocity);
    }


    public Vector2 getOriginBodyA() {
        return originBodyA;
    }


    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public Vector2 getScaleBodyA() {
        return scaleBodyA;
    }


    @Override
    public void dispose() {
        bodyA = null;
        grupo = null;
    }

}
