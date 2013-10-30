package com.rubentxu.juegos.core.controladores;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.rubentxu.juegos.core.managers.RubentxuManager;
import com.rubentxu.juegos.core.modelo.Rubentxu;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WorldControllerTest {

    private static WorldController worldController;

    @BeforeClass
    public static void testSetup() {
        worldController = new WorldController();
    }

    @Test
    public void testLeftPressed() {
        worldController.leftPressed();
        assertTrue(WorldController.keys.get(WorldController.Keys.LEFT));
    }

    @Test
    public void testLeftReleased() {
        worldController.leftReleased();
        assertFalse(WorldController.keys.get(WorldController.Keys.LEFT));
    }


    @Test
    public void testRightPressed() {
        worldController.rightPressed();
        assertTrue(WorldController.keys.get(WorldController.Keys.RIGHT));
    }

    @Test
    public void testRightReleased() {
        worldController.rightReleased();
        assertFalse(WorldController.keys.get(WorldController.Keys.RIGHT));
    }

    @Test
    public void testJumpPressed() {
        worldController.jumpPressed();
        assertTrue(WorldController.keys.get(WorldController.Keys.JUMP));
    }

    @Test
    public void testJumpReleased() {
        worldController.jumpReleased();
        assertFalse(WorldController.keys.get(WorldController.Keys.JUMP));
    }

    @Test
    public void testFirePressed() {
        worldController.firePressed();
        assertTrue(WorldController.keys.get(WorldController.Keys.FIRE));
    }

    @Test
    public void testFireReleased() {
        worldController.fireReleased();
        assertFalse(WorldController.keys.get(WorldController.Keys.FIRE));
    }

}
