package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.World;


public class Entity extends Sprite implements Comparable<Entity> {

    protected  Body body;
    protected final int id;
    protected final String type;
    private World physics;

    public Entity(String type, int id, float x, float y, float width, float height) {
        super();
        this.type = type;
        this.id = id;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPos() {
        return body.getPosition();
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public float getRotation() {
        return body.getAngle();
    }

    public String getType() {
        return type;
    }

    public int getID() {
        return id;
    }


    @Override
    public int compareTo(Entity that) {
        // reverse sorting:
        return (int) (that.getY() * 1000f - this.getY() * 1000f);
    }


    public World getPhysics() {
        return physics;
    }

    public void setPhysics(World physics) {
        this.physics = physics;
    }

    public String toString() {
        return String.format(
                "[Entity]:\n" +
                        "\ttype: %s\n" +
                        "\tid: %d\n" +
                        "\tconnection: %d" +
                        "\tpos: %s\n" +
                        "\trot: %G\n" +
                        "\tvel: %s\n" +
                        "\trot-vel: %G",
                type, id,
                getPos().toString(),
                getRotation(),
                getBody().getLinearVelocity().toString(),
                getBody().getAngularVelocity()
        );
    }
}