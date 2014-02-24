package com.rubentxu.juegos.core.managers.world;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rubentxu.juegos.core.controladores.WorldController.Keys;
import com.rubentxu.juegos.core.managers.AbstractWorldManager;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Hero;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPO;

import static com.rubentxu.juegos.core.controladores.WorldController.keys;
import static com.rubentxu.juegos.core.modelo.Hero.StateHero.FALL;
import static com.rubentxu.juegos.core.modelo.Hero.StateHero.HURT;
import static com.rubentxu.juegos.core.modelo.Hero.StateHero.IDLE;
import static com.rubentxu.juegos.core.modelo.Hero.StateHero.JUMPING;
import static com.rubentxu.juegos.core.modelo.Hero.StateHero.PROPULSION;
import static com.rubentxu.juegos.core.modelo.Hero.StateHero.SWIMMING;
import static com.rubentxu.juegos.core.modelo.Hero.StateHero.WALKING;


public class HeroManager extends AbstractWorldManager {

    private Hero hero;
    private float stillTime = 0;
    public HeroManager(World world) {
        super(world);
        this.hero = world.getHero();
    }

    public void handleInput(Hero hero) {

        switch (hero.getStatePos()) {
            case ONGROUND:
                if (keys.get(Keys.LEFT)) {
                    hero.setState(WALKING);
                    hero.setFacingLeft(true);
                }
                if (keys.get(Keys.RIGHT)) {
                    hero.setState(WALKING);
                    hero.setFacingLeft(false);
                }

                if (!keys.get(Keys.LEFT) && !keys.get(Keys.RIGHT)) {
                    stillTime += Gdx.graphics.getDeltaTime();
                    hero.setState(IDLE);
                }
                if (keys.get(Keys.JUMP)) {
                    if(hero.getStateTime()>0.2) hero.setState(JUMPING);
                }
                handleState();
                break;
            case INWATER:
                if (keys.get(Keys.LEFT)) {
                    hero.setState(SWIMMING);
                    hero.setFacingLeft(true);
                }
                if (keys.get(Keys.RIGHT)) {
                    hero.setState(SWIMMING);
                    hero.setFacingLeft(false);
                }
                if (!keys.get(Keys.LEFT) && !keys.get(Keys.RIGHT)) {
                    stillTime += Gdx.graphics.getDeltaTime();
                    hero.setState(IDLE);
                }
                if (keys.get(Keys.JUMP)) {
                    hero.setState(PROPULSION);
                }

                handleState();
                break;
            case ONAIR:
                if (keys.get(Keys.LEFT)) {
                    hero.setFacingLeft(true);
                    applyPhysicMovingImpulse();
                }
                if (keys.get(Keys.RIGHT)) {
                    hero.setFacingLeft(false);
                    applyPhysicMovingImpulse();
                }
                if (hero.getVelocity().y <= 0 ){
                    hero.setState(FALL);
                }else{
                    hero.setState(JUMPING);
                }
                hero.getHeroPhysicsFixture().setFriction(0f);
                hero.getHeroSensorFixture().setFriction(0f);
                handleState();
                break;
        }
        hero.velocityLimit();

    }

    public void handleState() {
        if (hero.isChangedStatus()) setChanged();
        notifyObservers(hero.getState());
        Vector2 vel = hero.getVelocity();
        Vector2 pos = hero.getBodyA().getPosition();

        if (hero.getState().equals(Hero.StateHero.IDLE)) {
            stillTime += Gdx.graphics.getDeltaTime();
            hero.getBodyA().setLinearVelocity(hero.getVelocity().x * 0.9f, vel.y);
            hero.getHeroPhysicsFixture().setFriction(100f);
            hero.getHeroSensorFixture().setFriction(100f);
            hero.getEffect().allowCompletion();

        } else if (hero.getState().equals(Hero.StateHero.WALKING)) {
            applyPhysicMovingImpulse();
            stillTime = 0;
            hero.getHeroPhysicsFixture().setFriction(0.2f);
            hero.getHeroSensorFixture().setFriction(0.2f);
            if (hero.getEffect().isComplete()) hero.getEffect().reset();

        } else if (hero.getState().equals(Hero.StateHero.JUMPING)) {
            if (!hero.getStatePos().equals(Hero.StatePos.ONAIR) )
                applyPhysicJumpingImpulse(vel, pos);
            hero.getEffect().allowCompletion();

        } else if (hero.getState().equals(Hero.StateHero.PROPULSION)) {
            applyPhysicJumpingImpulse(vel, pos);
            hero.getEffect().allowCompletion();

        } else if (hero.getState().equals(Hero.StateHero.SWIMMING)) {
            applyPhysicMovingImpulse();
            hero.getEffect().allowCompletion();
        } else if (hero.getState().equals(Hero.StateHero.FALL)) {
            hero.getEffect().allowCompletion();
        }

    }


    private void applyPhysicJumpingImpulse(Vector2 vel, Vector2 pos) {
        hero.getBodyA().setLinearVelocity(vel.x, 0);
        hero.getBodyA().setTransform(pos.x, pos.y + 0.01f, 0);
        hero.getBodyA().applyLinearImpulse(0, hero.JUMP_FORCE, pos.x, pos.y, true);
    }

