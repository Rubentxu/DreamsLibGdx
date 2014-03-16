package com.rubentxu.juegos.core.managers.world;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rubentxu.juegos.core.BaseGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.constantes.GameState;
import com.rubentxu.juegos.core.controladores.WorldController.Keys;
import com.rubentxu.juegos.core.managers.AbstractWorldManager;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Hero;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.BaseState;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPO;
import com.rubentxu.juegos.core.modelo.base.State;

import static com.rubentxu.juegos.core.controladores.WorldController.keys;
import static com.rubentxu.juegos.core.modelo.Hero.StateHero.FALL;
import static com.rubentxu.juegos.core.modelo.Hero.StateHero.IDLE;
import static com.rubentxu.juegos.core.modelo.Hero.StateHero.JUMPING;
import static com.rubentxu.juegos.core.modelo.Hero.StateHero.PROPULSION;
import static com.rubentxu.juegos.core.modelo.Hero.StateHero.SWIMMING;
import static com.rubentxu.juegos.core.modelo.Hero.StateHero.WALKING;


public class HeroManager extends AbstractWorldManager {

    private float stillTime = 0;

    @Override
    public void update(float delta, Box2DPhysicsObject entity) {
        Hero hero = (Hero) entity;
        if (!hero.getState().equals(BaseState.HURT) || !hero.getState().equals(BaseState.HIT)
                || !hero.getState().equals(BaseState.DEAD)) {
            handleInput(hero);
            hero.setStateTime(hero.getStateTime() + delta);
            hero.getParticleEffectDust().setPosition(hero.getXBodyA() + hero.getWidthBodyA() / 2, hero.getYBodyA());
            hero.getParticleEffectDust().update(delta);
            hero.getParticleEffectContact().update(delta);
        } else if (hero.getState().equals(BaseState.HURT)) {

            Gdx.app.log(Constants.LOG, "---------------------------------------------------------------------PIERDES VIDA???");
            if (hero.getProfile().removeLive()) BaseGame.setGameState(GameState.GAME_OVER);
            hero.setState(Hero.StateHero.IDLE);

            handleState(hero.getState(), hero);
            hero.setStateTime(hero.getStateTime() + delta);
        }
    }


    public void handleInput(Hero hero) {
        State state = null;
        switch (hero.getStatePos()) {
            case ONGROUND:
                if (keys.get(Keys.LEFT)) {
                    state = WALKING;
                    hero.setFacingLeft(true);
                }
                if (keys.get(Keys.RIGHT)) {
                    state = WALKING;
                    hero.setFacingLeft(false);
                }

                if (!keys.get(Keys.LEFT) && !keys.get(Keys.RIGHT)) {
                    stillTime += Gdx.graphics.getDeltaTime();
                    state = IDLE;
                }
                if (keys.get(Keys.JUMP)) {
                    if (hero.getStateTime() > 0.2) state = JUMPING;
                }
                handleState(state, hero);
                break;
            case INWATER:
                if (keys.get(Keys.LEFT)) {
                    state = SWIMMING;
                    hero.setFacingLeft(true);
                }
                if (keys.get(Keys.RIGHT)) {
                    state = SWIMMING;
                    hero.setFacingLeft(false);
                }
                if (!keys.get(Keys.LEFT) && !keys.get(Keys.RIGHT)) {
                    stillTime += Gdx.graphics.getDeltaTime();
                    state = IDLE;
                }
                if (keys.get(Keys.JUMP)) {
                    state = PROPULSION;
                }

                handleState(state, hero);
                hero.getParticleEffectDust().allowCompletion();
                break;
            case ONAIR:
                if (keys.get(Keys.LEFT)) {
                    hero.setFacingLeft(true);
                }
                if (keys.get(Keys.RIGHT)) {
                    hero.setFacingLeft(false);
                }

                if (hero.getVelocity().y <= 0) {
                    state = FALL;
                } else {

                    state = JUMPING;
                }
                hero.getHeroPhysicsFixture().setFriction(0f);
                hero.getHeroSensorFixture().setFriction(0f);
                handleState(state, hero);
                hero.getParticleEffectDust().allowCompletion();
                break;
        }
        hero.velocityLimit();

    }

    public void handleState(State state, Hero hero) {

        if (hero.setState(state)) notifyObservers(state, hero);

        Vector2 vel = hero.getVelocity();
        Vector2 pos = hero.getBodyA().getPosition();

        if (hero.getState().equals(Hero.StateHero.IDLE)) {
            stillTime += Gdx.graphics.getDeltaTime();
            hero.getBodyA().setLinearVelocity(hero.getVelocity().x * 0.9f, vel.y);
            hero.getHeroPhysicsFixture().setFriction(100f);
            hero.getHeroSensorFixture().setFriction(100f);
            hero.getParticleEffectDust().allowCompletion();

        } else if (hero.getState().equals(Hero.StateHero.WALKING)) {
            applyPhysicMovingImpulse(hero);
            stillTime = 0;
            hero.getHeroPhysicsFixture().setFriction(0.2f);
            hero.getHeroSensorFixture().setFriction(0.2f);
            if (hero.getParticleEffectDust().isComplete()) hero.getParticleEffectDust().reset();

        } else if (hero.getState().equals(Hero.StateHero.JUMPING)) {
            if (!hero.getStatePos().equals(Hero.StatePos.ONAIR)) applyPhysicJumpingImpulse(vel, pos, hero);
            if (keys.get(Keys.LEFT) || keys.get(Keys.RIGHT)) {
                applyPhysicMovingImpulse(hero);
            }
        } else if (hero.getState().equals(Hero.StateHero.PROPULSION)) {
            applyPhysicJumpingImpulse(vel, pos, hero);

        } else if (hero.getState().equals(Hero.StateHero.SWIMMING)) {
            applyPhysicMovingImpulse(hero);
            hero.getParticleEffectDust().allowCompletion();
        } else if (hero.getState().equals(Hero.StateHero.FALL)) {
            if (keys.get(Keys.LEFT) || keys.get(Keys.RIGHT)) {
                applyPhysicMovingImpulse(hero);
            }
        }

    }


