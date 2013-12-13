package com.rubentxu.juegos.core.modelo.base;


import com.badlogic.gdx.physics.box2d.Body;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;

public class Sensor extends Box2DPhysicsObject {

    public Sensor(String nombre, GRUPOS grupo, Body body) {
        super(nombre, grupo, body);
    }

}
