package com.rubentxu.juegos.core.controladores;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.rubentxu.juegos.core.controladores.WorldController.Keys;
import com.rubentxu.juegos.core.managers.RubentxuManager;
import com.rubentxu.juegos.core.modelo.Rubentxu;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WorldControllerTest {

    WorldController worldController;

    @Before
    public void testSetup() {
        worldController = new WorldController();
    }

    @Test
    public void testLeftPressed() {
        worldController.leftPressed();
        assertWorldContainsKey(WorldController.Keys.LEFT);
        
    }

    private void assertWorldContainsKey(Keys key) {
    	assertTrue(WorldController.keys.get(key));
		
	}

	@Test
    public void testLeftReleased() {
        worldController.leftReleased();
        assertFalse(WorldController.keys.get(WorldController.Keys.LEFT));
    }


    @Test
    public void testRightPressed() {
        worldController.rightPressed();
        assertWorldContainsKey(WorldController.Keys.RIGHT);
    }

    @Test
    public void testRightReleased() {
        worldController.rightReleased();
        assertFalse(WorldController.keys.get(WorldController.Keys.RIGHT));
    }

    @Test
    public void testJumpPressed() {
        worldController.jumpPressed();
        assertWorldContainsKey(WorldController.Keys.JUMP);
    }

    @Test
    public void testJumpReleased() {
        worldController.jumpReleased();
        assertFalse(WorldController.keys.get(WorldController.Keys.JUMP));
    }

    @Test
    public void testFirePressed() {
        worldController.firePressed();
        assertWorldContainsKey(WorldController.Keys.FIRE);
    }

    @Test
    public void testFireReleased() {
        worldController.fireReleased();
        assertFalse(WorldController.keys.get(WorldController.Keys.FIRE));
    }

}
