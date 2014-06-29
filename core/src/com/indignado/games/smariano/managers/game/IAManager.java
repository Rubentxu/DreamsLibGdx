package com.indignado.games.smariano.managers.game;

import com.indignado.games.smariano.managers.StateObserver;
import com.indignado.games.smariano.modelo.base.Box2DPhysicsObject;
import com.indignado.games.smariano.modelo.base.State;


public class IAManager implements StateObserver {
    @Override
    public void onNotify(State state, Box2DPhysicsObject entity) {

    }

    @Override
    public void onNotifyStateTimeLimit(State state, Box2DPhysicsObject entity, float time) {

    }
}
