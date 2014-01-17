package com.rubentxu.juegos.core.managers.world;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.GameState;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.controladores.WorldController.Keys;
import com.rubentxu.juegos.core.managers.interfaces.AbstractWorldManager;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Hero;
import com.rubentxu.juegos.core.modelo.Hero.State;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPO;


public class HeroManager extends AbstractWorldManager {

    private Hero hero;
    private float stillTime = 0;
    private float hurtTime = 0;

    public HeroManager(World world) {
        super(world);
        this.hero = world.getHero();
    }

    public void update(float delta) {

        Vector2 vel = hero.getVelocity();
        Vector2 pos = hero.getBody().getPosition();
        if(!hero.getState().equals(Hero.State.HURT) && hurtTime>1.2f){
            processVelocity(vel, delta);
            processContactGround();
            applyImpulses(vel, pos);
        } else if(hero.getState().equals(Hero.State.HURT) && hurtTime ==0)   {

            if(hero.getProfile().removeLive()) DreamsGame.gameState=GameState.GAME_OVER;
            System.out.println("Pierde vida: "+hero.getProfile().getLives()+" STATE GAME: "+ DreamsGame.gameState);
        }

        if(hurtTime>2)hurtTime=2;
        hurtTime += delta;

    }

    public void processVelocity(Vector2 vel,float delta) {

        if (!WorldController.keys.get(Keys.LEFT) && !WorldController.keys.get(Keys.RIGHT)) {
            stillTime += delta;
            hero.getBody().setLinearVelocity(hero.getVelocity().x * 0.9f, vel.y);
        } else {
            setStillTime(0);
        }

        hero.velocityLimit();
    }

    public void applyImpulses(Vector2 vel, Vector2 pos) {
        // apply left impulse, but only if max velocity is not reached yet
        if (WorldController.keys.get(Keys.LEFT) && vel.x > -hero.MAX_VELOCITY) {
            hero.getBody().applyLinearImpulse(-3f, 0f, pos.x, pos.y, true);
        }

        // apply right impulse, but only if max velocity is not reached yet
        if (WorldController.keys.get(Keys.RIGHT) && vel.x < hero.MAX_VELOCITY) {
            hero.getBody().applyLinearImpulse(3f, 0, pos.x, pos.y, true);
        }

        // jump, but only when grounded
        if (WorldController.keys.get(Keys.JUMP)) {
            if (hero.isGround()) {
                hero.setState(Hero.State.JUMPING);
                hero.getBody().setLinearVelocity(vel.x, 0);
                hero.getBody().setTransform(pos.x, pos.y + 0.01f, 0);
                hero.getBody().applyLinearImpulse(0, hero.JUMP_FORCE, pos.x, pos.y, true);
            }else if(!hero.getState().equals(Hero.State.JUMPING)) {
                hero.setState(Hero.State.FALL);
            }
        }
    }

    public void processContactGround() {

        if (!hero.isGround()) {
            hero.getHeroPhysicsFixture().setFriction(0f);
            hero.getHeroSensorFixture().setFriction(0f);
            if (hero.getVelocity().y <= 0 || !hero.getState().equals(Hero.State.JUMPING))
                hero.setState(Hero.State.FALL);
        } else {
            if(!hero.getState().equals(Hero.State.SWIMMING)) hero.setState(Hero.State.IDLE);
            if (WorldController.keys.get(Keys.LEFT)  ) {
                hero.setFacingLeft(true);
                if(!hero.getState().equals(Hero.State.SWIMMING)) hero.setState(Hero.State.WALKING);
            } else if (WorldController.keys.get(Keys.RIGHT) ) {
                hero.setFacingLeft(false);
                if(!hero.getState().equals(Hero.State.SWIMMING)) hero.setState(Hero.State.WALKING);
            }
            if (!WorldController.keys.get(Keys.LEFT) && !WorldController.keys.get(Keys.RIGHT) && stillTime > 1
                    && !hero.getState().equals(Hero.State.HURT)) {
                hero.getHeroPhysicsFixture().setFriction(100f);
                hero.getHeroSensorFixture().setFriction(100f);
            } else {
                hero.getHeroPhysicsFixture().setFriction(0.2f);
                hero.getHeroSensorFixture().setFriction(0.2f);
            }
        }
    }

    public Enemy getEnemy(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (box2dPhysicsA.getGrupo().equals(GRUPO.ENEMY)) {
            return (Enemy) box2dPhysicsA;
        } else if(box2dPhysicsB.getGrupo().equals(GRUPO.ENEMY)){
            return (Enemy) box2dPhysicsB;
        } else {
            return null;
        }
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

    public Boolean existSensor (Contact contact) {

        if (contact.getFixtureA() == hero.getHeroSensorFixture() ||
                contact.getFixtureB() == hero.getHeroSensorFixture()){
            return true;
        }

        return false;
    }



    @Override
    public void handleBeginContact(Contact contact) {
        //Gdx.app.log(DreamsGame.LOG, "Begin contact");

        if (getEnemy(contact)==null && contact.getFixtureA() == hero.getHeroSensorFixture()){
            hero.getGrounContacts().add(contact.getFixtureB());//A is foot so B is ground
        }

        if (getEnemy(contact)==null &&  contact.getFixtureB() == hero.getHeroSensorFixture()) {
            hero.getGrounContacts().add(contact.getFixtureA());//A is foot so B is ground
        }


        if (hero.getGrounContacts().size() > 0) {
            hero.setGround(true);
            contact.setEnabled(true);

           // Gdx.app.log(DreamsGame.LOG, "OnGroun True");
        }
        if(getEnemy(contact) != null && !hero.getState().equals(State.HURT)) {

            Vector2[] points = contact.getWorldManifold().getPoints();
            Vector2 force;
            if (points[0].x < hero.getBody().getPosition().x ) {
                force=new Vector2(4,7);
            }else {
                force=new Vector2(-4,7);
            }
            System.out.println("Fuerza colision Enemigo: "+force +" Sensor no exist");
            hero.getBody().applyLinearImpulse(force,hero.getBody().getWorldCenter(),true);

            hero.setState(Hero.State.HURT);
            hurtTime=0;
        }
    }

    @Override
    public void handleEndContact(Contact contact) {
        //Gdx.app.log(DreamsGame.LOG, "End contact");

        if (contact.getFixtureA() == hero.getHeroSensorFixture())
            hero.getGrounContacts().remove(contact.getFixtureB());//A is foot so B is ground

        if (contact.getFixtureB() == hero.getHeroSensorFixture())
            hero.getGrounContacts().remove(contact.getFixtureA());//A is foot so B is ground


        if (hero.getGrounContacts().size() == 0) {
            hero.setGround(false);

            //Gdx.app.log(DreamsGame.LOG, "OnGroun False");
        }
        if(getEnemy(contact)!=null) {
            hero.setState(Hero.State.IDLE);
        }
    }

    @Override
    public void handlePostSolve(Contact contact, ContactImpulse impulse) {
    }

    @Override
    public void handlePreSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public boolean handleShouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return true;
    }

    public void setStillTime(int stillTime) {
        this.stillTime = stillTime;
    }

    @Override
    public void dispose() {

    }
}
