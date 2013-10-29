package com.rubentxu.juegos.core.managers;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.rubentxu.juegos.core.modelo.Rubentxu;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RubentxuManagerTest {

    @Test
    public void testVelocityIsUnderMax() {
        GdxNativesLoader.load();
        com.badlogic.gdx.physics.box2d.World physic = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);
        Rubentxu r = new Rubentxu(physic);
        assertFalse(r.isVelocityXOverMax());
    }


    @Test
    public void testVelocityIsOverMax() {
        GdxNativesLoader.load();
        com.badlogic.gdx.physics.box2d.World physic = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);
        Rubentxu r = new Rubentxu(physic);
        r.setVelocity(Rubentxu.MAX_VELOCITY +1, 0f);
        assertTrue(r.isVelocityXOverMax());

    }
}
