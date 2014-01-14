package com.rubentxu.juegos.core.managers.world;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rubentxu.juegos.core.managers.interfaces.AbstractWorldManager;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Enemy.State;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPOS;


public class EnemyManager extends AbstractWorldManager {


    public EnemyManager(World world) {
        super(world);
    }

    public void update(float delta) {
        for(Enemy enemy:world.getEnemies()){
            enemy.getPath().update(enemy.getBody().getPosition(),delta);
            Vector2 vel = enemy.getVelocity();
            Vector2 pos = enemy.getBody().getPosition();
            //processVelocity(vel,enemy);
            processContactGround(enemy);
            applyImpulses(vel, pos,enemy);

        }
    }

    public void processVelocity(Vector2 vel,Enemy enemy) {

        if (enemy.getState().equals(State.IDLE)) {
            enemy.getBody().setLinearVelocity(enemy.getVelocity().x * 0.9f, vel.y);
        }

    }

    public void applyImpulses(Vector2 vel, Vector2 pos,Enemy enemy) {
        enemy.velocityLimit();
        if (enemy.getState().equals(State.WALKING)) {
            enemy.getBody().applyLinearImpulse(enemy.getPath().getForce(enemy.getBody().getMass()),enemy.getBody().getWorldCenter(),true);
        }

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
            if (enemy.getVelocity().y < 0 || !enemy.getState().equals(Enemy.State.JUMPING))
                enemy.setState(Enemy.State.FALL);
        } else {
            enemy.setState(State.WALKING);
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
            enemy.getGrounContacts().add(contact.getFixtureB());

        if (contact.getFixtureB() == enemy.getEnemySensorFixture())
            enemy.getGrounContacts().add(contact.getFixtureA());

        if (enemy.getGrounContacts().size() > 0) {
            enemy.setGround(true);
            System.out.println("Enemy Ground");
            contact.setEnabled(true);
        }

    }

    @Override
    public void handleEndContact(Contact contact) {
        Enemy enemy = getEnemy(contact);
        Box2DPhysicsObject other = getOther(contact);

        if (contact.getFixtureA() == enemy.getEnemySensorFixture())
            enemy.getGrounContacts().remove(contact.getFixtureB());

        if (contact.getFixtureB() == enemy.getEnemySensorFixture())
            enemy.getGrounContacts().remove(contact.getFixtureA());

        if (enemy.getGrounContacts().size() == 0) {
            System.out.println("Enemy UnGround");
            enemy.setGround(false);
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
