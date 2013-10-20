package com.rubentxu.juegos.core.modelo;


import com.badlogic.gdx.physics.box2d.Body;

public class Platform extends Box2DPhysicsObject {

    private  Boolean oneWay=false;

    public Platform(String nombre, GRUPOS grupo, Body body) {
        super(nombre, grupo, body);
    }


    public Boolean isOneWay() {
        return oneWay;
    }

    public void setOneWay(Boolean oneWay) {
        this.oneWay = oneWay;
    }


}