    private void applyPhysicMovingImpulse() {
        Vector2 vel = hero.getVelocity();
        Vector2 pos = hero.getBodyA().getPosition();

        int impulse=0;
        if (hero.isFacingLeft() && vel.x > -hero.MAX_VELOCITY) {
            impulse=-3;
        }
        if (!hero.isFacingLeft() && vel.x < hero.MAX_VELOCITY) {
            impulse=3;
        }
        if (!hero.isFacingLeft() && vel.x < 0) {
            impulse=2;
        }
        if (hero.isFacingLeft() && vel.x > 0) {
            impulse=-2;
        }
        hero.getBodyA().applyLinearImpulse(impulse, 0, pos.x, pos.y, true);
    }



 /*   public void update(float delta) {


        if(!hero.getState().equals(Hero.StateHero.HURT) && hurtTime>1.2f){
            processVelocity(vel, delta);
            processContactGround();
            applyImpulses(vel, pos);
        } else if(hero.getState().equals(Hero.StateHero.HURT) && hurtTime ==0)   {

            if(hero.getProfile().removeLive()) DreamsGame.gameState=GameStateHero.GAME_OVER;
            System.out.println("Pierde vida: "+hero.getProfile().getLives()+" STATE GAME: "+ DreamsGame.gameState);
        } else if (hurtTime>1.2f){
            hero.setState(Hero.StateHero.IDLE);
        }

        if(hurtTime>2)hurtTime=2;
        hurtTime += delta;
    }*/






    public Enemy getEnemy(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (box2dPhysicsA.getGrupo().equals(GRUPO.ENEMY)) {
            return (Enemy) box2dPhysicsA;
        } else if (box2dPhysicsB.getGrupo().equals(GRUPO.ENEMY)) {
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
        } else if (box2dPhysicsB.getGrupo().equals(GRUPO.HERO)) {
            return (Hero) box2dPhysicsB;
        } else {
            return null;
        }
    }

    public boolean isGroundGrupo(Fixture fixture){

        boolean check= ((Box2DPhysicsObject) fixture.getUserData()).getGrupo().equals(GRUPO.STATIC) ||
         ((Box2DPhysicsObject) fixture.getUserData()).getGrupo().equals(GRUPO.PLATFORM) ||
         ((Box2DPhysicsObject) fixture.getUserData()).getGrupo().equals(GRUPO.MOVING_PLATFORM);
        return check;
    }

    public Boolean existSensor(Contact contact) {

        if (contact.getFixtureA() == hero.getHeroSensorFixture() ||
                contact.getFixtureB() == hero.getHeroSensorFixture()) {
            return true;
        }

        return false;
    }


    @Override
    public void handleBeginContact(Contact contact) {

        Enemy enemy = getEnemy(contact);


        if (isGroundGrupo(contact.getFixtureB()) && contact.getFixtureA() == hero.getHeroSensorFixture()
                && !hero.getStatePos().equals(Hero.StatePos.INWATER)) {
            hero.getGrounContacts().add(contact.getFixtureB());//A is foot so B is ground
        }

        if (isGroundGrupo(contact.getFixtureA()) && contact.getFixtureB() == hero.getHeroSensorFixture()
                && !hero.getStatePos().equals(Hero.StatePos.INWATER)) {
            hero.getGrounContacts().add(contact.getFixtureA());//A is foot so B is ground
        }


        if (hero.getGrounContacts().size() > 0) {
            hero.setStatePos(Hero.StatePos.ONGROUND);
            contact.setEnabled(true);
        }

        if (enemy != null && (contact.getFixtureA() == hero.getHeroPhysicsFixture()
                || contact.getFixtureB() == hero.getHeroPhysicsFixture()) && !hero.getState().equals(Hero.StateHero.HURT) ) {

            Vector2[] points = contact.getWorldManifold().getPoints();
            Vector2 force;
            if (points[0].x < hero.getBodyA().getPosition().x) {
                force = new Vector2(6, 10);
            } else {
                force = new Vector2(-6, 10);
            }
            System.out.println("Fuerza colision Enemigo: " + force + " Sensor no exist");
            hero.getBodyA().applyLinearImpulse(force, hero.getBodyA().getWorldCenter(), true);

            hero.setState(HURT);
            setChanged();
            notifyObservers(hero.getState());
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
            if(!hero.getStatePos().equals(Hero.StatePos.INWATER))
            hero.setStatePos(Hero.StatePos.ONAIR);


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

    @Override
    public void update(float delta) {
        handleInput(hero);
        hero.setStateTime(hero.getStateTime() + delta);
        hero.getEffect().setPosition(hero.getXBodyA(), hero.getYBodyA() - hero.getHeightBodyA()/2.2f);
        hero.getEffect().update(delta);
    }

    public void setStillTime(int stillTime) {
        this.stillTime = stillTime;
    }

    @Override
    public void dispose() {

    }
}
