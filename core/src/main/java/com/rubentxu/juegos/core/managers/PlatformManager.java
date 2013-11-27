package com.rubentxu.juegos.core.managers;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.rubentxu.juegos.core.managers.interfaces.IManager;
import com.rubentxu.juegos.core.modelo.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.MovingPlatform;
import com.rubentxu.juegos.core.modelo.Platform;
import com.rubentxu.juegos.core.modelo.Rubentxu;

import java.util.HashSet;

public class PlatformManager implements IManager {


    private HashSet<Platform> platforms;
    private HashSet<MovingPlatform> movingPlatforms;
    private Boolean enabledContac = false;

    public HashSet<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(HashSet<Platform> platforms) {
        this.platforms = platforms;
    }

    public HashSet<MovingPlatform> getMovingPlatforms() {
        return movingPlatforms;
    }

    public void setMovingPlatforms(HashSet<MovingPlatform> movingPlatforms) {
        this.movingPlatforms = movingPlatforms;
    }

    private Box2DPhysicsObject getCollider(MovingPlatform p, Contact contact) {
        return (Box2DPhysicsObject) ((p.equals(contact.getFixtureA().getUserData()) ? contact.getFixtureB().getUserData() : contact.getFixtureA().getUserData()));

    }

    @Override
    public void update(float delta) {
        for (MovingPlatform p : getMovingPlatforms()) {
            updateMovingPlatform(p, delta);
        }
    }

    public void updateMovingPlatform(MovingPlatform platform, float delta) {
        Vector2 velocity = platform.getpVelocity().cpy();

        if ((platform.waitForPassenger && platform.getPassengers().size() == 0) || !platform.enabled) {
            velocity = new Vector2(0f, 0f);
        } else {


            platform.setTime(platform.getTime() + delta);
            platform.setDistance(platform.getDistance() + velocity.len() * delta);


            if (platform.getForward()) velocity.scl(-1f);


            if (platform.getDistance() > platform.getMaxDist()) {
                platform.setTime(0);
                platform.setForward(!platform.getForward());
                platform.setDistance(0);

            }

            platform.getBody().setLinearVelocity(velocity);
        }
    }

    @Override
    public void handleBeginContact(Contact contact) {
        MovingPlatform movingPlatform;
        Box2DPhysicsObject passenger;

        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (box2dPhysicsA.getGrupo().equals(Box2DPhysicsObject.GRUPOS.PLATAFORMAS_MOVILES)) {
            movingPlatform = (MovingPlatform) box2dPhysicsA;
            passenger = box2dPhysicsB;
        } else {
            movingPlatform = (MovingPlatform) box2dPhysicsB;
            passenger = box2dPhysicsA;
        }
        WorldManifold manifold = contact.getWorldManifold();
        for (Vector2 point : manifold.getPoints()) {
            float posPlatform = movingPlatform.getBody().getPosition().y + box2dPhysicsB.getHeight() / 2 ;
            float posPassenger = passenger.getBody().getPosition().y ;
            Vector2 velMovingPlatform = movingPlatform.getBody().getLinearVelocityFromWorldPoint(point);
            Vector2 velPassenger =
                    passenger.getBody().getLinearVelocityFromWorldPoint(point);
            Vector2 relativeVel = movingPlatform.getBody().getLocalVector(velPassenger.cpy().sub(velMovingPlatform));

            if (relativeVel.y < -1 && posPlatform< posPassenger) //if moving down faster than 1 m/s, handle as before
            {
                movingPlatform.getPassengers().add(passenger);
                return;//point is moving into platform, leave contact solid and exit
            } else if (relativeVel.y < 1 && posPlatform< posPassenger) { //if moving slower than 1 m/s
                //borderline case, moving only slightly out of platform
                Vector2 relativePoint = movingPlatform.getBody().getLocalPoint(point);
                float platformFaceY = 0.5f;//front of platform, from fixture definition :(
                if (relativePoint.y > platformFaceY - 0.05 ) {
                    movingPlatform.getPassengers().add(passenger);
                    return;//contact point is less than 5cm inside front face of platfrom
                }


            }
        }
        if (passenger.getGrupo().equals(Box2DPhysicsObject.GRUPOS.HEROES) &&
                !((Rubentxu) passenger).getState().equals(Rubentxu.State.WALKING)) {
            contact.setFriction(100f);

        }

        enabledContac = false;
        contact.setEnabled(enabledContac);
    }

    @Override
    public void handleEndContact(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();
        if (box2dPhysicsA.getGrupo().equals(Box2DPhysicsObject.GRUPOS.PLATAFORMAS_MOVILES)) {
            ((MovingPlatform) box2dPhysicsA).getPassengers().remove(box2dPhysicsB);
        } else {
            ((MovingPlatform) box2dPhysicsB).getPassengers().remove(box2dPhysicsA);
        }
        enabledContac = true;
        contact.setEnabled(enabledContac);
    }

    @Override
    public void handlePostSolve(Contact contact, ContactImpulse impulse) {
        contact.setEnabled(enabledContac);
    }

    @Override
    public void handlePreSolve(Contact contact, Manifold oldManifold) {
        contact.setEnabled(enabledContac);

        if (contact.isEnabled()) {
            Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
            Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();
            MovingPlatform movingPlatform;
            Box2DPhysicsObject passenger;

            if (box2dPhysicsA.getGrupo().equals(Box2DPhysicsObject.GRUPOS.PLATAFORMAS_MOVILES)) {
                movingPlatform = (MovingPlatform) box2dPhysicsA;
                passenger = box2dPhysicsB;
            } else {
                movingPlatform = (MovingPlatform) box2dPhysicsB;
                passenger = box2dPhysicsA;
            }
            if (passenger.getGrupo().equals(Box2DPhysicsObject.GRUPOS.HEROES) &&
                    !((Rubentxu) passenger).getState().equals(Rubentxu.State.WALKING)) {
                contact.setFriction(100f);

            }
            for (Vector2 point : contact.getWorldManifold().getPoints()) {

                Vector2 pointVelPlatform = movingPlatform.getBody().getLinearVelocityFromWorldPoint(point);
                Vector2 pointVelOther = passenger.getBody().getLinearVelocityFromWorldPoint(point);
                Vector2 relativeVel = movingPlatform.getBody().getLocalVector(pointVelOther.sub(pointVelPlatform));
                Vector2 relativeVelP = passenger.getBody().getLocalVector(pointVelPlatform.sub(pointVelOther));
                if (relativeVel.y > 0 && passenger.getGrupo().equals(Box2DPhysicsObject.GRUPOS.HEROES)
                        && !((Rubentxu) passenger).getState().equals(Rubentxu.State.JUMPING)) {
                    relativeVel.x = (((Rubentxu) passenger).getState().equals(Rubentxu.State.WALKING)) ? -passenger.getVelocity().nor().x : relativeVel.x;
                    passenger.getBody().applyLinearImpulse(relativeVel.scl(-4.5f), passenger.getBody().getWorldCenter(), true);
                }
            }
        }

    }

    @Override
    public boolean handleShouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return false;
    }
}
