package com.rubentxu.juegos.core.modelo.base;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Path {
    private static final int FORWARD = 1;
    private static final int REVERSE = -1;
    private ArrayList<Vector2> points;
    public int waypoint = 0;
    private float maxDist = 0;
    private float speed;
    private float distance = 0;
    private Vector2 velocity;
    private int direction = FORWARD;
    private boolean changeDirection=false;

    public Path(float speed) {
        points = new ArrayList<Vector2>();
        velocity = new Vector2();
        this.speed = speed;
    }

    public void addPoint(Vector2 pos) {
        points.add(pos.cpy());
    }

    public boolean update(Vector2 bodyPosition, float delta) {

        boolean checkChange = false;
        if (maxDist == 0) maxDist = points.get(waypoint).dst2(bodyPosition);
        distance += getVelocity().len() * delta;
        if (isWaypointReached(bodyPosition, delta) || changeDirection) {
            changeDirection=false;
            distance = 0;
            int tempPoint = waypoint;
            waypoint = getNextPoint();
            maxDist = points.get(tempPoint).dst2(points.get(waypoint));
            checkChange = true;
        }

        float angle = (float) Math.atan2(points.get(waypoint).y - bodyPosition.y, points.get(waypoint).x - bodyPosition.x);
        velocity.set((float) Math.cos(angle) * speed, (float) Math.sin(angle) * speed);
        return checkChange;
    }

    public boolean isWaypointReached(Vector2 bodyPosition, float delta) {
        return Math.abs(points.get(waypoint).x - bodyPosition.x) <= speed * delta;
    }

    public int getNextPoint() {
        int nextPoint = waypoint + direction;
        if (nextPoint >= points.size()) {
            direction = REVERSE;
        } else if (nextPoint < 0) {
            direction = FORWARD;
        }
        return waypoint + direction;
    }

    public Vector2 getVelocity() {
        return velocity.cpy();
    }

    public void setPoints(ArrayList<Vector2> points) {
        this.points = points;
    }

    public ArrayList<Vector2> getPoints() {
        return points;
    }

    public Vector2 getForce(float mass) {
        Vector2 v = velocity.cpy().nor().scl(mass);
        v.y = 0;
        return v;
    }


    public void setChangeDirection(boolean changeDirection) {
        this.changeDirection = changeDirection;
    }
}