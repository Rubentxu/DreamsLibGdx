package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.physics.box2d.Body;
import com.rubentxu.juegos.core.modelo.base.Sensor;

public class Coin extends Sensor {

    private int value;
    private int type;

    public Coin(String nombre, GRUPOS grupo, Body body) {
        super(nombre, grupo, body);
    }
}
