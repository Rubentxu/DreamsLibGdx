package com.rubentxu.juegos.core.controladores;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rubentxu.juegos.core.managers.EnemyManager;
import com.rubentxu.juegos.core.managers.PlatformManager;
import com.rubentxu.juegos.core.managers.RubentxuManager;
import com.rubentxu.juegos.core.managers.WaterManager;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPOS;


public class WorldController implements ContactListener, ContactFilter {

    private RubentxuManager rubenManager;
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

    public WorldController(World world) {
        rubenManager = new RubentxuManager(world.getRuben());
        platformManager = new PlatformManager();
        platformManager.setMovingPlatforms(world.getMovingPlatforms());
        platformManager.setPlatforms(world.getPlatforms());

        waterManager = new WaterManager();
        waterManager.setWaterSensors(world.getWaterSensors());

        enemyManager = new EnemyManager();
        enemyManager.setEnemies(world.getEnemies());
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

    public WaterManager getWaterManager() {
        return waterManager;
    }

    public void setWaterManager(WaterManager waterManager) {
        this.waterManager = waterManager;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public void setEnemyManager(EnemyManager enemyManager) {
        this.enemyManager = enemyManager;
    }

    public static enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }

    /**
     * The main update method *
     */
    public void update(float delta) {
        getRubenManager().update(delta);
        getPlatformManager().update(delta);
        waterManager.update(delta);
        enemyManager.update(delta);
    }



    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        contact.resetFriction();
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (GRUPOS.HEROES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.HEROES.equals(box2dPhysicsB.getGrupo())) {
            getRubenManager().handlePreSolve(contact, oldManifold);
        }
        if (GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsB.getGrupo())) {
            getPlatformManager().handlePreSolve(contact, oldManifold);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (GRUPOS.HEROES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.HEROES.equals(box2dPhysicsB.getGrupo())) {
            getRubenManager().handlePostSolve(contact, impulse);
        }
        if (GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsB.getGrupo())) {
            getPlatformManager().handlePostSolve(contact, impulse);
        }
        if (GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsB.getGrupo())) {
            getPlatformManager().handlePostSolve(contact, impulse);
        }

    }

    @Override
    public void beginContact(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (GRUPOS.HEROES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.HEROES.equals(box2dPhysicsB.getGrupo())) {
            getRubenManager().handleBeginContact(contact);
        }
        if (GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsB.getGrupo())) {

            getPlatformManager().handleBeginContact(contact);
        }
        if (GRUPOS.AGUA.equals(box2dPhysicsA.getGrupo()) || GRUPOS.AGUA.equals(box2dPhysicsB.getGrupo())) {
            getWaterManager().handleBeginContact(contact);
        }
        if (GRUPOS.ENEMIGOS.equals(box2dPhysicsA.getGrupo()) || GRUPOS.ENEMIGOS.equals(box2dPhysicsB.getGrupo())) {
            getEnemyManager().handleBeginContact(contact);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (GRUPOS.HEROES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.HEROES.equals(box2dPhysicsB.getGrupo())) {
            getRubenManager().handleEndContact(contact);
        }
        if (GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsA.getGrupo()) || GRUPOS.PLATAFORMAS_MOVILES.equals(box2dPhysicsB.getGrupo())) {
            getPlatformManager().handleEndContact(contact);
        }
        if (GRUPOS.AGUA.equals(box2dPhysicsA.getGrupo()) || GRUPOS.AGUA.equals(box2dPhysicsB.getGrupo())) {
            getWaterManager().handleEndContact(contact);
        }
        if (GRUPOS.ENEMIGOS.equals(box2dPhysicsA.getGrupo()) || GRUPOS.ENEMIGOS.equals(box2dPhysicsB.getGrupo())) {
            getEnemyManager().handleEndContact(contact);
        }
    }

    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return true;
    }
}
