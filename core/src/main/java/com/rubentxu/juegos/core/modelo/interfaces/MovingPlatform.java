package com.rubentxu.juegos.core.modelo.interfaces;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.rubentxu.juegos.core.modelo.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.Platform;

import java.util.HashSet;

public class MovingPlatform extends Platform{

    private final Vector2 pVelocity;
    private final float maxDist;
    public  float speed;
    private float distance=0;
    public Boolean enabled=true;
    public Boolean waitForPassenger=false;
    private Boolean forward= false;
    private HashSet<Box2DPhysicsObject> passengers= new HashSet<Box2DPhysicsObject>();
    private float time=0;
    private Vector2 start;
    private Vector2 end;

    public MovingPlatform(Body body, Vector2 pVelocity){
        super("Platform",GRUPOS.PLATAFORMAS_MOVILES,body);
        this.pVelocity = pVelocity;
        this.maxDist=0;
    }

    public MovingPlatform(String nombre, GRUPOS grupo, Body body,float x, float y, float ex, float ey,float speed) {
        super(nombre, grupo, body);
        start= new Vector2(x,y);
        end= new Vector2(ex,ey);
        this.speed=speed;
        this.pVelocity=end.cpy().sub(start).nor().scl(this.speed);
        this.maxDist=start.dst(end);
        this.setDistance(0f);
        body.setTransform(start,0);
        body.getFixtureList().get(0).setUserData(this);
        body.setUserData(this);
    }

    public Boolean getForward() {
        return forward;
    }

    public void setForward(Boolean forward) {
        this.forward = forward;
    }

    public HashSet<Box2DPhysicsObject> getPassengers() {
        return passengers;
    }

    public void setPassengers(HashSet<Box2DPhysicsObject> passengers) {
        this.passengers = passengers;
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

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return
                "\nSin Pasajeros?= " + passengers.isEmpty()+
                "\nPosicion Comienzo= " + start+
                "\nPosicion Final= " + end+
                "\nPosicion Actual= " + this.getBody().getPosition()+
                "\nVelocidad= " + this.pVelocity+
                "\nDiferencia Vector= " +  this.end.cpy().sub(start)+
                "\nMaxDistancia = " + this.getMaxDist() +
                "\nDistacia = " + this.getDistance() +
                "\nTiempo = " + this.time +
                "\nForward= " +  forward +
                "\nPasajeros= " + passengers.size() ;

    }
}
