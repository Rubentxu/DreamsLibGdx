package com.rubentxu.juegos.core.modelo.base;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.rubentxu.juegos.core.modelo.interfaces.IBox2DPhysicsCompoundObject;

public class Box2dPhysicsCompoundObject extends Box2DPhysicsObject implements IBox2DPhysicsCompoundObject  {

    private Joint joint;
    private Body bodyB;
    private final Vector2 originBodyB = new Vector2(0,0);
    private final Vector2 scaleBodyB = new Vector2(1,1);
    private float widthBodyB;
    private float heightBodyB;

    public Box2dPhysicsCompoundObject(String nombre, GRUPO grupo, Body bodyA,Body bodyB, Joint joint) {
        super(nombre, grupo, bodyA);
        this.bodyB=bodyB;
        this.joint=joint;
    }

    public Joint getJoint() {
        return joint;
    }

    @Override
    public float getXBodyB() {
        return bodyB.getPosition().x;
    }

    @Override
    public void setXBodyB(float value) {
        bodyB.getPosition().x = value;
    }

    @Override
    public float getYBodyB() {
        return bodyB.getPosition().y;
    }

    @Override
    public void setYBodyB(float value) {
        bodyB.getPosition().y = value;
    }

    @Override
    public float getRotationB() {
        return (float) (bodyB.getAngle() * 180 / Math.PI);
    }

    @Override
    public float getWidthBodyB() {
        return widthBodyB;
    }

    @Override
    public float getHeightBodyB() {
        return heightBodyB;
    }

    @Override
    public void setWidthBodyB(float widthBodyB) {
        this.widthBodyB = widthBodyB;
    }

    @Override
    public void setHeightBodyB(float heightBodyB) {
        this.heightBodyB = heightBodyB;
    }

    public Body getBodyB() {
        return bodyB;
    }


    public Vector2 getOriginBodyB() {
        return originBodyB;
    }

    public Vector2 getScaleBodyB() {
        return scaleBodyB;
    }

}
