package com.rubentxu.juegos.core.modelo.interfaces;


import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.Body;

public interface IBox2DPhysicsObject {

    public float getXBodyA();

    public void setXBodyA(float value);

    public float getYBodyA();

    public void setYBodyA(float value);

    public float getRotationBodyA();

    public float getWidthBodyA();

    public float getHeightBodyA();

    public Body getBodyA();

    public ParticleEffect getEffect();

    public void setEffect(ParticleEffect effect);

}
