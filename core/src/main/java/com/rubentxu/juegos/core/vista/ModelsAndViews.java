package com.rubentxu.juegos.core.vista;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Rubentxu;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.servicios.Assets;
import com.rubentxu.juegos.core.utils.dermetfan.graphics.AnimatedBox2DSprite;
import com.rubentxu.juegos.core.utils.dermetfan.graphics.AnimatedSprite;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ModelsAndViews {

    /**
     * Textures *
     */
    private Array<TextureAtlas.AtlasRegion> rubenJumpRight;
    private Array<TextureAtlas.AtlasRegion> rubenFallRight;
    private Array<TextureAtlas.AtlasRegion> rubenIdleRight;
    private Array<TextureAtlas.AtlasRegion> rubenSwimmingRight;
    /**
     * Animations *
     */
    private AnimatedBox2DSprite AnimationRuben;
    private AnimatedSprite walkRightAnimation;
    private AnimatedSprite jumpRightAnimation;
    private AnimatedSprite fallRightAnimation;
    private AnimatedSprite walkLeftAnimation;
    private AnimatedSprite jumpLeftAnimation;
    private AnimatedSprite fallLeftAnimation;
    private AnimatedSprite idleRightAnimation;
    private AnimatedSprite idleLeftAnimation;
    private AnimatedSprite swimmingRightAnimation;
    private AnimatedSprite swimmingLeftAnimation;


    private Map<Box2DPhysicsObject,Sprite> modelsAndViews= new HashMap<Box2DPhysicsObject,Sprite>();
    private ParticleEffect dustParticles;
    private float timeIdle;
    private Rubentxu ruben;

    public void render(SpriteBatch spriteBatch){
        Iterator it = modelsAndViews.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry mav = (Map.Entry)it.next();
            Sprite sprite=(Sprite) mav.getValue();
            Box2DPhysicsObject box2DPhysicsObject=(Box2DPhysicsObject)mav.getKey();
            if(box2DPhysicsObject.getGrupo().equals(Box2DPhysicsObject.GRUPOS.PLATAFORMAS_MOVILES)
                    || box2DPhysicsObject.getGrupo().equals(Box2DPhysicsObject.GRUPOS.ENEMIGOS)) updateModelPosition(box2DPhysicsObject,sprite);

            sprite.draw(spriteBatch);



        }
        updateAnimationsRubentxu(ruben);
        AnimationRuben.update();
        AnimationRuben.draw(spriteBatch);
        dustParticles.update(Gdx.graphics.getDeltaTime());
        dustParticles.draw(spriteBatch);
    }

    private void updateModelPosition(Box2DPhysicsObject box2DPhysicsObject, Sprite sprite){
        sprite.setPosition(box2DPhysicsObject.getBody().getPosition().x ,
                box2DPhysicsObject.getBody().getPosition().y);

        if (box2DPhysicsObject.getGrupo().equals(Box2DPhysicsObject.GRUPOS.ENEMIGOS) ){
            if(((Enemy)box2DPhysicsObject).isFacingLeft() && !sprite.isFlipX()) sprite.flip(true,false);
            if(!((Enemy)box2DPhysicsObject).isFacingLeft() && sprite.isFlipX()) sprite.flip(true,false);
        }


    }

    public void addModelAndView (Box2DPhysicsObject box2DPhysicsObject, Sprite sprite) {
        sprite.setSize(box2DPhysicsObject.getWidth(),box2DPhysicsObject.getHeight());
        sprite.setOrigin(box2DPhysicsObject.getWidth() / 2, box2DPhysicsObject.getHeight() / 2);
        sprite.setPosition(box2DPhysicsObject.getBody().getPosition().x,box2DPhysicsObject.getBody().getPosition().y);
        System.out.println("Nombre: "+box2DPhysicsObject.getNombre()+" Position: "+box2DPhysicsObject.getBody().getPosition()+" Grupo: "+ box2DPhysicsObject.getGrupo());
        this.modelsAndViews.put(box2DPhysicsObject,sprite);
    }

    public void addModelAndBuildView(Rubentxu ruben){
        TextureAtlas atlas = Assets.getInstance().get("imagenes/texturas/sprites.pack");
        TextureAtlas atlasVarios = Assets.getInstance().get("imagenes/texturas/varios.pack");
        this.ruben=ruben;
        // Particles
        dustParticles = Assets.getInstance().get(Assets.PARTICLE_EFFECT);

        Array<TextureAtlas.AtlasRegion> rubenRight = atlas.findRegions("Andando");
        rubenJumpRight = atlas.findRegions("Saltando");
        rubenFallRight = atlas.findRegions("Cayendo");
        rubenIdleRight = atlas.findRegions("Parado");
        rubenSwimmingRight = atlasVarios.findRegions("nadando");

        Animation walkRight = new Animation(Constants.RUNNING_FRAME_DURATION, rubenRight);
        walkRight.setPlayMode(Animation.LOOP);
        walkRightAnimation = new AnimatedSprite(walkRight);

        Animation jumpRight = new Animation(Constants.RUNNING_FRAME_DURATION * 7, rubenJumpRight);
        jumpRightAnimation = new AnimatedSprite(jumpRight);
        Animation fallRight = new Animation(Constants.RUNNING_FRAME_DURATION * 5, rubenFallRight);
        fallRightAnimation = new AnimatedSprite(fallRight);

        Animation idleRight = new Animation(Constants.RUNNING_FRAME_DURATION * 4, rubenIdleRight);
        idleRight.setPlayMode(Animation.LOOP);
        idleRightAnimation = new AnimatedSprite(idleRight);

        Animation swimmingRight = new Animation(Constants.RUNNING_FRAME_DURATION * 4, rubenSwimmingRight);
        swimmingRight.setPlayMode(Animation.LOOP);
        swimmingRightAnimation = new AnimatedSprite(swimmingRight);

        walkLeftAnimation = convertToLeft(rubenRight, 1);
        walkLeftAnimation.getAnimation().setPlayMode(Animation.LOOP);
        jumpLeftAnimation = convertToLeft(rubenJumpRight, 7);
        fallLeftAnimation = convertToLeft(rubenFallRight, 5);
        idleLeftAnimation = convertToLeft(rubenIdleRight, 4);
        idleLeftAnimation.getAnimation().setPlayMode(Animation.LOOP);
        swimmingLeftAnimation = convertToLeft(rubenSwimmingRight, 4);
        swimmingLeftAnimation.getAnimation().setPlayMode(Animation.LOOP);

        AnimationRuben = new AnimatedBox2DSprite(walkRightAnimation);
        AnimationRuben.setSize(ruben.getWidth()*1.5f , ruben.getHeight());
        AnimationRuben.setOrigin(AnimationRuben.getWidth() / 2, AnimationRuben.getHeight() / 1.9f);
        AnimationRuben.setPosition(ruben.getBody().getPosition().x ,
                ruben.getBody().getPosition().y );

    }

    public AnimatedSprite convertToLeft(Array<TextureAtlas.AtlasRegion> atlasRegions, int mul) {
        Array<TextureAtlas.AtlasRegion> atlasRegionsTmp = new Array<TextureAtlas.AtlasRegion>();
        for (TextureAtlas.AtlasRegion r : atlasRegions) {
            TextureAtlas.AtlasRegion r2 = new TextureAtlas.AtlasRegion(r);
            r2.flip(true, false);
            atlasRegionsTmp.add(r2);
        }
        return new AnimatedSprite(new Animation(Constants.RUNNING_FRAME_DURATION * mul, atlasRegionsTmp));

    }

    private void updateAnimationsRubentxu(Rubentxu ruben) {
        dustParticles.setPosition(ruben.getBody().getPosition().x ,
                ruben.getBody().getPosition().y - ruben.getHeight() / 2.2f);
        if (ruben.isFacingLeft()) {
            AnimationRuben.setAnimatedSprite(walkLeftAnimation);
        } else {
            AnimationRuben.setAnimatedSprite(walkRightAnimation);
        }
        if (ruben.getState().equals(Rubentxu.State.IDLE)) {
            dustParticles.allowCompletion();
            if (walkRightAnimation.getTime() == 0 && walkLeftAnimation.getTime() == 0) AnimationRuben.stop();
            timeIdle += Gdx.graphics.getDeltaTime();
            walkRightAnimation.setTime(0);
            walkLeftAnimation.setTime(0);

            if (timeIdle > 2) {
                if (ruben.isFacingLeft()) {
                    AnimationRuben.setAnimatedSprite(idleLeftAnimation);
                } else {
                    AnimationRuben.setAnimatedSprite(idleRightAnimation);
                }
            }
        }

        if (ruben.getState().equals(Rubentxu.State.WALKING)) {

            if (dustParticles.isComplete()) dustParticles.start();
            timeIdle = 0;
            AnimationRuben.play();
            jumpRightAnimation.setTime(0);
            fallRightAnimation.setTime(0);
            jumpLeftAnimation.setTime(0);
            fallLeftAnimation.setTime(0);
            idleLeftAnimation.setTime(0);
            idleRightAnimation.setTime(0);
            swimmingLeftAnimation.setTime(0);
            swimmingRightAnimation.setTime(0);

            if (ruben.isFacingLeft()) {
                AnimationRuben.setAnimatedSprite(walkLeftAnimation);
            } else {
                AnimationRuben.setAnimatedSprite(walkRightAnimation);
            }
        } else if (ruben.getState().equals(Rubentxu.State.JUMPING)) {
            dustParticles.allowCompletion();
            if (ruben.isFacingLeft()) {
                AnimationRuben.setAnimatedSprite(jumpLeftAnimation);
            } else {
                AnimationRuben.setAnimatedSprite(jumpRightAnimation);
            }
        } else if (ruben.getState().equals(Rubentxu.State.FALL)) {
            if (ruben.isFacingLeft()) {
                AnimationRuben.setAnimatedSprite(fallLeftAnimation);
            } else {
                AnimationRuben.setAnimatedSprite(fallRightAnimation);
            }
        }   else if (ruben.getState().equals(Rubentxu.State.SWIMMING)) {
            if (ruben.isFacingLeft()) {
                AnimationRuben.setAnimatedSprite(swimmingLeftAnimation);
            } else {
                AnimationRuben.setAnimatedSprite(swimmingRightAnimation);
            }
        }

        AnimationRuben.setPosition(ruben.getBody().getPosition().x - AnimationRuben.getWidth() / 2,
                ruben.getBody().getPosition().y - AnimationRuben.getHeight() / 2);

    }

    public Map<Box2DPhysicsObject, Sprite> getModelsAndViews() {
        return modelsAndViews;
    }
}
