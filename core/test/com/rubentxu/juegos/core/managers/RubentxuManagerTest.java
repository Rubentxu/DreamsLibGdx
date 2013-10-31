package com.rubentxu.juegos.core.managers;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.modelo.Rubentxu;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RubentxuManagerTest {


    static final float BOX_STEP=1/3f;
    static final int BOX_VELOCITY_ITERATIONS=10;
    static final int BOX_POSITION_ITERATIONS=10;
    private static RubentxuManager rubentxuManager;
    private static Rubentxu r;
    private static WorldController controller;
    private static World physic;

    @BeforeClass
    public static void testSetup() {
        GdxNativesLoader.load();
        physic= new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);
        r = new Rubentxu(physic);
        r.createRubenxu(0,0,0.7f,1.8f);

        r.getRubenPhysicsFixture().setFriction(0);
        r.getRubenSensorFixture().setFriction(0);
        rubentxuManager = new RubentxuManager(r);
        controller=new WorldController();
    }

    @Test
    public void testApplyImpulses() {
        Vector2 vel = r.getVelocity();
        Vector2 pos = r.getBody().getPosition();
        controller.rightReleased();
        controller.jumpReleased();
        controller.leftReleased();

        rubentxuManager.applyImpulses(vel,pos);
        assertEquals(vel.x, r.getVelocity().x,0);
    }

    @Test
    public void testApplyImpulsesLeft() {
        Vector2 velocity=new Vector2(0, 0f);
        r.setVelocity(velocity);
        controller.rightReleased();
        controller.jumpReleased();
        controller.leftPressed();

        rubentxuManager.applyImpulses(r.getVelocity(), r.getBody().getPosition());
        //physic.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
        System.out.println("Left "+r.getVelocity().toString());
        assertTrue(r.getVelocity().toString(), velocity.x > r.getVelocity().x);
    }

    @Test
    public void testApplyImpulsesRight() {
        Vector2 velocity=new Vector2(0, 0f);
        r.setVelocity(velocity);
        controller.leftReleased();
        controller.jumpReleased();
        controller.rightPressed();

        rubentxuManager.applyImpulses(r.getVelocity(), r.getBody().getPosition());
       // physic.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
        System.out.println("Right "+r.getVelocity().toString());
        assertTrue(r.getVelocity().toString(), velocity.x < r.getVelocity().x);

    }

    @Test
    public void testApplyImpulsesJump() {
        Vector2 velocity=new Vector2(0, 0f);
        r.setVelocity(velocity);
        controller.leftReleased();
        controller.rightReleased();
        controller.jumpPressed();
        r.setGround(true);

        rubentxuManager.applyImpulses(r.getVelocity(), r.getBody().getPosition());
        // physic.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
        System.out.println("Jump "+r.getVelocity().toString());
        assertTrue(r.getVelocity().toString(), velocity.y < r.getVelocity().y);
        assertEquals(Rubentxu.State.JUMPING,r.getState());

    }

    @Test
    public void testNoApplyImpulsesJump() {
        Vector2 velocity=new Vector2(0, 0f);
        r.setVelocity(velocity);
        controller.leftReleased();
        controller.rightReleased();
        controller.jumpPressed();
        r.setGround(false);
        r.setState(Rubentxu.State.IDLE);

        rubentxuManager.applyImpulses(r.getVelocity(), r.getBody().getPosition());
        // physic.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
        System.out.println("Jump "+r.getVelocity().toString());
        assertEquals( velocity.y , r.getVelocity().y,0);
        assertEquals(Rubentxu.State.FALL,r.getState());

    }

    @Test
    public void testApplyImpulsesRightPressedAndLeftPressed() {
        Vector2 velocity=new Vector2(0, 0f);
        r.setVelocity(velocity);
        controller.leftPressed();
        controller.jumpReleased();
        controller.rightPressed();

        rubentxuManager.applyImpulses(r.getVelocity(), r.getBody().getPosition());
        // physic.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
        System.out.println("RightAndLef "+r.getVelocity().toString());
        assertEquals( velocity.x , r.getVelocity().x,0);

    }

    @Test
    public void testApplyImpulsesRightPressedAndLeftPressedAndJump() {
        Vector2 velocity=new Vector2(0, 0f);
        r.setVelocity(velocity);
        controller.leftPressed();
        controller.jumpPressed();
        controller.rightPressed();
        r.setGround(true);

        rubentxuManager.applyImpulses(r.getVelocity(), r.getBody().getPosition());
        // physic.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
        System.out.println("RightAndLefAndJump "+r.getVelocity().toString());
        assertTrue( velocity.y < r.getVelocity().y);

    }

    @Test
    public void testProcessVelocity() {
        Vector2 velocity=new Vector2(10, 0f);
        r.setVelocity(velocity);
        controller.leftReleased();
        controller.jumpReleased();
        controller.rightReleased();
        r.setGround(true);

        rubentxuManager.processVelocity(velocity,1f);
        System.out.println("Velocity Process "+r.getVelocity().toString());
        assertTrue( r.getVelocity().x < r.MAX_VELOCITY);
    }

    @Test
    public void testProcessVelocity2() {
        Vector2 velocity=new Vector2(10, 0f);
        r.setVelocity(velocity);
        controller.leftReleased();
        controller.jumpReleased();
        controller.rightPressed();
        r.setGround(true);

        rubentxuManager.processVelocity(velocity,1f);
        System.out.println("Velocity Process "+r.getVelocity().toString());
        assertEquals( r.MAX_VELOCITY, r.getVelocity().x,0);
    }

}
