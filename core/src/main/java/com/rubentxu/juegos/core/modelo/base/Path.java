package com.rubentxu.juegos.core.modelo.base;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

public class Path{
    private static final int FORWARD=1;
    private static final int REVERSE=-1;

    private ArrayList<Vector2>  positions;
    private  float maxDist;
    private float speed;
    private float distance=0;
    private Vector2 velocity;
    int currentPointIndex;
    int nextPointIndex;
    private int direction=FORWARD;

    public Path(float speed){
        setPositions(new ArrayList<Vector2>());
        this.setSpeed(speed);

    }

    public void addPoint(Vector2 pos){
        getPositions().add(pos.cpy());

    }

    public void reset(){
        currentPointIndex=0;
        nextPointIndex=getNextPoint();
        setNextPointVelocity();
        setMaxDist(getCurrentPoint().cpy().dst(getNext2Point()));
        System.out.println(getCurrentPoint()+"--"+getNext2Point()+"-dist"+getNext2Point().dst(getCurrentPoint()));
    }

    public Vector2 getCurrentPoint(){
        return getPositions().get(currentPointIndex).cpy();
    }

    public Vector2 getNext2Point(){
        return getPositions().get(nextPointIndex).cpy();
    }


    public boolean updatePath(Vector2 bodyPosition,float delta){
        Vector2 nextPointPosition= getPositions().get(nextPointIndex);
        distance+=  getVelocity().len() * delta;
        if(getDistance() > getMaxDist() ){
            distance=0;
            currentPointIndex=nextPointIndex;
            nextPointIndex=getNextPoint();
            setNextPointVelocity();
            setMaxDist(getCurrentPoint().cpy().dst(getNext2Point()));
            return true;
        } else if(distance>maxDist*2) {
            direction= (direction==FORWARD)?REVERSE:FORWARD;
            nextPointIndex=getNextPoint();
            setNextPointVelocity();
            maxDist=positions.get(nextPointIndex).dst2(bodyPosition);
            return true;
        }

        return false;
    }


    int getNextPoint(){
        int nextPoint=currentPointIndex+direction;
        if(nextPoint >= getPositions().size()){
            setDirection(REVERSE);
        }else if(nextPoint<=-1){
            setDirection(FORWARD);
        }
        return currentPointIndex+ getDirection();
    }

    void setNextPointVelocity(){
        Vector2 nextPosition= getNext2Point();
        Vector2 currentPosition= getCurrentPoint();
        velocity=nextPosition.sub(currentPosition).nor().scl(this.getSpeed());
    }
    public Vector2 getVelocity(){
        return velocity.cpy();
    }

    public ArrayList<Vector2> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<Vector2> positions) {
        this.positions = positions;
    }

    public float getMaxDist() {
        return maxDist;
    }

    public void setMaxDist(float maxDist) {
        this.maxDist = maxDist;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getForce(float mass){
        return velocity.cpy().scl(mass);
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }


}