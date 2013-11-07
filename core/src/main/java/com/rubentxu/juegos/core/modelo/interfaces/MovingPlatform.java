package com.rubentxu.juegos.core.modelo.interfaces;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.rubentxu.juegos.core.modelo.Platform;

import java.util.HashSet;

public class MovingPlatform extends Platform{


    private final Vector2 pVelocity;
    private final float maxDist;
    private float distance=0;
    public float speed=2;
    public Boolean enabled=true;
    public Boolean waitForPassenger=false;
    private Vector2 start= new Vector2();
    private Vector2 end= new Vector2();
    private Boolean forward= false;
    private HashSet<Body> passengers= new HashSet<Body>();



    public MovingPlatform(Body body, Vector2 pVelocity){
        super("Platform",GRUPOS.PLATAFORMAS_MOVILES,body);
        this.pVelocity = pVelocity;
        this.maxDist=0;
    }

    public MovingPlatform(String nombre, GRUPOS grupo, Body body,float x, float y, float ex, float ey,float speed) {
        super(nombre, grupo, body);
        start.x= x;
        start.y= y;
        end.x= ex;
        end.y= ey;
        this.pVelocity=end.cpy().sub(start).scl(speed);
        this.maxDist=start.dst(end);
        this.setDistance(0f);
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
                "\nVelocidad= " + this.pVelocity+
                "\nMaxDistancia = " + this.getMaxDist() +
                "\nDistacia = " + this.getDistance() +
                "\nForward= " +  forward +
                "\nPasajeros= " + passengers.size() ;

    }

    public Vector2 getpVelocity() {
        return pVelocity;
    }

    public float getMaxDist() {
        return maxDist;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
