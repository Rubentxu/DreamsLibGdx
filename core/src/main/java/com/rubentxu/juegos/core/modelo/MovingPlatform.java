package com.rubentxu.juegos.core.modelo;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Path;
import com.rubentxu.juegos.core.modelo.base.State;

import java.util.HashSet;

public class MovingPlatform extends Platform implements Disposable{


    public enum StateMovingPlatform implements State {
        ENABLED, DISABLED
    }

    private  Vector2 pVelocity,start;

    private float distance=0;
    public Boolean enabled=true;
    public Boolean waitForPassenger=false;
    private Boolean forward= false;
    private HashSet<Box2DPhysicsObject> passengers= new HashSet<Box2DPhysicsObject>();
    private Path path;

    public MovingPlatform(Body body, Vector2 pVelocity){
        super("Platform", GRUPO.MOVING_PLATFORM,body);
        this.pVelocity = pVelocity;
        //this.maxDist=0;

    }

    public MovingPlatform(String nombre, GRUPO grupo, Body body, float dstX, float dstY,float speed) {
        super(nombre, grupo, body);
        path=new Path(speed);
        Vector2 pos= body.getPosition().cpy();
        path.addPoint(pos);
        path.addPoint(new Vector2(pos.x + dstX, pos.y + dstY));
        this.start=body.getPosition().cpy();


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


    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }


    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return
                "\nSin Pasajeros?= " + passengers.isEmpty()+
                "\nPosicion Comienzo= " + start+
                "\nPosicion Actual= " + this.getBody().getPosition()+
                "\nVelocidad= " + path.getVelocity()+
                "\nPasajeros= " + passengers.size() ;

    }

    @Override
    public void dispose() {
        super.dispose();
        pVelocity=null;
        start=null;
        passengers=null;
        path=null;
    }
}
