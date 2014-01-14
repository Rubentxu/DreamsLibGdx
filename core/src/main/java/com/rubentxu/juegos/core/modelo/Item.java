package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.physics.box2d.Body;
import com.rubentxu.juegos.core.modelo.base.Sensor;

public abstract class Item extends Sensor {

    final int value = 0;
    TYPE TYPE_ITEM=null ;


    public Item(String nombre, GRUPOS grupo, Body body) {
        super(nombre, grupo, body);
    }

    public static enum TYPE {
        COIN, KEY, POWERUP
    }

    public abstract String getName();

    public abstract int getValue();

    public abstract TYPE getType();
}