package com.indignado.games.smariano.managers.world;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.indignado.games.smariano.managers.AbstractWorldManager;
import com.indignado.games.smariano.modelo.Hero;
import com.indignado.games.smariano.modelo.Item;
import com.indignado.games.smariano.modelo.base.Box2DPhysicsObject;
import com.indignado.games.smariano.modelo.base.Box2DPhysicsObject.GRUPO;
import com.indignado.games.smariano.modelo.base.State;


public class ItemsManager extends AbstractWorldManager {


    @Override
    public void handleBeginContact(Contact contact) {
        if (getHero(contact) != null) {
            Item item = getItem(contact);
            State state = Box2DPhysicsObject.BaseState.DESTROY;
            if (item.setState(state)) notifyObservers(state, item);
        }
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
    public void update(float delta, Box2DPhysicsObject entity) {

    }

    @Override
    public void dispose() {

    }

    public Hero getHero(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (box2dPhysicsA.getGrupo().equals(GRUPO.HERO)) {
            return (Hero) box2dPhysicsA;
        } else if (box2dPhysicsB.getGrupo().equals(GRUPO.HERO)) {
            return (Hero) box2dPhysicsB;
        } else {
            return null;
        }
    }

    public Item getItem(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (box2dPhysicsA.getGrupo().equals(GRUPO.ITEMS)) {
            return (Item) box2dPhysicsA;
        } else if (box2dPhysicsB.getGrupo().equals(GRUPO.ITEMS)) {
            return (Item) box2dPhysicsB;
        } else {
            return null;
        }
    }
}
