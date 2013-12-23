package com.rubentxu.juegos.core.managers;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.rubentxu.juegos.core.managers.interfaces.IManager;
import com.rubentxu.juegos.core.modelo.MovingPlatform;
import com.rubentxu.juegos.core.modelo.Platform;
import com.rubentxu.juegos.core.modelo.Rubentxu;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;

import java.util.HashSet;

public class PlatformManager implements IManager {


    private HashSet<Platform> platforms;
    private HashSet<MovingPlatform> movingPlatforms;
    private Boolean enabledContac = true;

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


        if ((platform.waitForPassenger && platform.getPassengers().size() == 0) || !platform.enabled) {
            platform.getBody().setLinearVelocity(new Vector2(0f, 0f));
        } else {

            boolean check = platform.getPath().updatePath(platform.getBody().getPosition(), delta);
            Vector2 velPlatform = platform.getPath().getVelocity().cpy();

            if (check) {

                for (Box2DPhysicsObject passenger : platform.getPassengers()) {
                    System.out.println("Cambio e impulso: "+velPlatform.y);
                    passenger.getBody().setLinearVelocity(velPlatform);
                }
            }
            platform.getBody().setLinearVelocity(velPlatform);
        }
    }

    public MovingPlatform getMovingPlatform(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (box2dPhysicsA.getGrupo().equals(Box2DPhysicsObject.GRUPOS.PLATAFORMAS_MOVILES)) {
            return (MovingPlatform) box2dPhysicsA;
        } else {
            return (MovingPlatform) box2dPhysicsB;
        }
    }

    public Box2DPhysicsObject getPassenger(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (!box2dPhysicsA.getGrupo().equals(Box2DPhysicsObject.GRUPOS.PLATAFORMAS_MOVILES)) {
            return box2dPhysicsA;
        } else {
            return box2dPhysicsB;
        }
    }

    public Vector2 getRelativeVelocity(MovingPlatform movingPlatform, Box2DPhysicsObject passenger, Vector2 point) {
        Vector2 velMovingPlatform = movingPlatform.getBody().getLinearVelocityFromWorldPoint(point);
        Vector2 velPassenger = passenger.getBody().getLinearVelocityFromWorldPoint(point);
        return movingPlatform.getBody().getLocalVector(velPassenger.cpy().sub(velMovingPlatform));
    }


    @Override
    public void handleBeginContact(Contact contact) {
        MovingPlatform movingPlatform = getMovingPlatform(contact);
        Box2DPhysicsObject passenger = getPassenger(contact);

        WorldManifold manifold = contact.getWorldManifold();
        for (Vector2 point : manifold.getPoints()) {
            float posPlatform = movingPlatform.getBody().getPosition().y + movingPlatform.getHeight() / 2;
            float posPassenger = passenger.getBody().getPosition().y;
            Vector2 relativeVel = getRelativeVelocity(movingPlatform, passenger, point);

            if (relativeVel.y < -1 && posPlatform < posPassenger) {
                movingPlatform.getPassengers().add(passenger);
                return;
            } else if (relativeVel.y < 1 && posPlatform < posPassenger) { //if moving slower than 1 m/s

                Vector2 relativePoint = movingPlatform.getBody().getLocalPoint(point);
                float platformFaceY = 0.5f;
                if (relativePoint.y > platformFaceY - 0.05) {
                    movingPlatform.getPassengers().add(passenger);
                    return;
                }
            }
        }
        if (passenger.getGrupo().equals(Box2DPhysicsObject.GRUPOS.HEROES) &&
                !((Rubentxu) passenger).getState().equals(Rubentxu.State.WALKING)) {
            contact.setFriction(100f);
        }
        setEnabledContac(false);
        contact.setEnabled(getEnabledContac());
    }


    @Override
    public void handleEndContact(Contact contact) {
        MovingPlatform movingPlatform = getMovingPlatform(contact);
        Box2DPhysicsObject passenger = getPassenger(contact);
        movingPlatform.getPassengers().remove(passenger);
        setEnabledContac(true);
        contact.setEnabled(getEnabledContac());
    }

    @Override
    public void handlePostSolve(Contact contact, ContactImpulse impulse) {
        contact.setEnabled(getEnabledContac());
    }

    @Override
    public void handlePreSolve(Contact contact, Manifold oldManifold) {
        contact.setEnabled(getEnabledContac());

        if (contact.isEnabled()) {
            MovingPlatform movingPlatform = getMovingPlatform(contact);
            Box2DPhysicsObject passenger = getPassenger(contact);
            if (passenger.getGrupo().equals(Box2DPhysicsObject.GRUPOS.HEROES) &&
                    !((Rubentxu) passenger).getState().equals(Rubentxu.State.WALKING)) {
                contact.setFriction(100f);
            } else if (passenger.getGrupo().equals(Box2DPhysicsObject.GRUPOS.HEROES) &&
                    !((Rubentxu) passenger).getState().equals(Rubentxu.State.WALKING)) {
                contact.setFriction(0);
            }
        }

    }

    @Override
    public boolean handleShouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return false;
    }

    public Boolean getEnabledContac() {
        return enabledContac;
    }

    public void setEnabledContac(Boolean enabledContac) {
        this.enabledContac = enabledContac;
    }
}
