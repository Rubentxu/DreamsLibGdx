package com.rubentxu.juegos.core.managers.world;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.rubentxu.juegos.core.managers.AbstractWorldManager;
import com.rubentxu.juegos.core.modelo.Hero;
import com.rubentxu.juegos.core.modelo.Hero.StateHero;
import com.rubentxu.juegos.core.modelo.MovingPlatform;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPO;

public class PlatformManager extends AbstractWorldManager {


    @Override
    public void update(float delta, Box2DPhysicsObject entity) {
        MovingPlatform platform= (MovingPlatform) entity;
        updateMovingPlatform(platform,delta);
    }

    public void updateMovingPlatform(MovingPlatform platform, float delta) {


        if ((platform.waitForPassenger && platform.getPassengers().size() == 0) || !platform.enabled) {
            platform.getBodyA().setLinearVelocity(new Vector2(0f, 0f));
        } else {

            boolean check = platform.getPath().update(platform.getBodyA().getPosition(), delta);
            Vector2 velPlatform = platform.getPath().getVelocity().cpy();

            if (check && velPlatform.y < 0) {
                for (Box2DPhysicsObject passenger : platform.getPassengers()) {
                    float force = passenger.getBodyA().getMass() * velPlatform.y*2 ;
                    passenger.getBodyA().applyLinearImpulse(0, force, passenger.getBodyA().getPosition().x,
                            passenger.getBodyA().getPosition().y, true);
                }
            }
            platform.getBodyA().setLinearVelocity(velPlatform);
        }
    }

    public MovingPlatform getMovingPlatform(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (box2dPhysicsA.getGrupo().equals(GRUPO.MOVING_PLATFORM)) {
            return (MovingPlatform) box2dPhysicsA;
        } else {
            return (MovingPlatform) box2dPhysicsB;
        }
    }

    public Box2DPhysicsObject getPassenger(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (!box2dPhysicsA.getGrupo().equals(GRUPO.MOVING_PLATFORM)) {
            return box2dPhysicsA;
        } else {
            return box2dPhysicsB;
        }
    }

    public Vector2 getRelativeVelocity(MovingPlatform movingPlatform, Box2DPhysicsObject passenger, Vector2 point) {
        Vector2 velMovingPlatform = movingPlatform.getBodyA().getLinearVelocityFromWorldPoint(point);
        Vector2 velPassenger = passenger.getBodyA().getLinearVelocityFromWorldPoint(point);
        return movingPlatform.getBodyA().getLocalVector(velPassenger.cpy().sub(velMovingPlatform));
    }


    @Override
    public void handleBeginContact(Contact contact) {
        MovingPlatform movingPlatform = getMovingPlatform(contact);
        contact.setEnabled(movingPlatform.enabled);
    }


    @Override
    public void handleEndContact(Contact contact) {
        MovingPlatform movingPlatform = getMovingPlatform(contact);
        Box2DPhysicsObject passenger = getPassenger(contact);
        movingPlatform.getPassengers().remove(passenger);
        movingPlatform.enabled = true;
        contact.setEnabled(movingPlatform.enabled);
    }

    @Override
    public void handlePostSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public void handlePreSolve(Contact contact, Manifold oldManifold) {
        Box2DPhysicsObject passenger = getPassenger(contact);
        MovingPlatform movingPlatform = getMovingPlatform(contact);

        if (contact.isEnabled()) {
            if (passenger.getGrupo().equals(GRUPO.HERO) &&
                    !((Hero) passenger).getState().equals(StateHero.WALKING)) {
                contact.setFriction(100f);
            } else if (passenger.getGrupo().equals(GRUPO.HERO) &&
                    !((Hero) passenger).getState().equals(StateHero.WALKING)) {
                contact.setFriction(0);
            }
        }
        WorldManifold manifold = contact.getWorldManifold();
        for (Vector2 point : manifold.getPoints()) {
            Vector2 pointVelPlatform= movingPlatform.getBodyA().getLinearVelocityFromWorldPoint(point);
            Vector2 pointVelOther= passenger.getBodyA().getLinearVelocityFromWorldPoint(point);
            Vector2 relativeVel=movingPlatform.getBodyA().getLocalVector(pointVelOther.sub(pointVelPlatform));

            if (relativeVel.y < -1 ) {
                movingPlatform.getPassengers().add(passenger);
                movingPlatform.enabled = true;
                return;
            } else if (relativeVel.y < 1 ) {
                Vector2 relativePoint = movingPlatform.getBodyA().getLocalPoint(point);
                float platformFaceY = 0.5f;
                if (relativePoint.y > platformFaceY - 0.05) {
                    movingPlatform.getPassengers().add(passenger);
                    movingPlatform.enabled = true;
                    return;
                }
            }
        }

        movingPlatform.enabled = false;
        contact.setEnabled(movingPlatform.enabled);

    }

    @Override
    public boolean handleShouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return false;
    }

    @Override
    public void dispose() {

    }
}
