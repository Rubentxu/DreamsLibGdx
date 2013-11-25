package com.rubentxu.juegos.core.managers;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.controladores.WorldController.Keys;
import com.rubentxu.juegos.core.managers.interfaces.IManager;
import com.rubentxu.juegos.core.modelo.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.Rubentxu;


public class RubentxuManager implements IManager {

    private Rubentxu ruben;
    private int stillTime = 0;

    public RubentxuManager(Rubentxu ruben) {
        this.ruben = ruben;
    }

    public void update(float delta) {
        Vector2 vel = ruben.getVelocity();
        Vector2 pos = ruben.getBody().getPosition();
        processVelocity(vel,delta);
        processContactGround();
        applyImpulses(vel, pos);
    }

    public void processVelocity(Vector2 vel,float delta) {

        if (!WorldController.keys.get(Keys.LEFT) && !WorldController.keys.get(Keys.RIGHT)) {
            stillTime += delta;
            ruben.getBody().setLinearVelocity(ruben.getVelocity().x * 0.9f, vel.y);
        } else {
            setStillTime(0);
        }

        ruben.velocityLimit();
    }

    public void applyImpulses(Vector2 vel, Vector2 pos) {
        // apply left impulse, but only if max velocity is not reached yet
        if (WorldController.keys.get(Keys.LEFT) && vel.x > -ruben.MAX_VELOCITY) {
            ruben.getBody().applyLinearImpulse(-2f, 0f, pos.x, pos.y, true);
        }

        // apply right impulse, but only if max velocity is not reached yet
        if (WorldController.keys.get(Keys.RIGHT) && vel.x < ruben.MAX_VELOCITY) {
            ruben.getBody().applyLinearImpulse(2f, 0, pos.x, pos.y, true);
        }

        // jump, but only when grounded
        if (WorldController.keys.get(Keys.JUMP)) {
            if (ruben.isGround()) {
                ruben.setState(Rubentxu.State.JUMPING);
                ruben.getBody().setLinearVelocity(vel.x, 0);
                ruben.getBody().setTransform(pos.x, pos.y + 0.01f, 0);
                ruben.getBody().applyLinearImpulse(0, ruben.JUMP_FORCE, pos.x, pos.y, true);
            }else if(!ruben.getState().equals(Rubentxu.State.JUMPING)) {
                ruben.setState(Rubentxu.State.FALL);
            }
        }
    }

    public void processContactGround() {
        if (!ruben.isGround()) {
            ruben.getRubenPhysicsFixture().setFriction(0f);
            ruben.getRubenSensorFixture().setFriction(0f);
            if (ruben.getVelocity().y <= 0 || !ruben.getState().equals(Rubentxu.State.JUMPING))
                ruben.setState(Rubentxu.State.FALL);
        } else {
            ruben.setState(Rubentxu.State.IDLE);
            if (WorldController.keys.get(Keys.LEFT)) {
                ruben.setFacingLeft(true);
                ruben.setState(Rubentxu.State.WALKING);
            } else if (WorldController.keys.get(Keys.RIGHT)) {
                ruben.setFacingLeft(false);
                ruben.setState(Rubentxu.State.WALKING);
            }
            if (!WorldController.keys.get(Keys.LEFT) && !WorldController.keys.get(Keys.RIGHT) && stillTime > 0.2) {
                ruben.getRubenPhysicsFixture().setFriction(100f);
                ruben.getRubenSensorFixture().setFriction(100f);
            } else {
                ruben.getRubenPhysicsFixture().setFriction(0.4f);
                ruben.getRubenSensorFixture().setFriction(0.4f);
            }
        }
    }

    @Override
    public void handleBeginContact(Contact contact) {
        //Gdx.app.log(DreamsGame.LOG, "Begin contact");

        if (contact.getFixtureA() == ruben.getRubenSensorFixture())
            ruben.getGrounContacts().add(contact.getFixtureB());//A is foot so B is ground

        if (contact.getFixtureB() == ruben.getRubenSensorFixture())
            ruben.getGrounContacts().add(contact.getFixtureA());//A is foot so B is ground

        if (ruben.getGrounContacts().size() > 0) {
            ruben.setGround(true);
            contact.setEnabled(true);
            ruben.getRubenPhysicsFixture().setFriction(0.2f);
            ruben.getRubenSensorFixture().setFriction(0.2f);
           // Gdx.app.log(DreamsGame.LOG, "OnGroun True");
        }


    }

    @Override
    public void handleEndContact(Contact contact) {
        //Gdx.app.log(DreamsGame.LOG, "End contact");

        if (contact.getFixtureA() == ruben.getRubenSensorFixture())
            ruben.getGrounContacts().remove(contact.getFixtureB());//A is foot so B is ground

        if (contact.getFixtureB() == ruben.getRubenSensorFixture())
            ruben.getGrounContacts().remove(contact.getFixtureA());//A is foot so B is ground

        if (ruben.getGrounContacts().size() == 0) {
            ruben.setGround(false);
            contact.resetFriction();
            //Gdx.app.log(DreamsGame.LOG, "OnGroun False");
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

    public void setStillTime(int stillTime) {
        this.stillTime = stillTime;
    }
}
