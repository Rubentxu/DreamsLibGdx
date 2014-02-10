package com.rubentxu.juegos.core.vista;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Hero;
import com.rubentxu.juegos.core.modelo.Hero.StateHero;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.BaseState;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPO;
import com.rubentxu.juegos.core.servicios.Assets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ModelsAndViews {


    public ModelsAndViews(){
        loadHeroAnimations();
        loadEnemyAnimations();
        loadWaterAnimations();
        loadMovingPlatformAnimations();
    }

    /**
     * Textures *
     */
    private Array<TextureAtlas.AtlasRegion> heroJumpRight;
    private Array<TextureAtlas.AtlasRegion> heroFallRight;
    private Array<TextureAtlas.AtlasRegion> heroIdleRight;
    private Array<TextureAtlas.AtlasRegion> heroSwimmingRight;
    /**
     * Animations *
     */
    private HashMap<String, Animation> animationHero;
    private HashMap<String, Animation> animationEnemy;
    private HashMap<String, Animation> animationWater;
    private HashMap<String, Animation> animationMovingPlatform;


    /**
     * Stats Profile
     */
    private Table Score;
    private Map<Box2DPhysicsObject, Sprite> modelsAndViews = new HashMap<Box2DPhysicsObject, Sprite>();
    private List<Box2DPhysicsObject> entities = new ArrayList<Box2DPhysicsObject>();
    private ParticleEffect dustParticles;
    private float timeIdle;
    private Hero hero;


    public void render(SpriteBatch spriteBatch) {
        Iterator it = modelsAndViews.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry mav = (Map.Entry) it.next();
            Sprite sprite = (Sprite) mav.getValue();
            Box2DPhysicsObject box2DPhysicsObject = (Box2DPhysicsObject) mav.getKey();
            if (box2DPhysicsObject.getGrupo().equals(GRUPO.MOVING_PLATFORM)
                    || box2DPhysicsObject.getGrupo().equals(GRUPO.ENEMY))
                updateModelPosition(box2DPhysicsObject, sprite);

            if (((Box2DPhysicsObject) mav.getKey()).isFlaggedForDelete()) {

            }
            sprite.draw(spriteBatch);

        }
        render2(spriteBatch);
        //dustParticles.update(Gdx.graphics.getDeltaTime());
        //dustParticles.draw(spriteBatch);
    }

    public void render2(SpriteBatch batch) {
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
                //System.out.println("GET FRAME ANIMATION " +e.getGrupo()+" State: " + e.getState() + " StateTime " + e.getStateTime());
                frame = anims.get(String.valueOf(e.getState())).getKeyFrame(e.getStateTime());

                if (e.isFacingLeft() && !frame.isFlipX()) {
                    frame.flip(true, false);
                } else if (!e.isFacingLeft() && frame.isFlipX()) {
                    frame.flip(true, false);
                }
                batch.draw(frame, e.getX()-offsetX , e.getY()-offsetY, e.getWidth()+offsetWidth, e.getHeight());
            }
        }
    }

    private Map<String, Animation> getAnimation(Box2DPhysicsObject e) {

        if (e.getGrupo().equals(GRUPO.HERO)) {
            //System.out.println("GET ANIMATION " + e.getGrupo());
            return animationHero;
        } else if(e.getGrupo().equals(GRUPO.ENEMY)) {
            return animationEnemy;
        } else if(e.getGrupo().equals(GRUPO.FLUID)) {
            return animationWater;
        } else if(e.getGrupo().equals(GRUPO.MOVING_PLATFORM)) {
            return animationMovingPlatform;
        }
        return null;
    }

    private void loadHeroAnimations() {
        TextureAtlas atlas = Assets.getInstance().get(Assets.getInstance().SPRITE_ATLAS);
        TextureAtlas atlasVarios = Assets.getInstance().get(Assets.getInstance().VARIOS_ATLAS);

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
        animationHero.put(String.valueOf(StateHero.IDLE), idle);
        animationHero.put(String.valueOf(StateHero.SWIMMING), swimming);
    }

    private void loadEnemyAnimations() {

        TextureAtlas atlasVarios = Assets.getInstance().get(Assets.getInstance().VARIOS_ATLAS);
        Array<TextureAtlas.AtlasRegion> enemy = atlasVarios.findRegions("ENEMY");
        Animation walking = new Animation(Constants.RUNNING_FRAME_DURATION, enemy,Animation.LOOP);
        animationEnemy= new HashMap<String,Animation>();
        animationEnemy.put(String.valueOf(StateHero.WALKING), walking);
        animationEnemy.put(String.valueOf(StateHero.JUMPING), walking);
        animationEnemy.put(String.valueOf(StateHero.FALL), walking);
        animationEnemy.put(String.valueOf(StateHero.IDLE), walking);
    }

    private void loadWaterAnimations() {

        TextureAtlas atlasVarios = Assets.getInstance().get(Assets.getInstance().VARIOS_ATLAS);
        Array<TextureAtlas.AtlasRegion> water = atlasVarios.findRegions("agua");
        Animation defaultState = new Animation(Constants.RUNNING_FRAME_DURATION, water,Animation.LOOP);
        animationWater= new HashMap<String,Animation>();
        animationWater.put(String.valueOf(BaseState.DEFAULT), defaultState);

    }

    private void loadMovingPlatformAnimations() {

        TextureAtlas atlasVarios = Assets.getInstance().get(Assets.getInstance().VARIOS_ATLAS);
        Array<TextureAtlas.AtlasRegion> moving_platform = atlasVarios.findRegions("MOVING_PLATFORM");
        Animation defaultState = new Animation(Constants.RUNNING_FRAME_DURATION, moving_platform,Animation.LOOP);
        animationMovingPlatform= new HashMap<String,Animation>();
        animationMovingPlatform.put(String.valueOf(BaseState.DEFAULT), defaultState);

    }


    private void updateModelPosition(Box2DPhysicsObject box2DPhysicsObject, Sprite sprite) {
        sprite.setPosition(box2DPhysicsObject.getBody().getPosition().x,
                box2DPhysicsObject.getBody().getPosition().y);

        if (box2DPhysicsObject.getGrupo().equals(GRUPO.ENEMY)) {
            if (((Enemy) box2DPhysicsObject).isFacingLeft() && !sprite.isFlipX()) sprite.flip(true, false);
            if (!((Enemy) box2DPhysicsObject).isFacingLeft() && sprite.isFlipX()) sprite.flip(true, false);
        }


    }

    public void addModelAndView(Box2DPhysicsObject box2DPhysicsObject, Sprite sprite) {
        sprite.setSize(box2DPhysicsObject.getWidth(), box2DPhysicsObject.getHeight());
        sprite.setOrigin(box2DPhysicsObject.getWidth() / 2, box2DPhysicsObject.getHeight() / 2);
        sprite.setPosition(box2DPhysicsObject.getBody().getPosition().x, box2DPhysicsObject.getBody().getPosition().y);
        System.out.println("Nombre: " + box2DPhysicsObject.getNombre() + " Position: " + box2DPhysicsObject.getBody().getPosition() + " Grupo: " + box2DPhysicsObject.getGrupo());
        this.modelsAndViews.put(box2DPhysicsObject, sprite);
    }



    public Map<Box2DPhysicsObject, Sprite> getModelsAndViews() {
        return modelsAndViews;
    }

    public void update(World world) {
        for (Body b : world.getBodiesFlaggedDestroy()) {
            if (modelsAndViews.containsKey(b.getUserData())) {
                modelsAndViews.remove(b.getUserData());
            }
        }
    }

    public void addEntity(Box2DPhysicsObject e) {
        entities.add(e);

    }
}
