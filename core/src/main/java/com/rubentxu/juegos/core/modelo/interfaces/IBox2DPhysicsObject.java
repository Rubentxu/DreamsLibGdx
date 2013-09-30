package com.rubentxu.juegos.core.modelo.interfaces;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

public interface IBox2DPhysicsObject {

    public void handleBeginContact(Contact contact);
    public void handleEndContact(Contact contact);
    public void handlePreSolve(Contact contact,Manifold oldManifold);
    public void handlePostSolve(Contact contact,ContactImpulse impulse);
    public void fixedUpdate();
    public float getX();
    public void setX(float value);
    public float getY();
    public void setY(float value);
    public float getRotation();
    public void setRotation(float value);
    public float getWidth();
    public float setWidth(float value);
    public float getHeight();
    public float setHeight(float value);
    public float getDepth();
    public float getRadius();
    public float setRadius(float value);
    public Body getBody();
    

    public Boolean getBeginContactCallEnabled();
    public Boolean setBeginContactCallEnabled(Boolean value);
    public Boolean getEndContactCallEnabled();
    public Boolean setEndContactCallEnabled(Boolean value);
    public Boolean getPreContactCallEnabled();
    public Boolean setPreContactCallEnabled(Boolean value);
    public Boolean getPostContactCallEnabled();
    public Boolean setPostContactCallEnabled(Boolean value);
}
