package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Disposable;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.State;

import java.util.HashSet;


public class Hero extends Box2DPhysicsObject implements Disposable {


    public enum StateHero implements State {
        IDLE, WALKING, JUMPING, DYING, FALL, SWIMMING, PROPULSION,WIN
    }

    public enum StatePos {ONGROUND, INWATER, ONAIR}


    // States
    private StatePos statePos = StatePos.ONGROUND;
    boolean facingLeft = true;

    public final static float MAX_VELOCITY = 4f;
    public final static float JUMP_FORCE = 14.5f;
    private HashSet<Fixture> grounContacts;
    private Fixture heroPhysicsFixture;
    private Fixture heroSensorFixture;
    private ParticleEffect particleEffect;
    private Profile profile;

    public Hero(String name, Body body) {
        super(name, GRUPO.HERO,body);
        setGrounContacts(new HashSet<Fixture>());
        setState(StateHero.IDLE);
    }

    public void velocityLimit() {
        Vector2 vel = this.getBodyA().getLinearVelocity();

        if (Math.abs(vel.x) > this.MAX_VELOCITY) {
            vel.x = Math.signum(vel.x) * this.MAX_VELOCITY;
            this.setVelocity(new Vector2(vel.x, vel.y));
        } else if (Math.abs(vel.y) > this.MAX_VELOCITY * 2) {
            vel.y = Math.signum(vel.y) * this.MAX_VELOCITY * 2;
            this.setVelocity(new Vector2(vel.x, vel.y));
        }
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public Vector2 getVelocity() {
        return super.getBodyA().getLinearVelocity();
    }

    public void setVelocity(Vector2 velocity) {
        super.getBodyA().setLinearVelocity(velocity);
    }

    public Fixture getHeroPhysicsFixture() {
        return heroPhysicsFixture;
    }

    public Fixture getHeroSensorFixture() {
        return heroSensorFixture;
    }

    public void setHeroPhysicsFixture(Fixture heroPhysicsFixture) {
        this.heroPhysicsFixture = heroPhysicsFixture;
    }

    public void setHeroSensorFixture(Fixture heroSensorFixture) {
        this.heroSensorFixture = heroSensorFixture;
    }


    public HashSet<Fixture> getGrounContacts() {
        return grounContacts;
    }

    public void setGrounContacts(HashSet<Fixture> grounContacts) {
        this.grounContacts = grounContacts;
    }


    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public StatePos getStatePos() {
        return statePos;
    }

    public void setStatePos(StatePos statePos) {
        this.statePos = statePos;
    }

    @Override
    public ParticleEffect getEffect() {
        return particleEffect;
    }

    @Override
    public void setEffect(ParticleEffect effect) {
        this.particleEffect = effect;
    }


    @Override
    public String toString() {
        return
                "StatePos=" + statePos +
                        "\nstate=" + getState() +
                        "\nfacingLeft=" + facingLeft +
                        "\nGravityScale=" + getBodyA().getGravityScale() +
                        "\nInertia=" + getBodyA().getInertia() +
                        "\nMasa=" + getBodyA().getMass() +
                        "\nPeso=" + getBodyA().getMass() * 9.8f +
                        "\nisFixedRotation=" + getBodyA().isFixedRotation() +
                        "\nLinearDamping=" + getBodyA().getLinearDamping() +
                        "\nLinearVelocity=" + getBodyA().getLinearVelocity().toString() +
                        "\nPosition=" + getBodyA().getPosition().toString() +
                        "\nSizeGroundContact=" + grounContacts.size() +
                        "\nWidth=" + getWidthBodyA() +
                        "\nHeight=" + getHeightBodyA() +
                        "\nTimeState=" + getStateTime();
    }

    @Override
    public void dispose() {
        super.dispose();
        grounContacts = null;
        heroPhysicsFixture = null;
        heroSensorFixture = null;
        profile = null;
    }

}


