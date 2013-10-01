package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DUtils;


public class Rubentxu extends Box2DPhysicsObject implements ContactListener, ContactFilter {

    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return false;
    }

    public enum State {
        IDLE, WALKING, JUMPING, DYING, FALL
    }

    public final static float MAX_VELOCITY = 7f;
    public final static float JUMP_FORCE = 58f;
    protected boolean onGround = true;
    State state = State.IDLE;
    boolean facingLeft = true;
    private float killVelocity;
    private Boolean hurt;
    private float hurtVelocityY = 10f;
    private float hurtVelocityX = 6f;
    private float springOffEnemy = -1f;
    private Array<Fixture> grounContacts;
    private float combinedGroundAngle = 0f;
    private Fixture rubenPhysicsFixture;
    private Fixture rubenSensorFixture;

    public Rubentxu(World world, float x, float y, float width, float height) {
        super("Heroe", grupos.HEROES, world.getPhysics(), x, y, width, height, 0);
        grounContacts= new Array<Fixture>();
        createRubenxu(world, x, y, width, height);
    }

    public void createRubenxu(World world, float x, float y, float width, float height) {
        defineBody();
        createBody();
        super.getBody().setFixedRotation(true);
        super.getBody().setSleepingAllowed(false);

        PolygonShape poly = (PolygonShape) createShape(width, height, 0);
        rubenPhysicsFixture = createFixture(defineFixture(poly));
        rubenPhysicsFixture.setRestitution(0);
        poly.dispose();

        PolygonShape sensor = (PolygonShape) createShape(width * 0.9f, height / 5, new Vector2(0, -height * 0.9f), 0);
        rubenSensorFixture = createFixture(defineFixture(sensor));
        rubenSensorFixture.setRestitution(0);
        sensor.dispose();

        super.getBody().setBullet(true);

    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public Vector2 getVelocity() {
        return super.getBody().getLinearVelocity();
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }

    public Body getBody() {
        return super.getBody();
    }

    public Fixture getRubenPhysicsFixture() {
        return rubenPhysicsFixture;
    }

    public Fixture getRubenSensorFixture() {
        return rubenSensorFixture;
    }

    public boolean onGround() {
        return onGround;
    }


    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        System.out.println("preSolve ");

        Body otro = (Body) ((this.getBody() == contact.getFixtureA().getBody()) ?
                contact.getFixtureB().getBody() : contact.getFixtureA().getBody());

        float heroTop = getY();
        float objectBottom = otro.getPosition().y + (Box2DUtils.height(otro) / 2);

        //if (objectBottom > heroTop) contact.setEnabled(false);

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        System.out.println("postSolve ");
    }

    @Override
    public void beginContact(Contact contact) {
        System.out.println("Begin contact");
        Fixture collider = (Fixture) ((this.getRubenSensorFixture() == contact.getFixtureA()) ?
                contact.getFixtureB() : contact.getFixtureA());

/*        if (collider instanceof EnemyObject) {
            if (body.getLinearVelocity().y < killVelocity && !hurt) {
                hurt();

                Vector2 hurtVelocity = body.getLinearVelocity();
                hurtVelocity.y = -hurtVelocityY;
                hurtVelocity.x = hurtVelocityX;
                if (collider.getX() > getX()) {
                    hurtVelocity.x = -hurtVelocityX;
                }
                body.setLinearVelocity(hurtVelocity);
            } else {
                springOffEnemy = collider.getY() - getHeight();
                //onGiveDamage.dispatch();
            }

        }*/

        //angulo de colision contacta con el sensor.
        if (contact.isTouching() ) {
            float anguloColision = new Vector2(contact.getWorldManifold().getNormal().x, contact.getWorldManifold().getNormal().y).angle();
            anguloColision = (float) ((anguloColision * 180f / Math.PI) + 360f) % 360f;

            if (anguloColision > 45f && anguloColision < 135f) {
                grounContacts.add(collider);
                onGround = true;
                updateCombinedGroundAngle();
                System.out.println("OnGroun True");
            }
        }


    }

    @Override
    public void endContact(Contact contact) {
        System.out.println("End contact");
        Fixture collider = (Fixture) ((this.getRubenSensorFixture() == contact.getFixtureA()) ?
                contact.getFixtureB() : contact.getFixtureA());

        int index= grounContacts.indexOf(collider ,false);
        if(index !=-1){
            grounContacts.removeIndex(index);
            if(grounContacts.size==0) onGround=false;
            System.out.println("OnGroun False");
            updateCombinedGroundAngle();

        }

    }

    protected void updateCombinedGroundAngle() {
        combinedGroundAngle = 0;
        if (grounContacts.size == 0) return;

        for (Fixture contact : grounContacts) {
            float angle = contact.getBody().getAngle();
            float turn = (float) (45 * Math.PI / 180);
            angle = angle % turn;
            combinedGroundAngle += angle;
        }
        combinedGroundAngle /= grounContacts.size;
    }

    public void hurt() {
        hurt = true;

    }




}


