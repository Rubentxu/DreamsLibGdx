package com.rubentxu.juegos.core.managers;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.modelo.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.MovingPlatform;
import com.rubentxu.juegos.core.modelo.Rubentxu;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.rubentxu.juegos.core.modelo.Box2DPhysicsObject.GRUPOS.PLATAFORMAS_MOVILES;
import static org.easymock.EasyMock.createNiceMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlatformManagerTest {

    private static PlatformManager platformManager;
    private static WorldController controller;
    private static World physic;
    private Vector2 velocity;
    private Contact contact;
    private Fixture fixMock;
    private MovingPlatform m1;
    private MovingPlatform m2;
    private static Rubentxu r;


    private class MyContact extends Contact {
        public MyContact(World physic, Long addr) {
            super(physic, addr);
        }
    }

    @BeforeClass
    public static void testSetup() {
        GdxNativesLoader.load();
        physic= new World(new Vector2(0, -9.81f), true);
        platformManager = new PlatformManager();
    }

    private Body createBox(BodyDef.BodyType type, float width, float height, float density){
        BodyDef def= new BodyDef();
        def.type= type;
        Body box = physic.createBody(def);

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width, height);
        box.createFixture(poly, density);
        poly.dispose();

        return box;
    }

    @Before
    public void setup(){
        Body body1 = createBox(BodyDef.BodyType.KinematicBody, 4, 0.5f, 1);
        Body body2 = createBox(BodyDef.BodyType.KinematicBody, 4, 1, 1);
        contact = createNiceMock(Contact.class);
        fixMock= createNiceMock(Fixture.class);
        m1= new MovingPlatform("M1", PLATAFORMAS_MOVILES, body1,
                68,5,64,9,4);

        m2= new MovingPlatform("M2", PLATAFORMAS_MOVILES,body2,
                78,4,80,9,4 );
        r = new Rubentxu(physic);
        r.createRubenxu(0,0,0.7f,1.8f);
    }


    @Test
    public void testUpdateMovingPlatform() {
        float delta=1;
        float distance = m1.getDistance();
        platformManager.updateMovingPlatform(m1,delta);
        assertFalse(m1.getForward());
        assertEquals(m1.getVelocity(), m1.getpVelocity());
        assertEquals(distance + m1.getVelocity().len() * delta ,m1.getDistance(),0);
        System.out.println("Distance1: "+ m1.getDistance());
    }

    @Test
    public void testUpdateMovingPlatformExceededMaxDist() {
        float delta=2;
        float distance = m1.getDistance();
        platformManager.updateMovingPlatform(m1,delta);
        assertTrue(m1.getForward());
        assertTrue(m1.getDistance() == 0);
        assertTrue(m1.getMaxDist() < distance + m1.getVelocity().len() * delta);
        platformManager.updateMovingPlatform(m1,0.1f);
        assertEquals(m1.getVelocity(), m1.getpVelocity().scl(-1f));
        System.out.println("Distance2: "+ m1.getDistance());
    }

    @Test
    public void testUpdateMovingPlatformDisabled() {
        float delta=2;
        m1.enabled=false;
        platformManager.updateMovingPlatform(m1,delta);
        assertEquals(m1.getVelocity(), new Vector2(0,0));
        System.out.println("Distance3: "+ m1.getDistance());
    }



}
