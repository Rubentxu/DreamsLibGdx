package com.rubentxu.juegos.core.modelo;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxNativesLoader;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HeroTest {

    private static Hero r;

    @BeforeClass
    public static void testSetup() {
        GdxNativesLoader.load();
        com.badlogic.gdx.physics.box2d.World physic = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);
        r = new Hero(physic);
        r.createHero(0, 0, 0.7f, 1.8f);
    }

    @Test
    public void testVelocityIsUnderMax() {
        Vector2 velocity=new Vector2(1f, 0f);
        r.setVelocity(velocity);
        r.velocityLimit();
        assertEquals(velocity, r.getVelocity());
    }

    @Test
    public void testVelocityIsOverMax() {
        Vector2 velocity=new Vector2(Hero.MAX_VELOCITY +1, 0f);
        r.setVelocity(new Vector2(Hero.MAX_VELOCITY +1, 0f));
        r.velocityLimit();
        assertEquals(Hero.MAX_VELOCITY, r.getVelocity().x, 0f);
    }
}
