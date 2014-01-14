package com.rubentxu.juegos.core.managers.interfaces;


import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rubentxu.juegos.core.modelo.World;

public abstract class AbstractWorldManager {


    protected final World world;

    public AbstractWorldManager(World world){
           this.world=world;
        }

    public abstract void handleBeginContact(Contact contact);

    public abstract void handleEndContact(Contact contact);

    public abstract void handlePostSolve(Contact contact, ContactImpulse impulse);

    public abstract void handlePreSolve(Contact contact, Manifold oldManifold);

    public abstract boolean handleShouldCollide(Fixture fixtureA, Fixture fixtureB);

    public abstract void update(float delta);
}
