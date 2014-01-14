package com.rubentxu.juegos.core.managers.world;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.controladores.WorldController.Keys;
import com.rubentxu.juegos.core.managers.interfaces.AbstractWorldManager;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Rubentxu;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;


public class RubentxuManager extends AbstractWorldManager {

    private Rubentxu ruben;
    private float stillTime = 0;
    private float hurtTime = 0;

    public RubentxuManager(World world) {
        super(world);
        this.ruben = world.getRuben();
    }

    public void update(float delta) {
        hurtTime += delta;
        Vector2 vel = ruben.getVelocity();
        Vector2 pos = ruben.getBody().getPosition();
        if(!ruben.getState().equals(Rubentxu.State.HURT) && hurtTime>1.5f){
            processVelocity(vel, delta);
            processContactGround();
            applyImpulses(vel, pos);
        }

            if(hurtTime>2)hurtTime=2;


    }

    public void processVelocity(Vector2 vel,float delta) {

        if (!WorldController.keys.get(Keys.LEFT) && !WorldController.keys.get(Keys.RIGHT)) {
            stillTime += delta;
            ruben.getBody().setLinearVelocity(ruben.getVelocity().x * 0.9f, vel.y);
        } else {
            setStillTime(0);
        }

        ruben.velocityLimit();
    }

    public void applyImpulses(Vector2 vel, Vector2 pos) {
        // apply left impulse, but only if max velocity is not reached yet
        if (WorldController.keys.get(Keys.LEFT) && vel.x > -ruben.MAX_VELOCITY) {
            ruben.getBody().applyLinearImpulse(-3f, 0f, pos.x, pos.y, true);
        }

        // apply right impulse, but only if max velocity is not reached yet
        if (WorldController.keys.get(Keys.RIGHT) && vel.x < ruben.MAX_VELOCITY) {
            ruben.getBody().applyLinearImpulse(3f, 0, pos.x, pos.y, true);
        }

        // jump, but only when grounded
        if (WorldController.keys.get(Keys.JUMP)) {
            if (ruben.isGround()) {
                ruben.setState(Rubentxu.State.JUMPING);
                ruben.getBody().setLinearVelocity(vel.x, 0);
                ruben.getBody().setTransform(pos.x, pos.y + 0.01f, 0);
                ruben.getBody().applyLinearImpulse(0, ruben.JUMP_FORCE, pos.x, pos.y, true);
            }else if(!ruben.getState().equals(Rubentxu.State.JUMPING)) {
                ruben.setState(Rubentxu.State.FALL);
            }
        }
    }

    public void processContactGround() {

        if (!ruben.isGround()) {
            ruben.getRubenPhysicsFixture().setFriction(0f);
            ruben.getRubenSensorFixture().setFriction(0f);
            if (ruben.getVelocity().y <= 0 || !ruben.getState().equals(Rubentxu.State.JUMPING))
                ruben.setState(Rubentxu.State.FALL);
        } else {
            if(!ruben.getState().equals(Rubentxu.State.SWIMMING)) ruben.setState(Rubentxu.State.IDLE);
            if (WorldController.keys.get(Keys.LEFT)  ) {
                ruben.setFacingLeft(true);
                if(!ruben.getState().equals(Rubentxu.State.SWIMMING)) ruben.setState(Rubentxu.State.WALKING);
            } else if (WorldController.keys.get(Keys.RIGHT) ) {
                ruben.setFacingLeft(false);
                if(!ruben.getState().equals(Rubentxu.State.SWIMMING)) ruben.setState(Rubentxu.State.WALKING);
            }
            if (!WorldController.keys.get(Keys.LEFT) && !WorldController.keys.get(Keys.RIGHT) && stillTime > 1
                    && !ruben.getState().equals(Rubentxu.State.HURT)) {
                ruben.getRubenPhysicsFixture().setFriction(100f);
                ruben.getRubenSensorFixture().setFriction(100f);
            } else {
                ruben.getRubenPhysicsFixture().setFriction(0.2f);
                ruben.getRubenSensorFixture().setFriction(0.2f);
            }
        }
    }

    public Enemy getEnemy(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (box2dPhysicsA.getGrupo().equals(Box2DPhysicsObject.GRUPOS.ENEMIGOS)) {
            return (Enemy) box2dPhysicsA;
        } else if(box2dPhysicsB.getGrupo().equals(Box2DPhysicsObject.GRUPOS.ENEMIGOS)){
            return (Enemy) box2dPhysicsB;
        } else {
            return null;
        }
    }

    public Rubentxu getRuben(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (box2dPhysicsA.getGrupo().equals(Box2DPhysicsObject.GRUPOS.HEROES)) {
            return (Rubentxu) box2dPhysicsA;
        } else {
            return (Rubentxu) box2dPhysicsB;
        }
    }

    public Boolean existSensor (Contact contact) {

        if (contact.getFixtureA() == ruben.getRubenSensorFixture() ||
                contact.getFixtureB() == ruben.getRubenSensorFixture()){
            return true;
        }

        return false;
    }



    @Override
    public void handleBeginContact(Contact contact) {
        //Gdx.app.log(DreamsGame.LOG, "Begin contact");
        if (contact.getFixtureA() == ruben.getRubenSensorFixture()){
            ruben.getGrounContacts().add(contact.getFixtureB());//A is foot so B is ground
        }


        if (contact.getFixtureB() == ruben.getRubenSensorFixture()) {
            ruben.getGrounContacts().add(contact.getFixtureA());//A is foot so B is ground
        }


        if (ruben.getGrounContacts().size() > 0) {
            ruben.setGround(true);
            contact.setEnabled(true);

           // Gdx.app.log(DreamsGame.LOG, "OnGroun True");
        }
        if(getEnemy(contact)!=null) {
            ruben.setState(Rubentxu.State.HURT);
            hurtTime=0;
            Vector2[] points = contact.getWorldManifold().getPoints();
            Vector2 force;
            if (points[0].x < ruben.getBody().getPosition().x ) {
                force=new Vector2(9,15);
            }else {
                force=new Vector2(-9,15);
            }
            System.out.println("Fuerza colision Enemigo: "+force +" Sensor no exist");
            ruben.getBody().applyLinearImpulse(force,ruben.getBody().getWorldCenter(),true);

        }


    }

    @Override
    public void handleEndContact(Contact contact) {
        //Gdx.app.log(DreamsGame.LOG, "End contact");

        if (contact.getFixtureA() == ruben.getRubenSensorFixture())
            ruben.getGrounContacts().remove(contact.getFixtureB());//A is foot so B is ground

        if (contact.getFixtureB() == ruben.getRubenSensorFixture())
            ruben.getGrounContacts().remove(contact.getFixtureA());//A is foot so B is ground


        if (ruben.getGrounContacts().size() == 0) {
            ruben.setGround(false);

            //Gdx.app.log(DreamsGame.LOG, "OnGroun False");
        }
        if(getEnemy(contact)!=null) {
            ruben.setState(Rubentxu.State.IDLE);
        }
    }

    @Override
    public void handlePostSolve(Contact contact, ContactImpulse impulse) {
        float impulseN = impulse.getNormalImpulses()[0];


    }

    @Override
    public void handlePreSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public boolean handleShouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return true;
    }

    public void setStillTime(int stillTime) {
        this.stillTime = stillTime;
    }
}
