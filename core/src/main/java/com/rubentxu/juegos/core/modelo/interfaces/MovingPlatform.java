package com.rubentxu.juegos.core.modelo.interfaces;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.rubentxu.juegos.core.modelo.Platform;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DUtils;

import java.util.HashSet;

public class MovingPlatform extends Platform{

    public Vector2 reverseDesplazamiento;
    public Vector2 desplazamiento;
    public float speed=2;
    public Boolean enabled=true;
    public Boolean waitForPassenger=false;
    private Vector2 start= new Vector2();
    private Vector2 end= new Vector2();
    private Boolean forward= true;
    private HashSet<Body> passengers= new HashSet<Body>();
    public float dist=0;
    public float maxDist;
    private Vector2 direction;


    public MovingPlatform(Body body){
        super("Platform",GRUPOS.PLATAFORMAS_MOVILES,body);
    }

    public MovingPlatform(String nombre, GRUPOS grupo, Body body,float x, float y, float ex, float ey,float maxDist) {
        super(nombre, grupo, body);
        start.x= x;
        start.y= y;
        end.x= ex;
        end.y= ey;
        this.maxDist=end.dst(start);
        this.desplazamiento=end.cpy().sub(start);
        this.reverseDesplazamiento=start.cpy().sub(end);
        body.setTransform(start,0);
        body.getFixtureList().get(0).setUserData(this);
        body.setUserData(this);
    }

    public Vector2 getStart() {
        return start;
    }

    public void setStart(Vector2 start) {
        this.start = start;
    }

    public Vector2 getEnd() {
        return end;
    }

    public void setEnd(Vector2 end) {
        this.end = end;
    }

    public Boolean getForward() {
        return forward;
    }

    public void setForward(Boolean forward) {
        this.forward = forward;
    }

    public HashSet<Body> getPassengers() {
        return passengers;
    }

    public void setPassengers(HashSet<Body> passengers) {
        this.passengers = passengers;
    }

    @Override
    public String toString() {
        return
                "\nSin Pasajeros?= " + passengers.isEmpty()+
                "\nPosicion Comienzo= " + start+
                "\nPosicion Final= " + end+
                "\nPosicion Actual= " + this.getBody().getPosition()+
                "\nDistancia = " + this.maxDist+
                "\nDistacia hasta Comienzo= " +  start.dst(this.getBody().getPosition())+
                "\nDistacia hasta Final= " +  end.dst(this.getBody().getPosition()) +
                "\nDistacia Actual= " +  this.direction.len() +
                "\nVector Desplazamiento= " +  this.desplazamiento +
                "\nVector ReverseDesplazamiento= " +  this.reverseDesplazamiento +
                "\nForward= " +  forward +
                "\nPasajeros= " + passengers.size() ;

    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public Vector2 getDirection() {
        return direction;
    }
}
