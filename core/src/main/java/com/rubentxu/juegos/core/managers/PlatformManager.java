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
    private float mLastPlatformXPos=0, mLastPlatformYPos=0;

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
        System.out.println("Friccion2 "+ contact.getFriction());
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if(box2dPhysicsA.getGrupo().equals(Box2DPhysicsObject.GRUPOS.PLATAFORMAS_MOVILES)){
            float posPlatform = box2dPhysicsA.getBody().getPosition().y + box2dPhysicsB.getHeight() / 2 ;
            float posPassenger = box2dPhysicsB.getBody().getPosition().y - box2dPhysicsB.getHeight() / 1.9f;
            int numPoints = contact.getWorldManifold().getNumberOfContactPoints();
            WorldManifold worldManifold = contact.getWorldManifold();

            //check if contact points are moving downward
            for (int i = 0; i < numPoints; i++) {
                Vector2 pointVel = box2dPhysicsB.getBody().getLinearVelocityFromWorldPoint(worldManifold.getPoints()[i]);
                if ( pointVel.y > 0 )
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
    public void handleEndContact(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();
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
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();
        MovingPlatform movingPlatform;
        Box2DPhysicsObject passenger;

        if(box2dPhysicsA.getGrupo().equals(Box2DPhysicsObject.GRUPOS.PLATAFORMAS_MOVILES)){
            movingPlatform= (MovingPlatform) box2dPhysicsA;
            passenger= box2dPhysicsB;
        }else {
            movingPlatform= (MovingPlatform) box2dPhysicsB;
            passenger= box2dPhysicsA;
        }
        if(passenger.getGrupo().equals(Box2DPhysicsObject.GRUPOS.HEROES) &&
                !((Rubentxu) passenger).getState().equals(Rubentxu.State.WALKING)){
            contact.setFriction(100f);

        }

        for ( Vector2 point  :contact.getWorldManifold().getPoints()) {

            Vector2 pointVelPlatform = movingPlatform.getBody().getLinearVelocityFromWorldPoint( point );
            Vector2 pointVelOther = passenger.getBody().getLinearVelocityFromWorldPoint( point );
            Vector2 relativeVel = movingPlatform.getBody().getLocalVector(pointVelOther.sub(pointVelPlatform));
            Vector2 relativeVelP = passenger.getBody().getLocalVector(pointVelPlatform.sub(pointVelOther));
            if ( relativeVel.y > 0 && passenger.getGrupo().equals(Box2DPhysicsObject.GRUPOS.HEROES)
                    && !((Rubentxu) passenger).getState().equals(Rubentxu.State.JUMPING) ) {
                relativeVel.x= (((Rubentxu) passenger).getState().equals(Rubentxu.State.WALKING))?-passenger.getVelocity().nor().x:relativeVel.x;
                passenger.getBody().applyLinearImpulse(relativeVel.scl(-4.5f), passenger.getBody().getWorldCenter(), true);
            }
        }

    }

    @Override
    public boolean handleShouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return false;
    }
}
