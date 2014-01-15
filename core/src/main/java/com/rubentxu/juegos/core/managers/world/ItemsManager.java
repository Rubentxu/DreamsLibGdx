package com.rubentxu.juegos.core.managers.world;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rubentxu.juegos.core.managers.interfaces.AbstractWorldManager;
import com.rubentxu.juegos.core.modelo.World;


public class ItemsManager extends AbstractWorldManager {


    public ItemsManager(World world) {
        super(world);
    }

    @Override
    public void handleBeginContact(Contact contact) {

    }

    @Override
    public void handleEndContact(Contact contact) {

    }

    @Override
    public void handlePostSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public void handlePreSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public boolean handleShouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return false;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void dispose() {

    }
}
