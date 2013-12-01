package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.physics.box2d.Body;

public class Water extends Box2DPhysicsObject {



    public Water(String nombre, Body body) {
        super(nombre, GRUPOS.AGUA, body);

    }



}
