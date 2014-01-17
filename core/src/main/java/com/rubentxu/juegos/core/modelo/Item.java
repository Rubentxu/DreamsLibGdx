package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.physics.box2d.Body;
import com.rubentxu.juegos.core.modelo.base.Sensor;

public class Item extends Sensor {


    public static enum TYPE {
        COIN, KEY, POWERUP
    }

    private int value;
    private String name;
    private TYPE TYPE_ITEM=null ;

    public Item(String nombre, GRUPO grupo,TYPE tipo, Body body,int value) {
        super(nombre, grupo, body);
        this.TYPE_ITEM=tipo;
        this.name=nombre;
        this.value=value;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public int getValue(){
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public TYPE getType() {
        return TYPE_ITEM ;
    }

    public void setType(TYPE type) {
        this.TYPE_ITEM = type;
    }


}