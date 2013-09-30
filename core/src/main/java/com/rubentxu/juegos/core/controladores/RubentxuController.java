package com.rubentxu.juegos.core.controladores;


import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.*;
import com.rubentxu.juegos.core.modelo.*;
import com.rubentxu.juegos.core.modelo.World;

import java.util.*;


public class RubentxuController {


    enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }

    private World world;
    private Rubentxu ruben;
    float stillTime = 0;

    static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();

    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.JUMP, false);
        keys.put(Keys.FIRE, false);
    }

    public RubentxuController(World world) {
        this.world = world;
        this.ruben = world.getRuben();

    }

    public void leftPressed() {
        keys.get(keys.put(Keys.LEFT, true));
    }

    public void rightPressed() {
        keys.get(keys.put(Keys.RIGHT, true));
    }

    public void jumpPressed() {
        keys.get(keys.put(Keys.JUMP, true));
    }

    public void firePressed() {
        keys.get(keys.put(Keys.FIRE, false));
    }

    public void leftReleased() {
        keys.get(keys.put(Keys.LEFT, false));
    }

    public void rightReleased() {
        keys.get(keys.put(Keys.RIGHT, false));
    }

    public void jumpReleased() {
        keys.get(keys.put(Keys.JUMP, false));
        //jumpingPressed = false;
    }

    public void fireReleased() {
        keys.get(keys.put(Keys.FIRE, false));
    }

    /**
     * The main update method *
     */
    public void update(float delta) {
        processInput(delta);
    }

    public boolean processInput(float delta) {

        Vector2 vel = ruben.getBody().getLinearVelocity();
        Vector2 pos = ruben.getBody().getPosition();
        ruben.setGrounded( isPlayerGrounded(delta));


        // cap max velocity on x
        if(Math.abs(vel.x) > ruben.MAX_VELOCITY) {
            vel.x = Math.signum(vel.x) * ruben.MAX_VELOCITY;
            ruben.getBody().setLinearVelocity(vel.x, vel.y);
        }

        // calculate stilltime & damp
        if(!keys.get(Keys.LEFT) && !keys.get(Keys.RIGHT)) {
            stillTime += Gdx.graphics.getDeltaTime();
            ruben.getBody().setLinearVelocity(vel.x * 0.9f, vel.y);
        }
        else {
            stillTime = 0;
        }

        if(!ruben.isGrounded()) {
            ruben.getRubenPhysicsFixture().setFriction(0f);
            ruben.getRubenSensorFixture().setFriction(0f);
            if(!ruben.getState().equals(Rubentxu.State.JUMPING)) ruben.setState(Rubentxu.State.FALL);
        } else {
            ruben.setState(Rubentxu.State.IDLE);
            if (keys.get(Keys.LEFT)) {
                ruben.setFacingLeft(true);
                ruben.setState(Rubentxu.State.WALKING);
            } else if (keys.get(Keys.RIGHT)) {
                ruben.setFacingLeft(false);
                ruben.setState(Rubentxu.State.WALKING);
            }
            if(!keys.get(Keys.LEFT) && !keys.get(Keys.RIGHT) && stillTime > 0.2) {
                ruben.getRubenPhysicsFixture().setFriction(100f);
                ruben.getRubenSensorFixture().setFriction(100f);
            }
            else {
                ruben.getRubenPhysicsFixture().setFriction(0.2f);
                ruben.getRubenSensorFixture().setFriction(0.2f);
            }

           /* if(groundedPlatform != null && groundedPlatform.dist == 0) {
                player.applyLinearImpulse(0, -24, pos.x, pos.y);
            }*/
        }

        // apply left impulse, but only if max velocity is not reached yet
        if(keys.get(Keys.LEFT) && vel.x > -ruben.MAX_VELOCITY) {
            ruben.getBody().applyLinearImpulse(-2f, 0f, pos.x, pos.y, true);
        }

        // apply right impulse, but only if max velocity is not reached yet
        if(keys.get(Keys.RIGHT) && vel.x < ruben.MAX_VELOCITY) {
            ruben.getBody().applyLinearImpulse(2f, 0, pos.x, pos.y, true);
        }

        // jump, but only when grounded
        if(keys.get(Keys.JUMP)) {
            ruben.setState(Rubentxu.State.JUMPING);
            if(ruben.isGrounded()) {
                ruben.getBody().setLinearVelocity(vel.x, 0);
                System.out.println("jump before: " + ruben.getBody().getLinearVelocity());
                ruben.getBody().setTransform(pos.x, pos.y + 0.01f, 0);
                ruben.getBody().applyLinearImpulse(0, ruben.JUMP_FORCE, pos.x, pos.y, true);
                System.out.println("jump, " + ruben.getBody().getLinearVelocity());
            }
        }

        return false;
    }

    public boolean isPlayerGrounded(float deltaTime) {
        //groundedPlatform = null;
        Array<Contact> contactList =  world.getPhysics().getContactList();
        for(int i = 0; i < contactList.size; i++) {
            Contact contact = contactList.get(i);
            if(contact.isTouching() && (contact.getFixtureA() == ruben.getRubenSensorFixture() ||
                    contact.getFixtureB() == ruben.getRubenSensorFixture())) {

                Vector2 pos = ruben.getBody().getPosition();
                WorldManifold manifold = contact.getWorldManifold();
                float anguloColision;
                anguloColision= new Vector2(manifold.getNormal().x,manifold.getNormal().y).angle()
                        ;
                anguloColision= (float) ((anguloColision * 180f / Math.PI) + 360f) % 360f ;
                 if (anguloColision > 45f && anguloColision < 135f) return true;



             /*   boolean below = true;
                for(int j = 0; j < manifold.getNumberOfContactPoints(); j++) {
                    below = (manifold.getPoints()[j].y < pos.y - 1.8f);
                    //if (below) return true;
                }
                if(manifold.getNormal().y > pos.y-ruben.getHeight()) return true;

                if(below) {
                   *//* if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("p")) {
                        groundedPlatform = (MovingPlatform)contact.getFixtureA().getBody().getUserData();
                    }

                    if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("p")) {
                        groundedPlatform = (MovingPlatform)contact.getFixtureB().getBody().getUserData();
                    }*//*
                    return true;
                }*/

                return false;
            }
        }
        return false;
    }


}
