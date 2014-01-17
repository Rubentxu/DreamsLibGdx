package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.physics.box2d.Body;

public class Coin  extends Item {

    public Coin(String nombre, GRUPO grupo, Body body,int value) {
        super(nombre, grupo, body,value);

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

    @Override
    public void dispose(){
        super.dispose();
    }

}
