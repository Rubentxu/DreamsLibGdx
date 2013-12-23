package com.rubentxu.juegos.core.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.managers.interfaces.IManager;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPOS;
import com.rubentxu.juegos.core.modelo.Rubentxu;
import com.rubentxu.juegos.core.modelo.Water;
import com.rubentxu.juegos.core.utils.physics.BuoyancyUtils;

import java.util.HashSet;


public class WaterManager implements IManager {

    private HashSet<Water> waterSensors;

    @Override
    public void handleBeginContact(Contact contact) {
        Water w = getWater(contact);
        Body b = getSubmergedBody(contact).getBody();
        w.addBody(b);
        if(((Box2DPhysicsObject)b.getUserData()).getGrupo().equals(GRUPOS.HEROES))
            ((Rubentxu)b.getUserData()).setState(Rubentxu.State.SWIMMING);

    }


    @Override
    public void handleEndContact(Contact contact) {
        Water w = getWater(contact);
        w.removeBody(getSubmergedBody(contact).getBody());
    }

    @Override
    public void handlePostSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public void handlePreSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public boolean handleShouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return false;
    }

    public Box2DPhysicsObject getSubmergedBody(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (!box2dPhysicsA.getGrupo().equals(GRUPOS.AGUA)) {
            return box2dPhysicsA;
        } else {
            return box2dPhysicsB;
        }
    }

    public Water getWater(Contact contact) {
        Box2DPhysicsObject box2dPhysicsA = (Box2DPhysicsObject) contact.getFixtureA().getUserData();
        Box2DPhysicsObject box2dPhysicsB = (Box2DPhysicsObject) contact.getFixtureB().getUserData();

        if (box2dPhysicsA.getGrupo().equals(GRUPOS.AGUA)) {
            return (Water) box2dPhysicsA;
        } else {
            return (Water) box2dPhysicsB;
        }
    }

    @Override
    public void update(float delta) {
        for (Water w : waterSensors) {
            for (int i = 0; i < w.m_bodyList.size; i++) {
                Array<Fixture> fixtureList = w.m_bodyList.get(i).getFixtureList();
                for (int j = 0; j < fixtureList.size; j++) {
                    Fixture fixture = fixtureList.get(j);
                    if(!fixture.isSensor()){
                        ApplyToFixture(fixture, w);
                    }
                }
            }
        }
    }

    private boolean ApplyToFixture(Fixture f, Water w) {
        float shapeDensity = w.mUseDensity ? f.getDensity() : w.mFluidDensity;

        // don't bother with buoyancy on sensors or fixtures with no density
        if (f.isSensor() || (shapeDensity == 0)) {
            return false;
        }
        Body body = f.getBody();
        w.mAreac.set(Vector2.Zero);
        w.mMassc.set(Vector2.Zero);
        float area = 0;

        // Get shape for displacement area calculations
        Shape shape = f.getShape();

        w.mSC.set(Vector2.Zero);
        float sarea;
        switch (shape.getType()) {
            case Circle:
                sarea = BuoyancyUtils.ComputeSubmergedArea((CircleShape) shape, w.mSurfaceNormal, w.mSurfaceHeight, body.getTransform(), w.mSC);
                break;

            case Chain:
                sarea = BuoyancyUtils.ComputeSubmergedArea((ChainShape) shape, w.mSurfaceNormal, w.mSurfaceHeight, body.getTransform(), w.mSC);
                break;

            case Edge:
                sarea = BuoyancyUtils.ComputeSubmergedArea((EdgeShape) shape, w.mSurfaceNormal, w.mSurfaceHeight, body.getTransform(), w.mSC);
                break;

            case Polygon:
                sarea = BuoyancyUtils.ComputeSubmergedArea((PolygonShape) shape, w.mSurfaceNormal, w.mSurfaceHeight, body.getTransform(), w.mSC);
                break;

            default:
                sarea = 0;
                break;
        }

        area += sarea;
        w.mAreac.x += sarea * w.mSC.x;
        w.mAreac.y += sarea * w.mSC.y;
        float mass = sarea * shapeDensity;
        w.mMassc.x += sarea * w.mSC.x * shapeDensity;
        w.mMassc.y += sarea * w.mSC.y * shapeDensity;

        w.mAreac.x /= area;
        w.mAreac.y /= area;
        w.mMassc.x /= mass;
        w.mMassc.y /= mass;
        if (area < Float.MIN_VALUE) {
            return false;
        }

        if (w.DEBUG_BUOYANCY) {
            // Run debug w/HCR to see the effects of different fluid densities / linear drag
            w.mFluidDensity = 2f;
            w.mLinearDrag = 5;
            w.mAngularDrag = 2;
        }

        // buoyancy force.
        w.mTmp.set(w.mGravity).scl(-w.mFluidDensity * area);
        body.applyForce(w.mTmp, w.mMassc, true); // multiply by -density to invert gravity

        // linear drag.
        w.mTmp.set(body.getLinearVelocityFromWorldPoint(w.mAreac).sub(w.mFluidVelocity).scl(-w.mLinearDrag * area));
        body.applyForce(w.mTmp, w.mAreac, true);

        // angular drag.
        float bodyMass = body.getMass();
        if (bodyMass < 1) // prevent a huge torque from being generated...
        {
            bodyMass = 1;
        }
        float torque = -body.getInertia() / bodyMass * area * body.getAngularVelocity() * w.mAngularDrag;
        body.applyTorque(torque, true);
        return true;
    }


    public HashSet<Water> getWaterSensors() {
        return waterSensors;
    }

    public void setWaterSensors(HashSet<Water> waterSensors) {
        this.waterSensors = waterSensors;
    }
}
