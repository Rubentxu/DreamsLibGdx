package com.rubentxu.juegos.core.modelo;


public class Sensor extends Box2DPhysicsObject {
    public Sensor(String nombre, GRUPOS tipo, com.badlogic.gdx.physics.box2d.World box2D, float x, float y, float width, float height, float radius) {
        super(nombre, tipo, box2D, x, y, width, height, radius);
    }
}
