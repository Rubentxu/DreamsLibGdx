package com.rubentxu.juegos.core.managers.world;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rubentxu.juegos.core.managers.AbstractWorldManager;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Enemy.StateEnemy;
import com.rubentxu.juegos.core.modelo.Hero;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPO;

public class EnemyManager extends AbstractWorldManager {


    public EnemyManager(World world) {
        super(world);
    }

    public void update(float delta) {
        for (Enemy enemy : world.getEnemies()) {
            enemy.getPath().update(enemy.getBodyA().getPosition(), delta);
            Vector2 vel = enemy.getVelocity();
            Vector2 pos = enemy.getBodyA().getPosition();
            enemy.setStateTime(enemy.getStateTime() + delta);
            handleState(enemy);
        }
    }

    private void applyPhysicJumpingImpulse(Vector2 vel, Vector2 pos, Enemy enemy) {
        enemy.getBodyA().setLinearVelocity(vel.x, 0);
        enemy.getBodyA().setTransform(pos.x, pos.y + 0.01f, 0);
        enemy.getBodyA().applyLinearImpulse(0, enemy.JUMP_FORCE, pos.x, pos.y, true);
    }

    public void handleState(Enemy enemy) {
        Vector2 vel = enemy.getVelocity();
        Vector2 pos = enemy.getBodyA().getPosition();

        switch (enemy.getStatePos()) {
            case ONGROUND:
                if (enemy.getState().equals(StateEnemy.IDLE)) {
                    enemy.getBodyA().setLinearVelocity(enemy.getVelocity().x * 0.9f, vel.y);
                    enemy.getEnemyPhysicsFixture().setFriction(100f);
                    enemy.getEnemySensorFixture().setFriction(100f);

                } else if (enemy.getState().equals(StateEnemy.WALKING)) {
                    enemy.getBodyA().applyLinearImpulse(enemy.getPath().getForce(enemy.getBodyA().getMass()), enemy.getBodyA().getWorldCenter(), true);
                    enemy.getEnemyPhysicsFixture().setFriction(0.2f);
                    enemy.getEnemySensorFixture().setFriction(0.2f);

                } else if (enemy.getState().equals(StateEnemy.JUMPING)) {
                    if (!enemy.getStatePos().equals(Hero.StatePos.ONAIR))
                        applyPhysicJumpingImpulse(vel, pos, enemy);

                }

                break;
            case INWATER:



                break;
            case ONAIR:

                break;
        }
        enemy.velocityLimit();
    }


    public boolean isGroundGrupo(Fixture fixture) {

        boolean check = ((Box2DPhysicsObject) fixture.getUserData()).getGrupo().equals(GRUPO.STATIC) ||
                ((Box2DPhysicsObject) fixture.getUserData()).getGrupo().equals(GRUPO.PLATFORM) ||
                ((Box2DPhysicsObject) fixture.getUserData()).getGrupo().equals(GRUPO.MOVING_PLATFORM);
        System.out.println("IsGrounGrupo " + check);
        return check;
    }

    public Enemy getEnemy(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (box2dPhysicsA.getGrupo().equals(GRUPO.ENEMY)) {
            return (Enemy) box2dPhysicsA;
        } else {
            return (Enemy) box2dPhysicsB;
        }
    }

    public Box2DPhysicsObject getOther(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (!box2dPhysicsA.getGrupo().equals(GRUPO.ENEMY)) {
            return box2dPhysicsA;
        } else {
            return box2dPhysicsB;
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


    @Override
    public void handleBeginContact(Contact contact) {
        Enemy enemy = getEnemy(contact);
        Hero hero = getHero(contact);


        if (isGroundGrupo(contact.getFixtureB()) && contact.getFixtureA() == enemy.getEnemySensorFixture()
                && !enemy.getStatePos().equals(Enemy.StatePos.INWATER)) {
            enemy.getGrounContacts().add(contact.getFixtureB());
        }

        if (isGroundGrupo(contact.getFixtureA()) && contact.getFixtureB() == enemy.getEnemySensorFixture()
                && !enemy.getStatePos().equals(Enemy.StatePos.INWATER)) {
            enemy.getGrounContacts().add(contact.getFixtureA());
        }

        if (enemy.getGrounContacts().size() > 0) {
            enemy.setStatePos(Enemy.StatePos.ONGROUND);
            enemy.setState(StateEnemy.WALKING);
            contact.setEnabled(true);
        }


        if (hero != null && enemy != null && (contact.getFixtureA() == hero.getHeroSensorFixture() ||
                contact.getFixtureB() == hero.getHeroSensorFixture())) {
            Vector2[] points = contact.getWorldManifold().getPoints();
            if (points[0].y > enemy.getBodyA().getPosition().y + enemy.getHeightBodyA() / 2) {
                enemy.setFlaggedForDelete(true);
                world.addBodiesFlaggedDestroy(enemy.getBodyA());
            }
        }

        if (hero != null && (contact.getFixtureA() == hero.getHeroPhysicsFixture()
                || contact.getFixtureB() == hero.getHeroPhysicsFixture()) && !enemy.getState().equals(StateEnemy.HIT)) {

            Vector2[] points = contact.getWorldManifold().getPoints();
            Vector2 force;
            if (points[0].x < enemy.getBodyA().getPosition().x) {
                force = new Vector2(6, 10);
            } else {
                force = new Vector2(-6, 10);
            }

            enemy.getBodyA().applyLinearImpulse(force, enemy.getBodyA().getWorldCenter(), true);

            enemy.setState(StateEnemy.HIT);
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
            enemy.setStatePos(Enemy.StatePos.ONAIR);
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
    public void dispose() {

    }
}
