package com.rubentxu.juegos.core.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.rubentxu.juegos.core.managers.interfaces.IManager;

import java.util.*;


public class WaterManager implements IManager {

    private Map<Fixture, Fixture> fixturesPair;
    private final static float EPSILON = 0.001f;

    public WaterManager() {
        this.fixturesPair = new HashMap<Fixture, Fixture>();

    }

    @Override
    public void handleBeginContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();


        if (fixtureA.isSensor() && fixtureB.getBody().getType() == BodyDef.BodyType.DynamicBody)
            fixturesPair.put(make_pair(fixtureA, fixtureB));
        else if (fixtureB.isSensor() && fixtureA.getBody().getType() == BodyDef.BodyType.DynamicBody)
            fixturesPair.put(make_pair(fixtureB, fixtureA));
    }


    @Override
    public void handleEndContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.isSensor() && fixtureB.getBody().getType() == BodyDef.BodyType.DynamicBody)
            fixturesPair.remove(make_pair(fixtureA, fixtureB));
        else if (fixtureB.isSensor() && fixtureA.getBody().getType() == BodyDef.BodyType.DynamicBody)
            fixturesPair.remove(make_pair(fixtureB, fixtureA));
    }

    @Override
    public void handlePostSolve(Contact contact, ContactImpulse impulse) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void handlePreSolve(Contact contact, Manifold oldManifold) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean handleShouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void update(float delta) {

        Iterator<Map.Entry<Fixture, Fixture>> itFixt = fixturesPair.entrySet().iterator();
        while (itFixt.hasNext()) {
            Map.Entry<Fixture, Fixture> it = itFixt.next();
            //fixtureA is the fluid
            Fixture fixtureA = it.getKey();
            Fixture fixtureB = it.getValue();

            float density = fixtureA.getDensity();

            Vector2 intersectionPoints= findIntersectionOfFixtures(fixtureA, fixtureB);
            if (intersectionPoints!=null) {

                //find centroid
                float area = 0;
                Vector2 centroid = ComputeCentroid(intersectionPoints, area);

                Vector2 gravity=new Vector2( 0, -10 );

                //apply buoyancy force (fixtureA is the fluid)
                float displacedMass = fixtureA.getDensity() * area;
                Vector2 buoyancyForce = gravity.cpy().scl(-1).scl(displacedMass);
                fixtureB.getBody().applyForce(buoyancyForce, centroid, true);
            }

        }
    }

    private Vector2 ComputeCentroid(Vector2[] intersectionPoints, float area) {
        Vector2 c= new Vector2(0.0f,0.0f);
        area = 0.0f;

        // pRef is the reference point for forming triangles.
        // It's location doesn't change the result (except for rounding error).

        Vector2 pRef= new Vector2(0.0f,0.0f);

        final float inv3 = 1.0f / 3.0f;

        for (int i = 0; i < intersectionPoints.length; ++i)
        {
            // Triangle vertices.
            Vector2 p1 = pRef;
            Vector2 p2 = intersectionPoints[i];
            Vector2 p3 = (i + 1 < intersectionPoints.length )? intersectionPoints[i+1] : intersectionPoints[0];

            Vector2 e1 = p2.cpy().sub( p1);
            Vector2 e2 = p3.cpy().sub( p1);

            float D = e1.crs( e2);

            float triangleArea = 0.5f * D;
            area += triangleArea;

            // Area weighted centroid
            c.add(p1.cpy().add(p2).add(p3)).scl(inv3).scl(triangleArea);
        }

        // Centroid
        if (area > EPSILON)
            c.scl( 1.0f / area);
        else
            area = 0;
        return c;
    }

    private List<Vector2> findIntersectionOfFixtures(Fixture fA, Fixture fB, List<Vector2> outputVertices) {
        //currently this only handles polygon vs polygon
        if ( fA.getShape().getType() != Shape.Type.Polygon ||
                fB.getShape().getType() != Shape.Type.Polygon )
        return null;

        PolygonShape polyA = (PolygonShape)fA.getShape();
        PolygonShape polyB = (PolygonShape)fB.getShape();

        //fill 'subject polygon' from fixtureA polygon
        for (int i = 0; i < polyA.getVertexCount(); i++)
            outputVertices.add( fA.getBody().getWorldPoint(polyA.getVertex(i)) );

        //fill 'clip polygon' from fixtureB polygon
        List<Vector2> clipPolygon=new ArrayList<Vector2>();
        for (int i = 0; i < polyB.getVertexCount(); i++)
            clipPolygon.add( fB.getBody().getWorldPoint(polyB.getVertex(i)) );

        ArrayList<Vector2> cp1 = new ArrayList<Vector2>(clipPolygon);
        for (int j = 0; j < clipPolygon.size(); j++) {
            Vector2 cp2 = clipPolygon.get(j);
            if ( outputVertices.isEmpty() )
                return null;

            List<Vector2> inputList = outputVertices;
            outputVertices.clear();
            Vector2 s = inputList.get(inputList.size() - 1); //last on the input list
            for (int i = 0; i < inputList.size(); i++) {
                Vector2 e = inputList.get(i);
                if (inside(cp1, cp2, e)) {
                    if (!inside(cp1, cp2, s)) {
                        outputVertices.add( intersection(cp1, cp2, s, e) );
                    }
                    outputVertices.add(e);
                }
                else if (inside(cp1, cp2, s)) {
                    outputVertices.add(intersection(cp1, cp2, s, e));
                }
                s = e;
            }
            cp1 = cp2;
        }

        return (outputVertices.isEmpty())?null:outputVertices;
    }

    private Vector2 intersection(ArrayList<Vector2> cp1, Vector2 cp2, Vector2 s, Vector2 e) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    private boolean inside(ArrayList<Vector2> cp1, Vector2 cp2, Vector2 e) {

        return false;
    }


}
