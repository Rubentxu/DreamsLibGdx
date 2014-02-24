package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Path;
import com.rubentxu.juegos.core.modelo.base.State;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Enemy extends Box2DPhysicsObject implements Disposable {

    public enum StateEnemy implements State {
        IDLE, WALKING, JUMPING, FALL, HURT, HIT, DEAD
    }

    public enum StatePos {ONGROUND, INWATER, ONAIR}

    // States
    private StatePos statePos = StatePos.ONGROUND;
    boolean facingLeft = true;

    public final static float MAX_VELOCITY = 3f;
    public final static float JUMP_FORCE = 14.5f;

    private HashSet<Fixture> grounContacts = new HashSet<Fixture>();
    private Fixture enemyPhysicsFixture;
    private Fixture enemySensorFixture;
    private Path path;


    public Enemy(World physics) {
        super("Enemigo", GRUPO.ENEMY, physics);
    }

    public Enemy(String name, Body body, List<Vector2> points) {
        super(name, GRUPO.ENEMY, body);
        path = new Path(MAX_VELOCITY);
        path.setPoints((ArrayList<Vector2>) points);
        setState(StateEnemy.WALKING);
    }

    public void velocityLimit() {
        Vector2 vel = this.getBodyA().getLinearVelocity();

        if (Math.abs(vel.x) > this.MAX_VELOCITY) {
            vel.x = Math.signum(vel.x) * this.MAX_VELOCITY;
            this.setVelocity(new Vector2(vel.x, vel.y));
        }

        if (vel.x < -0.5f) {
            facingLeft = true;
        } else if (vel.x > 0.5f) {
            facingLeft = false;
        }
    }


    public Vector2 getVelocity() {
        return super.getBodyA().getLinearVelocity();
    }

    public void setVelocity(Vector2 velocity) {
        super.getBodyA().setLinearVelocity(velocity);
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


    public HashSet<Fixture> getGrounContacts() {
        return grounContacts;
    }

    public void setGrounContacts(HashSet<Fixture> grounContacts) {
        this.grounContacts = grounContacts;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public StatePos getStatePos() {
        return statePos;
    }

    public void setStatePos(StatePos statePos) {
        this.statePos = statePos;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }


    @Override
    public String toString() {
        return
                "statePos=" + statePos +
                        "\nstate=" + getState() +
                        "\nfacingLeft=" + isFacingLeft() +
                        "\nisActive= " + getBodyA().isActive() +
                        "\nisSleepingAllowed= " + getBodyA().isSleepingAllowed() +
                        "\nisAwake=" + getBodyA().isAwake() +
                        "\nWayPoint0=" + this.getPath().getPoints().toArray()[0] +
                        "\nWayPoint1=" + this.getPath().getPoints().toArray()[1] +
                        "\nWayPointSize=" + this.getPath().getPoints().size() +
                        "\nWayPointIndex=" + this.getPath().waypoint +
                        "\nMass=" + getBodyA().getMass() +
                        "\nisBullet=" + getBodyA().isBullet() +
                        "\nisFixedRotation=" + getBodyA().isFixedRotation() +
                        "\nLinearDamping=" + getBodyA().getLinearDamping() +
                        "\nLinearVelocity=" + getBodyA().getLinearVelocity().toString() +
                        "\nPosition=" + getBodyA().getPosition().toString() +
                        "\nLocalCenter=" + getBodyA().getLocalCenter().toString() +
                        "\nWidth=" + getWidthBodyA() +
                        "\nHeight=" + getHeightBodyA() +
                        "\nWorldCenter=" + getBodyA().getWorldCenter().toString();
    }

    @Override
    public void dispose() {
        super.dispose();
        grounContacts = null;
        enemyPhysicsFixture = null;
        enemySensorFixture = null;
        path = null;
    }


}


