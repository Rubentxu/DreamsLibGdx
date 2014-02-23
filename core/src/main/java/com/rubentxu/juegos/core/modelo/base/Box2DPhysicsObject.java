package com.rubentxu.juegos.core.modelo.base;


import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.rubentxu.juegos.core.modelo.interfaces.IBox2DPhysicsObject;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DUtils;

public class Box2DPhysicsObject implements IBox2DPhysicsObject, Disposable {

    public static enum GRUPO {
        HERO((short) 0x0001), ENEMY((short) 0x0002), PLATFORM((short) 0x0004), MOVING_PLATFORM((short) 0x0008),
        ITEMS((short) 0x0010), SENSOR((short) 0x0020), STATIC((short) 0x0040), FLUID((short) 0x0080);
        // 256= 0x0100 , 512=0x0200, 1024=0x0400

        private final short category;

        GRUPO(short category) {
            this.category = category;

        }

        public short getCategory() {
            return category;
        }
    }

    public enum BaseState implements State{
        INITIAL, DEFAULT, DELETE
    }

    public static final short MASK_HERO = (short) ~GRUPO.HERO.category;
    public static final short MASK_ENEMY = (short) (GRUPO.HERO.category | GRUPO.STATIC.category);
    public static final short MASK_FLUID = (short) ~GRUPO.FLUID.category;
    public static final short MASK_MOVING_PLATFORM = (short) ~GRUPO.MOVING_PLATFORM.category;
    public static final short MASK_ITEMS = (short) (GRUPO.HERO.category | GRUPO.STATIC.category);
    public static final short MASK_STATIC = -1;


    protected com.badlogic.gdx.physics.box2d.World box2D;
    protected Body body;

    protected GRUPO grupo;
    protected String nombre;
    protected boolean isFlaggedForDelete = false;
    private float stateTime;
    private State state= BaseState.INITIAL;
    private boolean changedStatus=false;
    private boolean facingLeft = false;

    public State getState(){
        return state;
    }

    public void setState(State state){
        if(this.state.equals(state)){
            changedStatus=false;
            return;
        }
        this.state = state;
        stateTime=0;
        changedStatus=true;
    }

    public Box2DPhysicsObject(String nombre, GRUPO grupo, Body body) {
        this.nombre = nombre;
        this.grupo = grupo;
        this.body = body;
    }

    public Box2DPhysicsObject(String nombre, GRUPO grupo, World physics) {
        this.nombre = nombre;
        this.grupo = grupo;
        this.box2D = physics;
    }



    @Override
    public float getX() {
        return getBody().getPosition().x;
    }

    @Override
    public void setX(float value) {
        getBody().getPosition().x = value;
    }

    @Override
    public float getY() {
        return getBody().getPosition().y;

    }

    @Override
    public void setY(float value) {
        getBody().getPosition().y = value;
    }


    @Override
    public float getRotation() {
        return (float) (getBody().getAngle() * 180 / Math.PI);

    }

    @Override
    public float getWidth() {
        return Box2DUtils.width(getBody());
    }


    @Override
    public float getHeight() {
        return Box2DUtils.height(getBody());
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public ParticleEffect getEffect() {
        return null;
    }

    @Override
    public void setEffect(ParticleEffect effect) {

    }

    public void setBody(Body body) {
        this.body = body;
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
        return body.getLinearVelocity();

    }

    public void setVelocity(Vector2 velocity) {
        body.setLinearVelocity(velocity);
    }

    public boolean isFlaggedForDelete() {
        return isFlaggedForDelete;
    }

    public void setFlaggedForDelete(boolean isFlaggedForDelete) {
        this.isFlaggedForDelete = isFlaggedForDelete;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public boolean isChangedStatus() {
        return changedStatus;
    }


    @Override
    public void dispose() {
        body = null;
        grupo = null;
    }

}
