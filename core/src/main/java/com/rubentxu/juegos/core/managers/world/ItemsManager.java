package com.rubentxu.juegos.core.managers.world;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rubentxu.juegos.core.managers.AbstractWorldManager;
import com.rubentxu.juegos.core.modelo.Hero;
import com.rubentxu.juegos.core.modelo.Item;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPO;


public class ItemsManager extends AbstractWorldManager {


    public ItemsManager(World world) {
        super(world);
    }

    @Override
    public void handleBeginContact(Contact contact) {
        if(getHero(contact)!=null) {
            Item item= getItem(contact);
            setChanged();
            notifyObservers(item);
            item.setFlaggedForDelete(true);
            world.addBodiesFlaggedDestroy(item.getBody());
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
    public void update(float delta) {

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

    public Item getItem(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (box2dPhysicsA.getGrupo().equals(GRUPO.ITEMS)) {
            return (Item) box2dPhysicsA;
        } else if(box2dPhysicsB.getGrupo().equals(GRUPO.ITEMS)){
            return (Item) box2dPhysicsB;}
        else {
            return null;
        }
    }
}
