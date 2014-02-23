package com.rubentxu.juegos.core.vista;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.managers.game.ResourcesManager;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Hero.StateHero;
import com.rubentxu.juegos.core.modelo.Item;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.BaseState;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelsAndViews {


    private final ResourcesManager resourcesManager;

    /**
     * Animations *
     */
    private HashMap<String, Animation> animationHero;
    private HashMap<String, Animation> animationEnemy;
    private HashMap<String, Animation> animationWater;
    private HashMap<String, Animation> animationMovingPlatform;
    private Map<String,Animation> animationItemCoin;

    private List<Box2DPhysicsObject> entities = new ArrayList<Box2DPhysicsObject>();
    private ParticleEffect dustParticles;

    public ModelsAndViews(ResourcesManager resourcesManager){
        this.resourcesManager = resourcesManager;
        loadHeroAnimations();
        loadEnemyAnimations();
        loadWaterAnimations();
        loadMovingPlatformAnimations();
        loadItemsCoinAnimations();
    }

    public void render(SpriteBatch batch) {
        TextureRegion frame;
        for (Box2DPhysicsObject e : entities) {
            Map<String, Animation> anims = getAnimation(e);
            float offsetX=0;
            float offsetY=0;
            float offsetWidth=0;
            if(e.getGrupo().equals(GRUPO.HERO)){

                offsetY= e.getHeight()/2;
                offsetWidth= e.getWidth()*0.8f;
                offsetX= e.getWidth()/2+offsetWidth/2;
            }
            if (anims != null) {
                try{
                    frame = anims.get(String.valueOf(e.getState())).getKeyFrame(e.getStateTime());

                    if (e.isFacingLeft() && !frame.isFlipX()) {
                        frame.flip(true, false);
                    } else if (!e.isFacingLeft() && frame.isFlipX()) {
                        frame.flip(true, false);
                    }
                    batch.draw(frame, e.getX()-offsetX , e.getY()-offsetY, e.getWidth()+offsetWidth, e.getHeight());

                }catch (Exception ex){
                   Gdx.app.log(Constants.LOG,"Error en render: "+ ex.getMessage()+ "Grupo "+e.getGrupo()+" State"+e.getState());
                }
            }
            if(e.getEffect()!=null){
                e.getEffect().draw(batch);
            }
        }
    }

    private Map<String, Animation> getAnimation(Box2DPhysicsObject e) {

        if (e.getGrupo().equals(GRUPO.HERO)) {
            return animationHero;
        } else if(e.getGrupo().equals(GRUPO.ENEMY)) {
            return animationEnemy;
        } else if(e.getGrupo().equals(GRUPO.FLUID)) {
            return animationWater;
        } else if(e.getGrupo().equals(GRUPO.MOVING_PLATFORM)) {
            return animationMovingPlatform;
        }else if(e.getGrupo().equals(GRUPO.ITEMS)) {
            if(((Item)e).getType().equals(Item.TYPE.COIN)) return animationItemCoin;
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


        Animation walking = new Animation(Constants.RUNNING_FRAME_DURATION, heroWalking,Animation.LOOP);
        Animation jump = new Animation(Constants.RUNNING_FRAME_DURATION * 7, heroJump,Animation.NORMAL);
        Animation fall = new Animation(Constants.RUNNING_FRAME_DURATION * 5, heroFall,Animation.NORMAL);
        Animation idle = new Animation(Constants.RUNNING_FRAME_DURATION * 4, heroIdle,Animation.LOOP);
        Animation swimming = new Animation(Constants.RUNNING_FRAME_DURATION * 4, heroSwimming,Animation.LOOP);

        animationHero = new HashMap<String, Animation>();
        animationHero.put(String.valueOf(StateHero.WALKING), walking);
        animationHero.put(String.valueOf(StateHero.JUMPING), jump);
        animationHero.put(String.valueOf(StateHero.FALL), fall);
        animationHero.put(String.valueOf(StateHero.HURT), fall);
        animationHero.put(String.valueOf(StateHero.IDLE), idle);
        animationHero.put(String.valueOf(StateHero.SWIMMING), swimming);
        animationHero.put(String.valueOf(StateHero.PROPULSION), swimming);
    }

    private void loadEnemyAnimations() {

        TextureAtlas atlasVarios = resourcesManager.get(ResourcesManager.VARIOS_ATLAS);
        Array<TextureAtlas.AtlasRegion> enemy = atlasVarios.findRegions("ENEMY");
        Animation walking = new Animation(Constants.RUNNING_FRAME_DURATION, enemy,Animation.LOOP);
        animationEnemy= new HashMap<String,Animation>();
        animationEnemy.put(String.valueOf(Enemy.StateEnemy.WALKING), walking);
        animationEnemy.put(String.valueOf(Enemy.StateEnemy.JUMPING), walking);
        animationEnemy.put(String.valueOf(Enemy.StateEnemy.FALL), walking);
        animationEnemy.put(String.valueOf(Enemy.StateEnemy.IDLE), walking);
        animationEnemy.put(String.valueOf(Enemy.StateEnemy.HIT), walking);
        animationEnemy.put(String.valueOf(Enemy.StateEnemy.HURT), walking);
    }

    private void loadWaterAnimations() {

        TextureAtlas atlasVarios = resourcesManager.get(ResourcesManager.VARIOS_ATLAS);
        Array<TextureAtlas.AtlasRegion> water = atlasVarios.findRegions("agua");
        Animation defaultState = new Animation(Constants.RUNNING_FRAME_DURATION, water,Animation.LOOP);
        animationWater= new HashMap<String,Animation>();
        animationWater.put(String.valueOf(BaseState.DEFAULT), defaultState);

    }

    private void loadMovingPlatformAnimations() {

        TextureAtlas atlasVarios = resourcesManager.get(ResourcesManager.VARIOS_ATLAS);
        Array<TextureAtlas.AtlasRegion> moving_platform = atlasVarios.findRegions("MOVING_PLATFORM");
        Animation defaultState = new Animation(Constants.RUNNING_FRAME_DURATION, moving_platform,Animation.LOOP);
        animationMovingPlatform= new HashMap<String,Animation>();
        animationMovingPlatform.put(String.valueOf(BaseState.DEFAULT), defaultState);

    }

    private void loadItemsCoinAnimations() {

        TextureAtlas atlasGui = resourcesManager.get(ResourcesManager.GUI_ATLAS);
        Array<TextureAtlas.AtlasRegion> moving_platform = atlasGui.findRegions("tijeras");
        Animation defaultState = new Animation(Constants.RUNNING_FRAME_DURATION, moving_platform,Animation.LOOP);
        animationItemCoin= new HashMap<String,Animation>();
        animationItemCoin.put(String.valueOf(BaseState.DEFAULT), defaultState);
    }

    public void addEntity(Box2DPhysicsObject e) {
        entities.add(e);

    }
}
