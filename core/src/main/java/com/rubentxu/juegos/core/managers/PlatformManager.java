package com.rubentxu.juegos.core.managers;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.rubentxu.juegos.core.managers.interfaces.IManager;
import com.rubentxu.juegos.core.modelo.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.Platform;
import com.rubentxu.juegos.core.modelo.Rubentxu;
import com.rubentxu.juegos.core.modelo.interfaces.MovingPlatform;

import java.util.HashSet;

public class PlatformManager implements IManager {


    private HashSet<Platform> platforms;
    private HashSet<MovingPlatform> movingPlatforms;

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

            if (platform.getForward()) velocity.scl(-1f);

            platform.setTime(platform.getTime() + delta);
            platform.setDistance(platform.getDistance() + velocity.len() * delta);

            if (platform.getDistance() > platform.getMaxDist()) {
                platform.setTime(0);
                platform.setForward(!platform.getForward());
                platform.setDistance(0);
            }

            platform.getBody().setLinearVelocity(velocity);

            for (Box2DPhysicsObject passenger : platform.getPassengers()) {
                if (velocity.y < 0) {
                    float forceY;
                    if (passenger.getGrupo().equals(Box2DPhysicsObject.GRUPOS.HEROES)
                            && ((Rubentxu) passenger).getState().equals(Rubentxu.State.JUMPING)) {
                        forceY = 0;
                    } else {
                        //forceY = (velocity.y / delta*0.9f) * passenger.getBody().getMass();
                        Vector2 p=passenger.getBody().getLinearVelocityFromWorldPoint(velocity);
                        forceY = (p.y / delta) * passenger.getBody().getMass();
                    }

                    passenger.getBody().applyForce(0, forceY, passenger.getBody().getWorldCenter().x,
                            passenger.getBody().getWorldCenter().y, true);
                }
            }
        }
    }

    @Override
    public void handleBeginContact(Contact contact, Box2DPhysicsObject box2dPhysicsA, Box2DPhysicsObject box2dPhysicsB) {

        if(box2dPhysicsA.getGrupo().equals(Box2DPhysicsObject.GRUPOS.PLATAFORMAS_MOVILES)){
            float posPlatform = box2dPhysicsA.getBody().getPosition().y + box2dPhysicsB.getHeight() / 2 ;
            float posPassenger = box2dPhysicsB.getBody().getPosition().y - box2dPhysicsB.getHeight() / 1.9f;
            int numPoints = contact.getWorldManifold().getNumberOfContactPoints();
            WorldManifold worldManifold = contact.getWorldManifold();

            //check if contact points are moving downward
            for (int i = 0; i < numPoints; i++) {
                Vector2 pointVel = box2dPhysicsB.getBody().getLinearVelocityFromWorldPoint(worldManifold.getPoints()[i]);
                if ( pointVel.y < 0 )
                    return;//point is moving down, leave contact solid and exit
            }
            if(posPlatform< posPassenger) ((MovingPlatform) box2dPhysicsA).getPassengers().add(box2dPhysicsB);

        }else {
            float posPassenger = box2dPhysicsA.getBody().getPosition().y - box2dPhysicsB.getHeight() / 1.9f;
            float posPlatform = box2dPhysicsB.getBody().getPosition().y + box2dPhysicsB.getHeight() / 2;

            if(posPlatform< posPassenger) ((MovingPlatform) box2dPhysicsB).getPassengers().add(box2dPhysicsA);
        }
    }

    @Override
    public void handleEndContact(Contact contact, Box2DPhysicsObject box2dPhysicsA, Box2DPhysicsObject box2dPhysicsB) {

        if(box2dPhysicsA.getGrupo().equals(Box2DPhysicsObject.GRUPOS.PLATAFORMAS_MOVILES)){
            ((MovingPlatform) box2dPhysicsA).getPassengers().remove(box2dPhysicsB);
        }else {
            ((MovingPlatform) box2dPhysicsB).getPassengers().remove(box2dPhysicsA);
        }
    }

    @Override
    public void handlePostSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public void handlePreSolve(Contact contact, Manifold oldManifold) {

        for (Platform p : getPlatforms()) {
            if (p.isOneWay()) {
                Box2DPhysicsObject collider = (Box2DPhysicsObject) ((p.equals(contact.getFixtureA().getUserData())) ? contact.getFixtureB().getUserData() : contact.getFixtureA().getUserData());
                float colliderBottom = collider.getY() + collider.getHeight() / 2;
                float slope = (float) (Math.sin(p.getBody().getAngle()) / Math.cos(p.getBody().getAngle()));
                if (colliderBottom >= ((slope * (collider.getX() - p.getX())) + p.getY()) - p.getHeight() / 2)
                    contact.setEnabled(false);
            }
        }
    }

    @Override
    public boolean handleShouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return false;
    }
}
