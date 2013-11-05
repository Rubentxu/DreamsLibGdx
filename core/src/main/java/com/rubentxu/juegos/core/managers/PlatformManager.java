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


    private HashSet<Platform> platforms;
    private HashSet<MovingPlatform> MovingPlatformplatforms;

    @Override
    public void update(float delta) {
        for (MovingPlatform p: getMovingPlatformplatforms()){
            Math.max(1/30.0f, delta);
            updateMovingPlatform(p,delta);
        }
    }



    public void updateMovingPlatform(MovingPlatform platform,float delta){
        platform.setDirection((platform.getForward())?platform.reverseDesplazamiento:platform.desplazamiento);
        if((platform.waitForPassenger && platform.getPassengers().size()==0) || !platform.enabled){
            platform.setDirection(new Vector2(0f,0f));
        }else {



          /*  if(platform.getStart().dst(platform.getBody().getPosition()) >= platform.maxDist) {
                platform.setForward( true);
                System.out.println("Cambio direccion");
                platform.getBody().setLinearVelocity(velocity.scl(-delta));
            }  else if (platform.getEnd().dst(platform.getBody().getPosition()) <= platform.maxDist) {
                 platform.setForward(false);
                platform.getBody().setLinearVelocity(velocity.scl(delta));
            }*/
            platform.getDirection().sub( platform.getDirection().x*delta*platform.speed,
                    platform.getDirection().y*delta*platform.speed);

            if(Math.abs( platform.getDirection().len()) < 1){
                platform.setForward(!platform.getForward());
                System.out.println("Sustraer");

            }
            platform.getBody().setLinearVelocity(platform.getDirection());

            for (Body passenger : platform.getPassengers()){
                passenger.setLinearVelocity(platform.getDirection());
            }
        }


        Vector2 passengerVelocity;
        for (Body passenger : platform.getPassengers()){
            if(platform.getDirection().y > 0){
                passengerVelocity= passenger.getLinearVelocity();
                passengerVelocity.y+= platform.getDirection().y;
                passenger.setLinearVelocity(passengerVelocity);
            }

        }
    }

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
