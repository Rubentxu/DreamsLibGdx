package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.rubentxu.juegos.core.modelo.base.Box2dPhysicsCompoundObject;


public class Mill extends Box2dPhysicsCompoundObject {

    public Mill(String nombre, Body bodyA, Body bodyB,Joint joint) {
        super(nombre, GRUPO.MILL, bodyA,bodyB,joint);
    }

}
