package com.rubentxu.juegos.core.managers;


import com.rubentxu.juegos.core.modelo.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.MovingPlatform;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;

import static org.easymock.EasyMock.createNiceMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlatformManagerTest {

    static PlatformManager platformManager;


    @BeforeClass
    public static void testSetup() {
        MovingPlatform m1= new MovingPlatform("M2", Box2DPhysicsObject.GRUPOS.PLATAFORMAS_MOVILES,body2,
                78,4,80,9 );
        HashSet<MovingPlatform> MovingPlatformplatforms = new HashSet<MovingPlatform>();
        MovingPlatformplatforms.add(m1);
        platformManager = new PlatformManager();
    }

    /* Test sobre el metodo applyImpulses */
    @Test
    public void testApplyImpulses() {
        platformManager.updateMovingPlatform();

    }




}
