package com.rubentxu.juegos.core.managers;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rubentxu.juegos.core.managers.interfaces.IManager;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPOS;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Enemy.State;

import java.util.ArrayList;


public class EnemyManager implements IManager {

    private ArrayList<Enemy> enemies;

    public EnemyManager(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void update(float delta) {
        for(Enemy enemy:enemies){
            Vector2 vel = enemy.getVelocity();
            Vector2 pos = enemy.getBody().getPosition();
            processVelocity(vel,enemy);
            processContactGround(enemy);
            applyImpulses(vel, pos,enemy);
        }
    }

    public void processVelocity(Vector2 vel,Enemy enemy) {

        if (enemy.getState().equals(State.IDLE)) {
            enemy.getBody().setLinearVelocity(enemy.getVelocity().x * 0.9f, vel.y);
        }
        enemy.velocityLimit();
    }

    public void applyImpulses(Vector2 vel, Vector2 pos,Enemy enemy) {
        // apply left impulse, but only if max velocity is not reached yet
        if (enemy.getState().equals(State.WALKING) && enemy.isFacingLeft() && vel.x > -enemy.MAX_VELOCITY) {
            enemy.getBody().applyLinearImpulse(-2f, 0f, pos.x, pos.y, true);
        }

        // apply right impulse, but only if max velocity is not reached yet
        if (enemy.getState().equals(State.WALKING) && !enemy.isFacingLeft() && vel.x < enemy.MAX_VELOCITY) {
            enemy.getBody().applyLinearImpulse(2f, 0, pos.x, pos.y, true);
        }

        // jump, but only when grounded
        if ((enemy.getState().equals(State.JUMPING))) {
            if (enemy.isGround()) {
                enemy.getBody().setLinearVelocity(vel.x, 0);
                enemy.getBody().setTransform(pos.x, pos.y + 0.01f, 0);
                enemy.getBody().applyLinearImpulse(0, enemy.JUMP_FORCE, pos.x, pos.y, true);
            }
        }
    }

    public void processContactGround(Enemy enemy) {
        if (!enemy.isGround()) {
            enemy.getEnemyPhysicsFixture().setFriction(0f);
            enemy.getEnemySensorFixture().setFriction(0f);
            if (enemy.getVelocity().y <= 0 || !enemy.getState().equals(Enemy.State.JUMPING))
                enemy.setState(Enemy.State.FALL);
        } else {
            enemy.setState(Enemy.State.IDLE);
            if (enemy.getState().equals(State.IDLE)) {
                enemy.getEnemyPhysicsFixture().setFriction(100f);
                enemy.getEnemySensorFixture().setFriction(100f);
            } else {
                enemy.getEnemyPhysicsFixture().setFriction(0.2f);
                enemy.getEnemySensorFixture().setFriction(0.2f);
            }
        }
    }

    public Enemy getEnemy(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (box2dPhysicsA.getGrupo().equals(GRUPOS.ENEMIGOS)) {
            return  (Enemy) box2dPhysicsA;
        } else {
            return (Enemy) box2dPhysicsB;
        }
    }

    public Box2DPhysicsObject getOther(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (!box2dPhysicsA.getGrupo().equals(Box2DPhysicsObject.GRUPOS.ENEMIGOS)) {
            return box2dPhysicsA;
        } else {
            return box2dPhysicsB;
        }
    }


    @Override
    public void handleBeginContact(Contact contact) {
        Enemy enemy = getEnemy(contact);
        Box2DPhysicsObject other = getOther(contact);

        if (contact.getFixtureA() == enemy.getEnemySensorFixture())
            enemy.getGrounContacts().add(contact.getFixtureB());//A is foot so B is ground

        if (contact.getFixtureB() == enemy.getEnemySensorFixture())
            enemy.getGrounContacts().add(contact.getFixtureA());//A is foot so B is ground

        if (enemy.getGrounContacts().size() > 0) {
            enemy.setGround(true);
            contact.setEnabled(true);
            enemy.getEnemyPhysicsFixture().setFriction(0.2f);
            enemy.getEnemySensorFixture().setFriction(0.2f);
        }


    }

    @Override
    public void handleEndContact(Contact contact) {
        Enemy enemy = getEnemy(contact);
        Box2DPhysicsObject other = getOther(contact);


        if (contact.getFixtureA() == enemy.getEnemySensorFixture())
            enemy.getGrounContacts().remove(contact.getFixtureB());//A is foot so B is ground

        if (contact.getFixtureB() == enemy.getEnemySensorFixture())
            enemy.getGrounContacts().remove(contact.getFixtureA());//A is foot so B is ground

        if (enemy.getGrounContacts().size() == 0) {
            enemy.setGround(false);
            contact.resetFriction();
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

}
