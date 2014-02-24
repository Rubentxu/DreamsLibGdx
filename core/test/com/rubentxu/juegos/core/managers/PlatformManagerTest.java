package com.rubentxu.juegos.core.managers;


public class PlatformManagerTest {

   /* private static PlatformManager platformManager;
    private static WorldController controller;
    private static World physic;
    private Vector2 velocity;
    private Contact contact;
    private Fixture fixture;
    private MovingPlatform m1;
    private MovingPlatform m2;
    private static Hero r;
    private WorldManifold manifold;


    private class WorldManifoldMock extends WorldManifold{}
    private class ManifoldMock extends Manifold{
        protected ManifoldMock(long addr) {
            super(addr);
        }
    }
    private class Fix extends Fixture{
        *//**
         * Constructs a new fixture
         *
         * @param addr the address of the fixture
         *//*
        protected Fix(Body bodyA, long addr) {
            super(bodyA, addr);
        }
    };

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
        fixture = new Fix(body1,1l);
        manifold= new WorldManifoldMock();
        m1= new MovingPlatform("M1", MOVING_PLATFORM, body1,
                0,0,3,5,4);

        m2= new MovingPlatform("M2", MOVING_PLATFORM,body2,
                5,-1,8,5,4 );
        r = new Hero(physic);
        r.createHero(0, 1.2f, 0.7f, 1.8f);
        r.setVelocity(new Vector2(1.2F,1.5F));
        fixture.setUserData(m1);
        manifold.getPoints()[0].x=13f;
        manifold.getPoints()[0].y=12f;

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

    @Test
    public void testGetMovingPlatform() {
        expect(contact.getFixtureA()).andReturn(r.getRubenSensorFixture()).anyTimes();
        expect(contact.getFixtureB()).andReturn(fixture).anyTimes();
        replay(contact);

        MovingPlatform platformResult = platformManager.getMovingPlatform(contact);
        assertEquals(m1,platformResult);
    }

    @Test
    public void testGetMovingPlatform2() {
        expect(contact.getFixtureB()).andReturn(r.getRubenSensorFixture()).anyTimes();
        expect(contact.getFixtureA()).andReturn(fixture).anyTimes();
        replay(contact);

        MovingPlatform platformResult = platformManager.getMovingPlatform(contact);
        assertEquals(m1,platformResult);
    }

    @Test
    public void testGetPassenger() {
        expect(contact.getFixtureA()).andReturn(r.getRubenSensorFixture()).anyTimes();
        expect(contact.getFixtureB()).andReturn(fixture).anyTimes();
        replay(contact);

        Box2DPhysicsObject passenger = platformManager.getPassenger(contact);
        assertEquals(r, passenger);
    }

    @Test
    public void testGetPassenger2() {
        expect(contact.getFixtureB()).andReturn(r.getRubenSensorFixture()).anyTimes();
        expect(contact.getFixtureA()).andReturn(fixture).anyTimes();
        replay(contact);

        Box2DPhysicsObject passenger = platformManager.getPassenger(contact);
        assertEquals(r, passenger);
    }

    @Test
    public void testGetRelativeVelocity() {
        platformManager.updateMovingPlatform(m1,0.4f);
        Vector2 velRelative = platformManager.getRelativeVelocity(m1, r, manifold.getPoints()[0]);
        System.out.println("Velocity Relative: "+ velRelative);
        assertTrue(velRelative.y < r.getBodyA().getLinearVelocity().y);
    }

    @Test
    public void testHandleBeginContact() {
        expect(contact.getFixtureA()).andReturn(r.getRubenSensorFixture()).anyTimes();
        expect(contact.getFixtureB()).andReturn(fixture).anyTimes();
        expect(contact.getWorldManifold()).andReturn(manifold).anyTimes();

        replay(contact);
        platformManager.updateMovingPlatform(m1,0.5f);
        platformManager.handleBeginContact(contact);
        assertTrue(m1.getPassengers().size()>0);


    }
    @Test
    public void testHandleBeginContactVelRelative() {
        r.setVelocity(new Vector2(3.2F,4F));
        expect(contact.getFixtureA()).andReturn(r.getRubenSensorFixture()).anyTimes();
        expect(contact.getFixtureB()).andReturn(fixture).anyTimes();
        expect(contact.getWorldManifold()).andReturn(manifold).anyTimes();

        replay(contact);
        platformManager.updateMovingPlatform(m1,0.5f);
        platformManager.handleBeginContact(contact);
        assertTrue(m1.getPassengers().size()>0);


    }

    @Test
    public void testHandleBeginContactVelRelative2() {
        r.setVelocity(new Vector2(3.2F,5F));
        expect(contact.getFixtureA()).andReturn(r.getRubenSensorFixture()).anyTimes();
        expect(contact.getFixtureB()).andReturn(fixture).anyTimes();
        expect(contact.getWorldManifold()).andReturn(manifold).anyTimes();

        replay(contact);
        platformManager.updateMovingPlatform(m1,0.5f);
        platformManager.handleBeginContact(contact);
        assertTrue("Num Pasenger: "+m1.getPassengers().size(),m1.getPassengers().size()==0);


    }

    @Test
    public void testHandleEndContact() {

        expect(contact.getFixtureA()).andReturn(r.getRubenSensorFixture()).anyTimes();
        expect(contact.getFixtureB()).andReturn(fixture).anyTimes();
        expect(contact.getWorldManifold()).andReturn(manifold).anyTimes();
        m1.getPassengers().add(r);

        replay(contact);
        platformManager.handleEndContact(contact);
        assertTrue("Num Pasenger: "+m1.getPassengers().size(),m1.getPassengers().size()==0);


    }

    @Test
    public void testHandlePresolve() {
        r.setVelocity(new Vector2(3.2F, 4F));
        expect(contact.getFixtureA()).andReturn(r.getRubenSensorFixture()).anyTimes();
        expect(contact.getFixtureB()).andReturn(fixture).anyTimes();
        expect(contact.getWorldManifold()).andReturn(manifold).anyTimes();
        expect(contact.isEnabled()).andReturn(true).anyTimes();

        replay(contact);

        platformManager.updateMovingPlatform(m1, 0.5f);
        platformManager.setEnabledContac(true);
        platformManager.handlePreSolve(contact, new ManifoldMock(1l));
        assertTrue("Contact is Enabled? " + contact.isEnabled(), contact.isEnabled());
        //assertEquals(100f, contact.getFriction(), 0f);



    }
*/

}
