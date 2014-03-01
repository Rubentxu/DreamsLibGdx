package com.rubentxu.juegos.core.managers.world;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.rubentxu.juegos.core.managers.AbstractWorldManager;
import com.rubentxu.juegos.core.modelo.CheckPoint;
import com.rubentxu.juegos.core.modelo.Hero;
import com.rubentxu.juegos.core.modelo.Item;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPO;


public class CheckPointManager extends AbstractWorldManager {


    @Override
    public void handleBeginContact(Contact contact) {
        if(getHero(contact)!=null) {
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
    public void update(float delta,Box2DPhysicsObject entity) {

    }

    @Override
    public void dispose() {

    }

    public Hero getHero(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (box2dPhysicsA.getGrupo().equals(GRUPO.HERO)) {
            return (Hero) box2dPhysicsA;
        } else if(box2dPhysicsB.getGrupo().equals(GRUPO.HERO)){
            return (Hero) box2dPhysicsB;}
        else {
            return null;
        }
    }

    public Item getMill(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (box2dPhysicsA.getGrupo().equals(GRUPO.CHECKPOINT)) {
            return (Item) box2dPhysicsA;
        } else if(box2dPhysicsB.getGrupo().equals(GRUPO.CHECKPOINT)){
            return (Item) box2dPhysicsB;}
        else {
            return null;
        }
    }

    private void enableMotor(CheckPoint checkPoint){
        ((PrismaticJoint)checkPoint.getJoint()).enableMotor(true);
    }

    private void disableMotor(CheckPoint checkPoint){
        ((PrismaticJoint)checkPoint.getJoint()).enableMotor(false);
    }
}
