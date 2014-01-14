package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.physics.box2d.Body;

public class Coin  extends Item {



    public Coin(String nombre, GRUPOS grupo, Body body) {
        super(nombre, grupo, body);
        TYPE_ITEM=getType();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getValue() {
        return 1;
    }

    @Override
    public TYPE getType() {
        return TYPE.COIN;
    }

}
