package com.rubentxu.juegos.core.managers.interfaces;


import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public interface IWorldManager {

    public void handleBeginContact(Contact contact);

    public void handleEndContact(Contact contact);

    public void handlePostSolve(Contact contact, ContactImpulse impulse);

    public void handlePreSolve(Contact contact, Manifold oldManifold);

    public boolean handleShouldCollide(Fixture fixtureA, Fixture fixtureB);

    public void update(float delta);
}
