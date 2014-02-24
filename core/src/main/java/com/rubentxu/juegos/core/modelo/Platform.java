package com.rubentxu.juegos.core.modelo;


import com.badlogic.gdx.physics.box2d.Body;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;

public class Platform extends Box2DPhysicsObject {


    public Platform(String nombre, GRUPO grupo, Body body) {
        super(nombre, grupo, body);
        setState(BaseState.DEFAULT);
    }


}
