package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.rubentxu.juegos.core.modelo.base.Box2dPhysicsCompoundObject;


public class CheckPoint extends Box2dPhysicsCompoundObject {

    public CheckPoint(String nombre, Body bodyA, Body bodyB,Joint joint) {
        super(nombre, GRUPO.CHECKPOINT, bodyA,bodyB,joint);
    }

}
