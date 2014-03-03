package com.rubentxu.juegos.core.vista;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.managers.game.ResourcesManager;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Hero.StateHero;
import com.rubentxu.juegos.core.modelo.Item;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.BaseState;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPO;
import com.rubentxu.juegos.core.modelo.base.Box2dPhysicsCompoundObject;

import java.util.HashMap;
import java.util.Map;

public class ModelsAndViews {


    private final ResourcesManager resourcesManager;
    private final World world;

    /**
     * Animations *
     */
    private HashMap<String, Animation> animationHero;
    private HashMap<String, Animation> animationEnemy;
    private HashMap<String, Animation> animationWater;
    private HashMap<String, Animation> animationMovingPlatform;
    private Map<String, Animation> animationItemCoin;
    private HashMap<String, Animation> animationMotorMill;
    private HashMap<String, Animation> animationAspasMolino;


    public ModelsAndViews(ResourcesManager resourcesManager, World world) {
        this.resourcesManager = resourcesManager;
        this.world = world;
        loadHeroAnimations();
        loadEnemyAnimations();
        loadWaterAnimations();
        loadMovingPlatformAnimations();
        loadItemsCoinAnimations();
        loadMotorMolinoAnimations();
        loadAspasMolinoAnimations();
    }

    public void render(SpriteBatch batch) {
        Vector2 originDefault = new Vector2(0, 0);
        for (Box2DPhysicsObject e : world.getEntities()) {
            TextureRegion frame = null;
            TextureRegion frame2 = null;
            Map<String, Animation> anims = getAnimation(e);
            Map<String, Animation> anims2 = null;
            if (e instanceof Box2dPhysicsCompoundObject) anims2 = getAnimation2(e);

            if (anims != null) {
                try {
                    if (!e.getState().equals(BaseState.DESTROY)) {
                        frame = anims.get(String.valueOf(e.getState())).getKeyFrame(e.getStateTime());
                        if (anims2 != null)
                            frame2 = anims2.get(String.valueOf(e.getState())).getKeyFrame(e.getStateTime());

                        if (e.isFacingLeft() && !frame.isFlipX()) {
                            frame.flip(true, false);
                        } else if (!e.isFacingLeft() && frame.isFlipX()) {
                            frame.flip(true, false);
                        }
                        batch.draw(frame, e.getXBodyA() - e.getOriginBodyA().x, e.getYBodyA() - e.getOriginBodyA().y,
                                e.getOriginBodyA().x, e.getOriginBodyA().y, e.getWidthBodyA(), e.getHeightBodyA(),
                                e.getScaleBodyA().x, e.getScaleBodyA().y, e.getRotationBodyA());
                        if (frame2 != null) {
                            Box2dPhysicsCompoundObject e2 = (Box2dPhysicsCompoundObject) e;
                            batch.draw(frame2, e2.getXBodyB() - e2.getOriginBodyB().x, e2.getYBodyB() - e2.getOriginBodyB().y,
                                    e2.getOriginBodyB().x, e2.getOriginBodyB().y, e2.getWidthBodyB(), e2.getHeightBodyB(),
                                    e2.getScaleBodyB().x, e2.getScaleBodyB().y, e2.getRotationB());
                        }
                    }

                } catch (Exception ex) {
                    Gdx.app.log(Constants.LOG, "Error en render: " + ex.getMessage() + "Grupo " + e.getGrupo() + " State" + e.getState());
                }
            }
            if (e.getEffect() != null) {
                e.getEffect().draw(batch);
            }

        }
    }

    private Map<String, Animation> getAnimation(Box2DPhysicsObject e) {

        if (e.getGrupo().equals(GRUPO.HERO)) {
            return animationHero;
        } else if (e.getGrupo().equals(GRUPO.ENEMY)) {
            return animationEnemy;
        } else if (e.getGrupo().equals(GRUPO.FLUID)) {
            return animationWater;
        } else if (e.getGrupo().equals(GRUPO.MOVING_PLATFORM)) {
            return animationMovingPlatform;
        } else if (e.getGrupo().equals(GRUPO.ITEMS)) {
            if (((Item) e).getType().equals(Item.TYPE.COIN)) return animationItemCoin;
        } else if (e.getGrupo().equals(GRUPO.MILL)) {
            return animationMotorMill;
        }
        return null;
    }

    private Map<String, Animation> getAnimation2(Box2DPhysicsObject e) {
        if (e.getGrupo().equals(GRUPO.MILL)) {
            return animationAspasMolino;
        }
        return null;
    }

