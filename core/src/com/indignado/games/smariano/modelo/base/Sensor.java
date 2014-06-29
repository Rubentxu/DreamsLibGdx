package com.indignado.games.smariano.modelo.base;


import com.badlogic.gdx.physics.box2d.Body;

public class Sensor extends Box2DPhysicsObject {

    public Sensor(String nombre, GRUPO grupo, Body body) {
        super(nombre, grupo, body);
    }

}
