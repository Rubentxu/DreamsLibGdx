package com.rubentxu.juegos.core.controladores;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Disposable;
import com.rubentxu.juegos.core.managers.world.EnemyManager;
import com.rubentxu.juegos.core.managers.world.HeroManager;
import com.rubentxu.juegos.core.managers.world.PlatformManager;
import com.rubentxu.juegos.core.managers.world.WaterManager;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPOS;


public class WorldController implements ContactListener, ContactFilter ,Disposable{

    private HeroManager heroManager;
    private PlatformManager platformManager;
    private WaterManager waterManager;
    private EnemyManager enemyManager;

    public static java.util.Map<WorldController.Keys, Boolean> keys = new java.util.HashMap<WorldController.Keys, Boolean>();

    static {
        keys.put(WorldController.Keys.LEFT, false);
        keys.put(WorldController.Keys.RIGHT, false);
        keys.put(WorldController.Keys.JUMP, false);
        keys.put(WorldController.Keys.FIRE, false);
    }

    public static enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }


    public WorldController(World world) {
        world.getPhysics().setContactListener(this);
        heroManager = new HeroManager(world);
        platformManager = new PlatformManager(world);
        waterManager = new WaterManager(world);
        enemyManager = new EnemyManager(world);

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
        heroManager.update(delta);
        platformManager.update(delta);
        waterManager.update(delta);
        enemyManager.update(delta);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        contact.resetFriction();
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (GRUPOS.HEROES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.HEROES.equals(box2dPhysicsB.getGrupo())) {
            heroManager.handlePreSolve(contact, oldManifold);
        }
        if (GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsB.getGrupo())) {
            platformManager.handlePreSolve(contact, oldManifold);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (GRUPOS.HEROES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.HEROES.equals(box2dPhysicsB.getGrupo())) {
            heroManager.handlePostSolve(contact, impulse);
        }
        if (GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsB.getGrupo())) {
            platformManager.handlePostSolve(contact, impulse);
        }
        if (GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsB.getGrupo())) {
            platformManager.handlePostSolve(contact, impulse);
        }

    }

    @Override
    public void beginContact(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (GRUPOS.HEROES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.HEROES.equals(box2dPhysicsB.getGrupo())) {
            heroManager.handleBeginContact(contact);
        }
        if (GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsB.getGrupo())) {

            platformManager.handleBeginContact(contact);
        }
        if (GRUPOS.AGUA.equals(box2dPhysicsA.getGrupo()) || GRUPOS.AGUA.equals(box2dPhysicsB.getGrupo())) {
            waterManager.handleBeginContact(contact);
        }
        if (GRUPOS.ENEMIGOS.equals(box2dPhysicsA.getGrupo()) || GRUPOS.ENEMIGOS.equals(box2dPhysicsB.getGrupo())) {
            enemyManager.handleBeginContact(contact);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (GRUPOS.HEROES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.HEROES.equals(box2dPhysicsB.getGrupo())) {
            heroManager.handleEndContact(contact);
        }
        if (GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsB.getGrupo())) {
            platformManager.handleEndContact(contact);
        }
        if (GRUPOS.AGUA.equals(box2dPhysicsA.getGrupo()) || GRUPOS.AGUA.equals(box2dPhysicsB.getGrupo())) {
            waterManager.handleEndContact(contact);
        }
        if (GRUPOS.ENEMIGOS.equals(box2dPhysicsA.getGrupo()) || GRUPOS.ENEMIGOS.equals(box2dPhysicsB.getGrupo())) {
            enemyManager.handleEndContact(contact);
        }
    }

    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return true;
    }


    @Override
    public void dispose() {
        heroManager=null;
        platformManager=null;
        enemyManager=null;
        waterManager=null;
    }
}
