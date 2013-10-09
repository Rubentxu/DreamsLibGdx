package com.rubentxu.juegos.core.controladores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.managers.RubentxuManager;
import com.rubentxu.juegos.core.modelo.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.Box2DPhysicsObject.GRUPOS;
import com.rubentxu.juegos.core.modelo.World;


public class WorldController implements ContactListener, ContactFilter {


    public static enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }

    private World world;
    float stillTime = 0;
    private final RubentxuManager rubenManager;

    public static java.util.Map<WorldController.Keys, Boolean> keys = new java.util.HashMap<WorldController.Keys, Boolean>();

    static {
        keys.put(WorldController.Keys.LEFT, false);
        keys.put(WorldController.Keys.RIGHT, false);
        keys.put(WorldController.Keys.JUMP, false);
        keys.put(WorldController.Keys.FIRE, false);
    }

    public WorldController(com.rubentxu.juegos.core.modelo.World world) {
        this.world = world;
        world.getPhysics().setContactListener(this);
        rubenManager = new RubentxuManager(world);
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
        keys.get(keys.put(WorldController.Keys.FIRE, false));
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
        rubenManager.update(delta);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public void beginContact(Contact contact) {
        if ((GRUPOS.HEROES == ((Box2DPhysicsObject)contact.getFixtureA().getUserData()).getGrupo())){
           rubenManager.handleBeginContact(contact);
            Gdx.app.log(DreamsGame.LOG, "Begin contact");
        }

    }

    @Override
    public void endContact(Contact contact) {
        if ((GRUPOS.HEROES == ((Box2DPhysicsObject)contact.getFixtureA().getUserData()).getGrupo())){
            rubenManager.handleEndContact(contact);
            Gdx.app.log(DreamsGame.LOG, "End contact");
        }

    }

    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return false;
    }

}