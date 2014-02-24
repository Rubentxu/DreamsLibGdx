package com.rubentxu.juegos.core.modelo.base;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPO;
import com.rubentxu.juegos.core.modelo.interfaces.IBox2DPhysicsCompoundObject;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DUtils;

public class Box2dPhysicsCompoundObject extends Box2DPhysicsObject implements IBox2DPhysicsCompoundObject  {

    private Joint joint;
    private Body bodyB;

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
        return Box2DUtils.width(bodyB);
    }

    @Override
    public float getHeightBodyB() {
        return Box2DUtils.height(bodyB);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Body getBodyB() {
        return bodyB;
    }


}
