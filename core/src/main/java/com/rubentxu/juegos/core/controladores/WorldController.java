package com.rubentxu.juegos.core.controladores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Disposable;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.managers.AbstractWorldManager;
import com.rubentxu.juegos.core.managers.world.CheckPointManager;
import com.rubentxu.juegos.core.managers.world.EnemyManager;
import com.rubentxu.juegos.core.managers.world.HeroManager;
import com.rubentxu.juegos.core.managers.world.ItemsManager;
import com.rubentxu.juegos.core.managers.world.PlatformManager;
import com.rubentxu.juegos.core.managers.world.WaterManager;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPO;

import java.util.ArrayList;
import java.util.List;


public class WorldController implements ContactListener, ContactFilter ,Disposable{

    private World world;
    private HeroManager heroManager;
    private PlatformManager platformManager;
    private WaterManager waterManager;
    private EnemyManager enemyManager;
    private ItemsManager itemsManager;
    private CheckPointManager checkPointManager;
    private List<Box2DPhysicsObject> destroy=new ArrayList<Box2DPhysicsObject>();

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


    public WorldController(DreamsGame game, World world) {
        this.world=world;
        world.getPhysics().setContactListener(this);
        heroManager = new HeroManager();
        platformManager = new PlatformManager();
        waterManager = new WaterManager();
        enemyManager = new EnemyManager();
        itemsManager= new ItemsManager();
        checkPointManager = new CheckPointManager();

        itemsManager.addObserver(game.getProfileManager());
        itemsManager.addObserver(game.getAudioManager());
        heroManager.addObserver(game.getProfileManager());
        heroManager.addObserver(game.getAudioManager());

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
        for(Box2DPhysicsObject e: world.getEntities()) {
            if (e.getState().equals(Box2DPhysicsObject.BaseState.DESTROY)){
                destroy.add(e);
            } else {
                AbstractWorldManager manager =getManager(e.getGrupo());
                if(manager!=null) manager.update(delta,e);
            }
        }
        for(Box2DPhysicsObject d:destroy){
            Gdx.app.log(Constants.LOG,"Destroy entity :" + d.getGrupo());
            world.destroyEntity(d);
        }
        destroy.clear();

        world.getPhysics().step(delta, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        contact.resetFriction();
        AbstractWorldManager managerA=getManager(((Box2DPhysicsObject) contact.getFixtureA().getUserData()).getGrupo());
        AbstractWorldManager managerB=getManager(((Box2DPhysicsObject) contact.getFixtureB().getUserData()).getGrupo());

        if(managerA!=null) managerA.handlePreSolve(contact, oldManifold);
        if(managerB!=null) managerB.handlePreSolve(contact, oldManifold);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        AbstractWorldManager managerA=getManager(((Box2DPhysicsObject) contact.getFixtureA().getUserData()).getGrupo());
        AbstractWorldManager managerB=getManager(((Box2DPhysicsObject) contact.getFixtureB().getUserData()).getGrupo());

        if(managerA!=null) managerA.handlePostSolve(contact, impulse);
        if(managerB!=null) managerB.handlePostSolve(contact, impulse);

    }

    public AbstractWorldManager getManager(GRUPO grupo){
        switch (grupo){
            case ENEMY:
                return enemyManager;

            case ITEMS:
                return itemsManager;
            case FLUID:
                return waterManager;
            case HERO:
                return heroManager;
            case PLATFORM:
                break;
            case MOVING_PLATFORM:
                return platformManager;
            case CHECKPOINT:
                return checkPointManager;
            case SENSOR:
                break;
            case STATIC:
                break;

            default:
                break;
        }
        return null;
    }

    @Override
    public void beginContact(Contact contact) {
        AbstractWorldManager managerA=getManager(((Box2DPhysicsObject) contact.getFixtureA().getUserData()).getGrupo());
        AbstractWorldManager managerB=getManager(((Box2DPhysicsObject) contact.getFixtureB().getUserData()).getGrupo());

        if(managerA!=null) managerA.handleBeginContact(contact);
        if(managerB!=null) managerB.handleBeginContact(contact);

    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureA() == null || contact.getFixtureB()==null ) return;

        AbstractWorldManager managerA=getManager(((Box2DPhysicsObject) contact.getFixtureA().getUserData()).getGrupo());
        AbstractWorldManager managerB=getManager(((Box2DPhysicsObject) contact.getFixtureB().getUserData()).getGrupo());

        if(managerA!=null) managerA.handleEndContact(contact);
        if(managerB!=null) managerB.handleEndContact(contact);

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
        checkPointManager=null;
    }
}