    private void applyPhysicJumpingImpulse(Vector2 vel, Vector2 pos, Hero hero) {
        hero.getBodyA().setLinearVelocity(vel.x, 0);
        hero.getBodyA().setTransform(pos.x, pos.y + 0.01f, 0);
        hero.getBodyA().applyLinearImpulse(0, hero.JUMP_FORCE, pos.x, pos.y, true);
    }

    private void applyPhysicMovingImpulse(Hero hero) {
        Vector2 vel = hero.getVelocity();
        Vector2 pos = hero.getBodyA().getPosition();

        float impulse = 0;
        if (hero.isFacingLeft() && vel.x > -hero.MAX_VELOCITY) {
            impulse = -3;
        }
        if (!hero.isFacingLeft() && vel.x < hero.MAX_VELOCITY) {
            impulse = 3;
        }
        if (!hero.isFacingLeft() && vel.x < 0) {
            impulse = 1.5f;
        }
        if (hero.isFacingLeft() && vel.x > 0) {
            impulse = -1.5f;
        }
        hero.getBodyA().applyLinearImpulse(impulse, 0, pos.x, pos.y, true);
    }


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

    public boolean isGroundGrupo(Fixture fixture) {

        boolean check = ((Box2DPhysicsObject) fixture.getUserData()).getGrupo().equals(GRUPO.STATIC) ||
                ((Box2DPhysicsObject) fixture.getUserData()).getGrupo().equals(GRUPO.PLATFORM) ||
                ((Box2DPhysicsObject) fixture.getUserData()).getGrupo().equals(GRUPO.MOVING_PLATFORM);
        return check;
    }

    @Override
    public void handleBeginContact(Contact contact) {

        Enemy enemy = getEnemy(contact);
        Hero hero = getHero(contact);

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

        if (enemy != null && hero != null) {

            Vector2 point = contact.getWorldManifold().getPoints()[0];
            Vector2 pointVelEnemy = enemy.getBodyA().getLinearVelocityFromWorldPoint(point);
            Vector2 pointVelHero = hero.getBodyA().getLinearVelocityFromWorldPoint(point);
            Vector2 diff = pointVelHero.cpy().sub(pointVelEnemy);
            Vector2 relativeVel = enemy.getBodyA().getLocalVector(diff);
            Gdx.app.log(Constants.LOG, " Contacto relativeVel " + relativeVel + " pointVelEnemy " + pointVelEnemy
                    + " pointVelHero " + pointVelHero + " diff " + diff + " HeroPhysics " +
                    contact.getFixtureA().equals(hero.getHeroPhysicsFixture()) + " " + contact.getFixtureB().equals(hero.getHeroPhysicsFixture()) +
                    " HeroSensor " + contact.getFixtureA().equals(hero.getHeroSensorFixture()) + " " + contact.getFixtureB().equals(hero.getHeroSensorFixture()));


            if (contact.getFixtureA().equals(hero.getHeroPhysicsFixture()) ||
                    contact.getFixtureB().equals(hero.getHeroPhysicsFixture())) {
                if (relativeVel.y < -0.5) {
                    enemy.setState(BaseState.DEAD);
                    hero.setState(BaseState.HIT);
                } else {
                    enemy.setState(BaseState.HIT);
                    hero.setState(BaseState.HURT);
                }
                hero.getParticleEffectContact().setPosition(point.x, point.y);
                hero.getParticleEffectContact().reset();
                Vector2 force = contact.getWorldManifold().getNormal();
                if (contact.getFixtureA().equals(hero.getHeroPhysicsFixture())) {
                    force.add(0, 0.7f);
                    force.scl(-8);
                } else {
                    force.add(0, 0.7f);
                    force.scl(8);
                }

                System.out.println("Fuerza colision Enemigo: " + force + " relativePoint " + force);
                hero.getBodyA().applyLinearImpulse(force, hero.getBodyA().getWorldCenter(), true);
                hero.velocityLimit();
            }
        }
    }

    @Override
    public void handleEndContact(Contact contact) {
        //Gdx.app.log(DreamsGame.LOG, "End contact");
        Enemy enemy = getEnemy(contact);
        Hero hero = getHero(contact);

        if (contact.getFixtureA() == hero.getHeroSensorFixture())
            hero.getGrounContacts().remove(contact.getFixtureB());//A is foot so B is ground

        if (contact.getFixtureB() == hero.getHeroSensorFixture())
            hero.getGrounContacts().remove(contact.getFixtureA());//A is foot so B is ground


        if (hero.getGrounContacts().size() == 0) {
            if (!hero.getStatePos().equals(Hero.StatePos.INWATER))
                hero.setStatePos(Hero.StatePos.ONAIR);


        }

        if (enemy != null && hero != null) {
            hero.getParticleEffectContact().allowCompletion();
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
