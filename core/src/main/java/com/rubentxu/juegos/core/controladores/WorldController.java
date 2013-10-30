package com.rubentxu.juegos.core.controladores;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.rubentxu.juegos.core.managers.PlatformManager;
import com.rubentxu.juegos.core.managers.RubentxuManager;
import com.rubentxu.juegos.core.modelo.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.Box2DPhysicsObject.GRUPOS;


public class WorldController implements ContactListener, ContactFilter {


    private World physics;
    private RubentxuManager rubenManager;
    private PlatformManager platformManager;

    public static java.util.Map<WorldController.Keys, Boolean> keys = new java.util.HashMap<WorldController.Keys, Boolean>();

    static {
        keys.put(WorldController.Keys.LEFT, false);
        keys.put(WorldController.Keys.RIGHT, false);
        keys.put(WorldController.Keys.JUMP, false);
        keys.put(WorldController.Keys.FIRE, false);
    }


    public void leftPressed() {
        keys.get(keys.put(WorldController.Keys.LEFT, true));
    }

    public void rightPressed() {
        keys.get(keys.put(WorldController.Keys.RIGHT, true));
    }

    public void jumpPressed() {
        keys.get(keys.put(WorldController.Keys.JUMP, true));
    }

    public void firePressed() {
        keys.get(keys.put(WorldController.Keys.FIRE, true));
    }

    public void leftReleased() {
        keys.get(keys.put(WorldController.Keys.LEFT, false));
    }

    public void rightReleased() {
        keys.get(keys.put(WorldController.Keys.RIGHT, false));
    }

    public void jumpReleased() {
        keys.get(keys.put(WorldController.Keys.JUMP, false));
        //jumpingPressed = false;
    }

    public void fireReleased() {
        keys.get(keys.put(WorldController.Keys.FIRE, false));
    }

    /**
     * The main update method *
     */
    public void update(float delta) {
        getRubenManager().update(delta);
        getPlatformManager().update(delta);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        if ((GRUPOS.HEROES == ((Box2DPhysicsObject) contact.getFixtureA().getUserData()).getGrupo())
                || GRUPOS.HEROES == ((Box2DPhysicsObject) contact.getFixtureB().getUserData()).getGrupo()) {
            getRubenManager().handlePreSolve(contact, oldManifold);

        }
        getPlatformManager().handlePreSolve(contact, oldManifold);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        if ((GRUPOS.HEROES == ((Box2DPhysicsObject) contact.getFixtureA().getUserData()).getGrupo())
                || GRUPOS.HEROES == ((Box2DPhysicsObject) contact.getFixtureB().getUserData()).getGrupo()) {
            getRubenManager().handlePostSolve(contact, impulse);

        }
    }

    @Override
    public void beginContact(Contact contact) {
        if ((GRUPOS.HEROES == ((Box2DPhysicsObject) contact.getFixtureA().getUserData()).getGrupo())
                || GRUPOS.HEROES == ((Box2DPhysicsObject) contact.getFixtureB().getUserData()).getGrupo()) {
            getRubenManager().handleBeginContact(contact);

        }
    }

    @Override
    public void endContact(Contact contact) {
        if ((GRUPOS.HEROES == ((Box2DPhysicsObject) contact.getFixtureA().getUserData()).getGrupo())
                || GRUPOS.HEROES == ((Box2DPhysicsObject) contact.getFixtureB().getUserData()).getGrupo()) {
            getRubenManager().handleEndContact(contact);
        }
    }

    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return true;
    }

    public void dispose() {
        setRubenManager(null);
        setPlatformManager(null);

    }


    public RubentxuManager getRubenManager() {
        return rubenManager;
    }

    public void setRubenManager(RubentxuManager rubenManager) {
        this.rubenManager = rubenManager;
    }

    public PlatformManager getPlatformManager() {
        return platformManager;
    }

    public void setPlatformManager(PlatformManager platformManager) {
        this.platformManager = platformManager;
    }

    public static enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }

}
