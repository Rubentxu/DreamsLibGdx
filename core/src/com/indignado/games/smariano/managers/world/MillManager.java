package com.indignado.games.smariano.managers.world;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.indignado.games.smariano.managers.AbstractWorldManager;
import com.indignado.games.smariano.modelo.Hero;
import com.indignado.games.smariano.modelo.Item;
import com.indignado.games.smariano.modelo.Mill;
import com.indignado.games.smariano.modelo.base.Box2DPhysicsObject;
import com.indignado.games.smariano.modelo.base.Box2DPhysicsObject.GRUPO;


public class MillManager extends AbstractWorldManager {


    @Override
    public void handleBeginContact(Contact contact) {
        if (getHero(contact) != null) {

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

    public Item getMill(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (box2dPhysicsA.getGrupo().equals(GRUPO.MILL)) {
            return (Item) box2dPhysicsA;
        } else if (box2dPhysicsB.getGrupo().equals(GRUPO.MILL)) {
            return (Item) box2dPhysicsB;
        } else {
            return null;
        }
    }

    private void enableMotor(Mill mill) {
        ((RevoluteJoint) mill.getJoint()).enableMotor(true);
    }

    private void disableMotor(Mill mill) {
        ((RevoluteJoint) mill.getJoint()).enableMotor(false);
    }
}