    private void loadHeroAnimations() {
        TextureAtlas atlas = resourcesManager.get(resourcesManager.SPRITE_ATLAS);
        TextureAtlas atlasVarios = resourcesManager.get(resourcesManager.VARIOS_ATLAS);

        Array<TextureAtlas.AtlasRegion> heroWalking = atlas.findRegions("Andando");
        Array<TextureAtlas.AtlasRegion> heroJump = atlas.findRegions("Saltando");
        Array<TextureAtlas.AtlasRegion> heroFall = atlas.findRegions("Cayendo");
        Array<TextureAtlas.AtlasRegion> heroIdle = atlas.findRegions("Parado");
        Array<TextureAtlas.AtlasRegion> heroSwimming = atlasVarios.findRegions("nadando");


        Animation walking = new Animation(Constants.RUNNING_FRAME_DURATION, heroWalking, Animation.LOOP);
        Animation jump = new Animation(Constants.RUNNING_FRAME_DURATION * 7, heroJump, Animation.NORMAL);
        Animation fall = new Animation(Constants.RUNNING_FRAME_DURATION * 5, heroFall, Animation.NORMAL);
        Animation idle = new Animation(Constants.RUNNING_FRAME_DURATION * 4, heroIdle, Animation.LOOP);
        Animation swimming = new Animation(Constants.RUNNING_FRAME_DURATION * 4, heroSwimming, Animation.LOOP);

        animationHero = new HashMap<String, Animation>();
        animationHero.put(String.valueOf(StateHero.WALKING), walking);
        animationHero.put(String.valueOf(StateHero.JUMPING), jump);
        animationHero.put(String.valueOf(StateHero.FALL), fall);
        animationHero.put(String.valueOf(BaseState.HURT), fall);
        animationHero.put(String.valueOf(StateHero.IDLE), idle);
        animationHero.put(String.valueOf(StateHero.SWIMMING), swimming);
        animationHero.put(String.valueOf(StateHero.PROPULSION), swimming);
        animationHero.put(String.valueOf(BaseState.HIT), fall);
    }

    private void loadEnemyAnimations() {

        TextureAtlas atlasVarios = resourcesManager.get(ResourcesManager.VARIOS_ATLAS);
        Array<TextureAtlas.AtlasRegion> enemy = atlasVarios.findRegions("ENEMY");
        Animation walking = new Animation(Constants.RUNNING_FRAME_DURATION, enemy, Animation.LOOP);
        Array<TextureAtlas.AtlasRegion> dead = atlasVarios.findRegions("ENEMY");
        dead.get(0).flip(true, true);

        Animation deadEnemy = new Animation(Constants.RUNNING_FRAME_DURATION, dead, Animation.LOOP);
        animationEnemy = new HashMap<String, Animation>();
        animationEnemy.put(String.valueOf(Enemy.StateEnemy.WALKING), walking);
        animationEnemy.put(String.valueOf(Enemy.StateEnemy.JUMPING), walking);
        animationEnemy.put(String.valueOf(Enemy.StateEnemy.FALL), walking);
        animationEnemy.put(String.valueOf(Enemy.StateEnemy.IDLE), walking);
        animationEnemy.put(String.valueOf(BaseState.HIT), walking);
        animationEnemy.put(String.valueOf(BaseState.HURT), walking);
        animationEnemy.put(String.valueOf(BaseState.DEAD), deadEnemy);
    }

    private void loadWaterAnimations() {

        TextureAtlas atlasVarios = resourcesManager.get(ResourcesManager.VARIOS_ATLAS);
        Array<TextureAtlas.AtlasRegion> water = atlasVarios.findRegions("agua");
        Animation defaultState = new Animation(Constants.RUNNING_FRAME_DURATION, water, Animation.LOOP);
        animationWater = new HashMap<String, Animation>();
        animationWater.put(String.valueOf(BaseState.DEFAULT), defaultState);

    }

    private void loadMotorMolinoAnimations() {
        TextureAtlas objectsAtlas = resourcesManager.get(ResourcesManager.OBJECTS_ATLAS);
        Array<TextureAtlas.AtlasRegion> motorMolino = objectsAtlas.findRegions("motorMolino");
        Animation defaultState = new Animation(Constants.RUNNING_FRAME_DURATION, motorMolino, Animation.LOOP);
        animationMotorMill = new HashMap<String, Animation>();
        animationMotorMill.put(String.valueOf(BaseState.DEFAULT), defaultState);
    }

    private void loadAspasMolinoAnimations() {
        TextureAtlas objectsAtlas = resourcesManager.get(ResourcesManager.OBJECTS_ATLAS);
        Array<TextureAtlas.AtlasRegion> aspasMolino = objectsAtlas.findRegions("aspasMolino");
        Animation defaultState = new Animation(Constants.RUNNING_FRAME_DURATION, aspasMolino, Animation.LOOP);
        animationAspasMolino = new HashMap<String, Animation>();
        animationAspasMolino.put(String.valueOf(BaseState.DEFAULT), defaultState);
    }


    private void loadMovingPlatformAnimations() {

        TextureAtlas atlasVarios = resourcesManager.get(ResourcesManager.VARIOS_ATLAS);
        Array<TextureAtlas.AtlasRegion> moving_platform = atlasVarios.findRegions("MOVING_PLATFORM");
        Animation defaultState = new Animation(Constants.RUNNING_FRAME_DURATION, moving_platform, Animation.LOOP);
        animationMovingPlatform = new HashMap<String, Animation>();
        animationMovingPlatform.put(String.valueOf(BaseState.DEFAULT), defaultState);

    }

    private void loadItemsCoinAnimations() {

        TextureAtlas atlasGui = resourcesManager.get(ResourcesManager.GUI_ATLAS);
        Array<TextureAtlas.AtlasRegion> moving_platform = atlasGui.findRegions("tijeras");
        Animation defaultState = new Animation(Constants.RUNNING_FRAME_DURATION, moving_platform, Animation.LOOP);
        animationItemCoin = new HashMap<String, Animation>();
        animationItemCoin.put(String.valueOf(BaseState.DEFAULT), defaultState);
    }
}
