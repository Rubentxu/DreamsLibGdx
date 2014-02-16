package com.rubentxu.juegos.core.managers;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.managers.world.HeroManager;
import com.rubentxu.juegos.core.modelo.Hero;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HeroManagerTest {

    private static HeroManager heroManager;
    private static Hero r;
    private static WorldController controller;
    private static World physic;
    private Vector2 velocity;
    private Contact contact;
    private Fixture fixMock;

    @BeforeClass
    public static void testSetup() {
        GdxNativesLoader.load();
        physic= new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);
        r = new Hero(physic);
        r.createHero(0, 0, 0.7f, 1.8f);

        r.getRubenPhysicsFixture().setFriction(0);
        r.getRubenSensorFixture().setFriction(0);
        heroManager = new HeroManager(r);
        controller=new WorldController(game, new com.rubentxu.juegos.core.modelo.World());
    }

    @Before
    public void setup(){
        velocity=new Vector2(0, 0f);
        r.setVelocity(velocity);
        contact = createNiceMock(Contact.class);
        fixMock= createNiceMock(Fixture.class);
        r.getGrounContacts().clear();
        controller.rightReleased();
        controller.leftReleased();
        controller.jumpReleased();
        r.setGround(true);
    }


    @Test
    public void testUpdate() {
        heroManager.update(1);
        assertEquals(0,r.getVelocity().x, 0);
    }

    /* Test sobre el metodo applyImpulses */
    @Test
    public void testApplyImpulses() {
        Vector2 vel = r.getVelocity();
        Vector2 pos = r.getBody().getPosition();

        heroManager.applyImpulses(vel,pos);
        assertEquals(vel.x, r.getVelocity().x,0);
    }

    @Test
    public void testApplyImpulsesLeft() {
        controller.leftPressed();
        heroManager.applyImpulses(r.getVelocity(), r.getBody().getPosition());
        assertTrue(r.getVelocity().toString(), velocity.x > r.getVelocity().x);
    }

    @Test
    public void testApplyImpulsesRight() {
        controller.rightPressed();
        heroManager.applyImpulses(r.getVelocity(), r.getBody().getPosition());
        assertTrue(r.getVelocity().toString(), velocity.x < r.getVelocity().x);
    }

    @Test
    public void testApplyImpulsesJump() {
        controller.jumpPressed();
        heroManager.applyImpulses(r.getVelocity(), r.getBody().getPosition());
        assertTrue(r.getVelocity().toString(), velocity.y < r.getVelocity().y);
        assertEquals(Hero.State.JUMPING,r.getState());

    }

    @Test
    public void testNoApplyImpulsesJump() {
        controller.jumpPressed();
        r.setGround(false);
        r.setState(Hero.State.IDLE);

        heroManager.applyImpulses(r.getVelocity(), r.getBody().getPosition());
        assertEquals( velocity.y , r.getVelocity().y,0);
        assertEquals(Hero.State.FALL,r.getState());

    }

    @Test
    public void testApplyImpulsesRightPressedAndLeftPressed() {
        controller.leftPressed();
        controller.rightPressed();
        heroManager.applyImpulses(r.getVelocity(), r.getBody().getPosition());
        assertEquals( velocity.x , r.getVelocity().x,0);
    }

    @Test
    public void testApplyImpulsesRightPressedAndLeftPressedAndJump() {
        controller.leftPressed();
        controller.jumpPressed();
        controller.rightPressed();

        heroManager.applyImpulses(r.getVelocity(), r.getBody().getPosition());
        assertTrue( velocity.y < r.getVelocity().y);

    }
     /* Test sobre el metodo processVelocity */
    @Test
    public void testProcessVelocityMAX_VELOCITY() {
        velocity=new Vector2(10, 0f);
        r.setVelocity(velocity);

        heroManager.processVelocity(velocity,1f);
        assertEquals(r.MAX_VELOCITY, r.getVelocity().x, 0);
    }

    @Test
    public void testProcessVelocityRightPressed() {
        velocity=new Vector2(10, 0f);
        r.setVelocity(velocity);
        controller.rightPressed();

        heroManager.processVelocity(velocity,1f);
        assertEquals( r.MAX_VELOCITY, r.getVelocity().x,0);
    }

    @Test
    public void testProcessVelocityRightPressed2() {
        velocity=new Vector2(5, 0f);
        r.setVelocity(velocity);
        controller.rightPressed();

        heroManager.processVelocity(velocity,1f);
        assertEquals( velocity.x, r.getVelocity().x,0);
    }

    @Test
    public void testProcessVelocity() {
        velocity=new Vector2(5, 0f);
        r.setVelocity(velocity);
        heroManager.processVelocity(velocity,1f);
        assertTrue(velocity.x > r.getVelocity().x);
    }

    /* Test sobre el metodo processContactGround */

    @Test
    public void testProcessContactGroundisGroundFalse() {
        r.setGround(false);

        heroManager.processContactGround();
        assertEquals(r.getState(), Hero.State.FALL);
        assertEquals(r.getRubenPhysicsFixture().getFriction(), 0, 0);
    }

    @Test
    public void testProcessContactGroundisGroundFalseAndJumping() {
        velocity=new Vector2(0, 2f);
        r.setVelocity(velocity);
        r.setGround(false);
        r.setState(Hero.State.JUMPING);

        heroManager.processContactGround();
        assertTrue( r.getState().equals(Hero.State.JUMPING));
        assertEquals(r.getRubenPhysicsFixture().getFriction(), 0, 0);
    }

    @Test
    public void testProcessContactGroundisGroundTrue() {
        heroManager.setStillTime(0);
        heroManager.processContactGround();
        assertEquals(r.getState(), Hero.State.IDLE);
        assertEquals(r.getRubenPhysicsFixture().getFriction() , 0.2,.1);
    }

    @Test
    public void testProcessContactGroundisGroundTrueStillTime1() {
        heroManager.setStillTime(1);
        heroManager.processContactGround();
        assertEquals( r.getState(), Hero.State.IDLE);
        assertEquals(r.getRubenPhysicsFixture().getFriction() , 100,.1);
    }

    @Test
    public void testProcessContactGroundisGroundTrueRightPressed() {
        controller.rightPressed();
        heroManager.processContactGround();
        assertEquals(r.getState(), Hero.State.WALKING);
        assertFalse(r.isFacingLeft());
        assertEquals(r.getRubenPhysicsFixture().getFriction() , 0.2,.1);
    }

    @Test
    public void testProcessContactGroundisGroundTrueLeftPressed() {
        controller.leftPressed();

        heroManager.processContactGround();
        assertEquals(r.getState(), Hero.State.WALKING);
        assertTrue(r.isFacingLeft());
        assertEquals(r.getRubenPhysicsFixture().getFriction() , 0.2,.1);
    }

    @Test
    public void testhandleBeginContact() {
        expect(contact.getFixtureA()).andReturn(r.getRubenSensorFixture()).anyTimes();
        expect(contact.getFixtureB()).andReturn(fixMock).anyTimes();
        replay(contact);

        heroManager.handleBeginContact(contact);
        assertTrue(r.getGrounContacts().contains(fixMock));
        assertTrue(r.isGround());

    }

    @Test
    public void testhandleBeginContact2() {
        expect(contact.getFixtureB()).andReturn(r.getRubenSensorFixture()).anyTimes();
        expect(contact.getFixtureA()).andReturn(fixMock).anyTimes();
        replay(contact);

        heroManager.handleBeginContact(contact);
        assertTrue(r.getGrounContacts().contains(fixMock));
        assertTrue(r.isGround());
    }

    @Test
    public void testhandleBeginContact3() {
        expect(contact.getFixtureA()).andReturn(fixMock).anyTimes();
        replay(contact);
        r.getGrounContacts().clear();
        heroManager.handleBeginContact(contact);
        assertTrue(r.getGrounContacts().isEmpty());
    }

    @Test
    public void testhandleEndContact() {
        r.getGrounContacts().add(fixMock);
        expect(contact.getFixtureA()).andReturn(r.getRubenSensorFixture()).anyTimes();
        expect(contact.getFixtureB()).andReturn(fixMock).anyTimes();
        replay(contact);
        heroManager.handleEndContact(contact);

        assertTrue(r.getGrounContacts().isEmpty());
        assertFalse(r.isGround());
    }

    @Test
    public void testhandleEndContact2() {
        r.getGrounContacts().add(fixMock);
        expect(contact.getFixtureB()).andReturn(r.getRubenSensorFixture()).anyTimes();
        expect(contact.getFixtureA()).andReturn(fixMock).anyTimes();
        replay(contact);

        heroManager.handleEndContact(contact);
        assertTrue(r.getGrounContacts().isEmpty());
        assertFalse(r.isGround());
    }

    @Test
    public void testhandleEndContact3() {
        r.getGrounContacts().add(fixMock);
        expect(contact.getFixtureA()).andReturn(fixMock).anyTimes();
        replay(contact);

        heroManager.handleEndContact(contact);
        assertFalse(r.getGrounContacts().isEmpty());
    }




}
