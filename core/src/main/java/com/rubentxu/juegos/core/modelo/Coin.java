package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.physics.box2d.Body;
import com.rubentxu.juegos.core.modelo.base.State;

public class Coin  extends Item {

    public enum StateCoin implements State {
        COLLECTED
    }

    public Coin(String nombre, GRUPO grupo,TYPE tipo, Body body,int value) {
        super(nombre, grupo,tipo, body,value);
        setState(BaseState.DEFAULT);
    }


}
