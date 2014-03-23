package com.rubentxu.juegos.core.managers;


import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.State;

public interface StateObserver {
    public void onNotify(State state, Box2DPhysicsObject entity);
    public void onNotifyStateTimeLimit(State state, Box2DPhysicsObject entity,float time);
}
