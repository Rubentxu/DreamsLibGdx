package com.rubentxu.juegos.core.modelo.interfaces;


import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;

public interface IBox2DPhysicsCompoundObject {

    public float getXBodyB();

    public void setXBodyB(float value);

    public float getYBodyB();

    public void setYBodyB(float value);

    public float getRotationB();

    public float getWidthBodyB();

    public float getHeightBodyB();

    public Body getBodyB();

    public Joint getJoint();


}
