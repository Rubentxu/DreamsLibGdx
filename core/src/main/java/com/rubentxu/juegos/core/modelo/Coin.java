package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.physics.box2d.Body;

public class Coin  extends Item {

    public Coin(String nombre, GRUPO grupo,TYPE tipo, Body body,int value) {
        super(nombre, grupo,tipo, body,value);
    }

}
