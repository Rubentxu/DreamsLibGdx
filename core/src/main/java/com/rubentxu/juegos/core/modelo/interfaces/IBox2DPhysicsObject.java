package com.rubentxu.juegos.core.modelo.interfaces;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public interface IBox2DPhysicsObject {

    public void fixedUpdate();

    public float getX();

    public void setX(float value);

    public float getY();

    public void setY(float value);

    public float getRotation();

    public float getWidth();

    public float getHeight();

    public Body getBody();

}
