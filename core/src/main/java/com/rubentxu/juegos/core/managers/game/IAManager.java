package com.rubentxu.juegos.core.managers.game;

import com.rubentxu.juegos.core.managers.StateObserver;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.State;


public class IAManager implements StateObserver {
    @Override
    public void onNotify(State state, Box2DPhysicsObject entity) {

    }

    @Override
    public void onNotifyStateTimeLimit(State state, Box2DPhysicsObject entity, float time) {

    }
}
