package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DUtils;

public class Water extends Box2DPhysicsObject {

    public Array<Body> m_bodyList;

    // The outer surface normal. Change this to point out from the surface. Assume up.
    public final Vector2 mSurfaceNormal = new Vector2();
    // Fluid velocity, for drag calculations. Creates a directional 'current' for the fluid.
    public final Vector2 mFluidVelocity = new Vector2();
    // Gravity vector of the fluid. Used to provide upward force within the fluid
    public final Vector2 mGravity = new Vector2();
    // Linear drag co-efficient. Recommend that this is about 5x the angular drag.
    public float mLinearDrag;
    // Angular drag co-efficient
    public float mAngularDrag;
    // The height of the Box2D fluid surface at the normal
    public float mSurfaceHeight;
    // The fluid density
    public float mFluidDensity;

    // If false, bodies are assumed to be uniformly dense, otherwise use the
    // shapes' densities
    public boolean mUseDensity = true;

    //
    // Shared values
    //
    public static final Vector2 mTmp = new Vector2(); // scratch value for various calculations
    public static final Vector2 mSC = new Vector2(); //
    public static final Vector2 mAreac = new Vector2(); // centroid of the area
    public static final Vector2 mMassc = new Vector2(); // centroid of the mass

    //
    // Default values
    //
    public static final Vector2 DEFAULT_SURFACE_NORMAL = new Vector2(0, 0.75f); // point up
    public static final Vector2 DEFAULT_FLUID_VELOCITY = new Vector2(0,5f); // zero velocity / no current
    public static final Vector2 DEFAULT_FLUID_GRAVITY = new Vector2(0, -9.8f); // standard gravity
    public static final float DEFAULT_LINEAR_DRAG = 3f;
    public static final float DEFAULT_ANGULAR_DRAG = 1f;

    public static final boolean DEBUG_BUOYANCY = false;


    public Water(String nombre, Body body) {
        this(nombre, body, DEFAULT_SURFACE_NORMAL, DEFAULT_FLUID_VELOCITY, DEFAULT_FLUID_GRAVITY,
                Box2DUtils.height(body), body.getFixtureList().get(0).getDensity(), DEFAULT_LINEAR_DRAG, DEFAULT_ANGULAR_DRAG);

    }

    public Water(String nombre, Body body, Vector2 surfaceNormal, Vector2 fluidVelocity, Vector2 gravity,
                 float surfaceHeight, float fluidDensity, float linearDrag, float angularDrag) {
        super(nombre, GRUPOS.AGUA, body);
        mSurfaceNormal.set(surfaceNormal);
        mFluidVelocity.set(fluidVelocity);
        mGravity.set(gravity);
        mSurfaceHeight = surfaceHeight;
        mFluidDensity = fluidDensity;
        mLinearDrag = linearDrag;
        mAngularDrag = angularDrag;
        m_bodyList = new Array<Body>();

    }

    public void addBody(Body body) {


        if (m_bodyList.contains(body, false) == false) {
            m_bodyList.add(body);
        }
    }

    public void removeBody(Body body) {
        if (m_bodyList != null) {
            m_bodyList.removeValue(body, true);
        }
    }

    public void clear() {
        if (m_bodyList != null) {
            m_bodyList.clear();
        }
        m_bodyList = null;
    }

    public Array<Body> getBodyList() {
        return m_bodyList;
    }


}