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


    static final float BOX_STEP=1/60f;
    static final int BOX_VELOCITY_ITERATIONS=6;
    static final int BOX_POSITION_ITERATIONS=2;  ;
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
        rubentxuManager = new RubentxuManager(r);
        controller=new WorldController();
    }

    @Before
    public void setup(){

    }


    @Test
    public void testApplyImpulses() {
        Vector2 vel = r.getVelocity();
        Vector2 pos = r.getBody().getPosition();
        rubentxuManager.applyImpulses(vel,pos);

        assertEquals(vel.x, r.getVelocity().x,0);
    }

    @Test
    public void testApplyImpulsesLeft() {
        Vector2 velocity=new Vector2(Rubentxu.MAX_VELOCITY -1, 0f);
        r.setVelocity(velocity);
        Vector2 pos = r.getBody().getPosition();
        controller.leftPressed();
        

        rubentxuManager.applyImpulses(r.getVelocity(), r.getBody().getPosition());
        physic.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);

        assertTrue(r.getVelocity().toString(), velocity.x > r.getVelocity().x);
    }

    @Test
    public void testApplyImpulsesRight() {
        Vector2 velocity=new Vector2(Rubentxu.MAX_VELOCITY -1, 0f);
        r.setVelocity(velocity);
        Vector2 pos = r.getBody().getPosition();
        controller.rightPressed();
        rubentxuManager.applyImpulses(r.getVelocity(), r.getBody().getPosition());
        //physic.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
        assertTrue(r.getVelocity().toString(), velocity.x < r.getVelocity().x);
        System.out.println(r.getVelocity().toString());
    }
}
