package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Path;

import java.util.HashSet;
import java.util.List;


public class Enemy extends Box2DPhysicsObject {

    public final static float MAX_VELOCITY = 4f;
    public final static float JUMP_FORCE = 14.5f;
    boolean facingLeft = true;
    private boolean onGround = false;
    private State state = State.WALKING;
    private float killVelocity;
    private Boolean hurt;
    private float hurtVelocityY = 10f;
    private float hurtVelocityX = 6f;
    private float springOffEnemy = -1f;
    private HashSet<Fixture> grounContacts = new HashSet<Fixture>();
    private Fixture enemyPhysicsFixture;
    private Fixture enemySensorFixture;
    private Path path;

    public Enemy(World physics) {
        super("Enemigo", GRUPOS.ENEMIGOS, physics);
    }

    public Enemy(String name, Body body, List<Vector2> points) {
        super(name, GRUPOS.ENEMIGOS, body);
        path = new Path(MAX_VELOCITY);
        Vector2 pos = body.getPosition().cpy();
        path.addPoint(pos);
        for (Vector2 v : points) {
            path.addPoint(v);
        }
        path.reset();
    }

    public void velocityLimit() {
        Vector2 vel = this.getBody().getLinearVelocity();

        if (Math.abs(vel.x) > this.MAX_VELOCITY) {
            vel.x = Math.signum(vel.x) * this.MAX_VELOCITY;
            this.setVelocity(new Vector2(vel.x, vel.y));
        }

        if (this.path.getVelocity().x < -0) {
            this.setFacingLeft(true);

            this.getBody().applyForce(this.getPath().getForce(this.getBody().getMass()).scl( 1.4f),this.getBody().getWorldCenter(),true);
        } else if (this.path.getVelocity().x > 0) {
            this.setFacingLeft(false);
            this.getBody().applyForce(this.getPath().getForce(this.getBody().getMass()).scl(1.4f),this.getBody().getWorldCenter(),true);
        }

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

    public void setVelocity(Vector2 velocity) {
        super.getBody().setLinearVelocity(velocity);
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }

    public Fixture getEnemyPhysicsFixture() {
        return enemyPhysicsFixture;
    }

    public void setEnemyPhysicsFixture(Fixture enemyPhysicsFixture) {
        this.enemyPhysicsFixture = enemyPhysicsFixture;
    }

    public Fixture getEnemySensorFixture() {
        return enemySensorFixture;
    }

    public void setEnemySensorFixture(Fixture enemySensorFixture) {
        this.enemySensorFixture = enemySensorFixture;
    }

    public boolean isGround() {
        return onGround;
    }

    public void setGround(boolean onGround) {
        this.onGround = onGround;
    }

    public HashSet<Fixture> getGrounContacts() {
        return grounContacts;
    }

    public void setGrounContacts(HashSet<Fixture> grounContacts) {
        this.grounContacts = grounContacts;
    }

    public void hurt() {
        hurt = true;

    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return
                "onGround=" + onGround +
                        "\nstate=" + state +
                        "\nfacingLeft=" + facingLeft +
                        "\nisActive= " + getBody().isActive() +
                        "\nisSleepingAllowed= " + getBody().isSleepingAllowed() +
                        "\nisAwake=" + getBody().isAwake() +
                        "\nAngle=" + getBody().getAngle() +
                        "\nAngularDamping=" + getBody().getAngularDamping() +
                        "\nAngularVelocity=" + getBody().getAngularVelocity() +
                        "\nGravityScale=" + getBody().getGravityScale() +
                        "\nInertia=" + getBody().getInertia() +
                        "\nMass=" + getBody().getMass() +
                        "\nisBullet=" + getBody().isBullet() +
                        "\nisFixedRotation=" + getBody().isFixedRotation() +
                        "\nLinearDamping=" + getBody().getLinearDamping() +
                        "\nLinearVelocity=" + getBody().getLinearVelocity().toString() +
                        "\nPosition=" + getBody().getPosition().toString() +
                        "\nLocalCenter=" + getBody().getLocalCenter().toString() +
                        "\nWidth=" + getWidth() +
                        "\nHeight=" + getHeight() +
                        "\nWorldCenter=" + getBody().getWorldCenter().toString();
    }

    public enum State {
        IDLE, WALKING, JUMPING, DYING, FALL
    }
}


