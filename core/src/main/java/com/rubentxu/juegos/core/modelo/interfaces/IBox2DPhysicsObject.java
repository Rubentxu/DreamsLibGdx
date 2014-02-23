package com.rubentxu.juegos.core.modelo.interfaces;


import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.Body;

public interface IBox2DPhysicsObject {

    public float getX();

    public void setX(float value);

    public float getY();

    public void setY(float value);

    public float getRotation();

    public float getWidth();

    public float getHeight();

    public Body getBody();

    public ParticleEffect getEffect();

    public void setEffect(ParticleEffect effect);

}
