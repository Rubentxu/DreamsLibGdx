package com.rubentxu.juegos.core.managers;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rubentxu.juegos.core.managers.interfaces.IManager;
import com.rubentxu.juegos.core.modelo.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.Platform;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.modelo.interfaces.MovingPlatform;

import java.util.HashSet;

public class PlatformManager implements IManager {

    private World world;
    private HashSet<Platform> platforms;
    private HashSet<MovingPlatform> MovingPlatformplatforms;

    @Override
    public void handleBeginContact(Contact contact) {
        for (MovingPlatform p: getMovingPlatformplatforms()){
            p.getPassengers().add(getCollider(p,contact).getBody());
        }
    }

    @Override
    public void handleEndContact(Contact contact) {
        for (MovingPlatform p: getMovingPlatformplatforms()){
            p.getPassengers().remove(getCollider(p,contact).getBody());
        }
    }

    @Override
    public void handlePostSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public void handlePreSolve(Contact contact, Manifold oldManifold) {

        for (Platform p : getPlatforms()) {
            if (p.isOneWay()) {
                Box2DPhysicsObject collider = (Box2DPhysicsObject) ((p == contact.getFixtureA().getUserData()) ? contact.getFixtureB().getUserData() : contact.getFixtureA().getUserData());
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

    @Override
    public void update(float delta) {
       for (MovingPlatform p: getMovingPlatformplatforms()){
           updateMovingPlatform(p,delta);
       }
    }

    private void updateMovingPlatform(MovingPlatform platform,float delta){
        Vector2 velocity= platform.getBody().getLinearVelocity();
        if((platform.waitForPassenger && platform.getPassengers().size()==0) || !platform.enabled){
            velocity.set(0,0);
        }else {
            Vector2 destination= platform.getForward()? new Vector2(platform.getEnd().x,platform.getEnd().y):
                                new Vector2(platform.getStart().x,platform.getStart().y);

            destination.sub(platform.getBody().getPosition());
            velocity= destination;

            if(velocity.len()> platform.speed) {
                velocity.limit(platform.speed);
            }else {
                platform.setForward(!platform.getForward());
            }

        }
        platform.getBody().setLinearVelocity(velocity);

        Vector2 passengerVelocity;
        for (Body passenger : platform.getPassengers()){
            if(velocity.y > 0){
                passengerVelocity= passenger.getLinearVelocity();
                passengerVelocity.y+= velocity.y;
                passenger.setLinearVelocity(passengerVelocity);
            }

        }
    }

    public HashSet<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(HashSet<Platform> platforms) {
        this.platforms = platforms;
    }

    public HashSet<MovingPlatform> getMovingPlatformplatforms() {
        return MovingPlatformplatforms;
    }

    public void setMovingPlatformplatforms(HashSet<MovingPlatform> movingPlatformplatforms) {
        MovingPlatformplatforms = movingPlatformplatforms;
    }

    private Box2DPhysicsObject getCollider (MovingPlatform p,Contact contact) {
        Box2DPhysicsObject collider = (Box2DPhysicsObject) ((p == contact.getFixtureA().getUserData()) ? contact.getFixtureB().getUserData() : contact.getFixtureA().getUserData());
        return collider;
    }
}
