package com.rubentxu.juegos.core.vista;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Hero;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject.GRUPO;
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
    private Array<TextureAtlas.AtlasRegion> heroJumpRight;
    private Array<TextureAtlas.AtlasRegion> heroFallRight;
    private Array<TextureAtlas.AtlasRegion> heroIdleRight;
    private Array<TextureAtlas.AtlasRegion> heroSwimmingRight;
    /**
     * Animations *
     */
    private AnimatedBox2DSprite animationHero;
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
    /**
     * Stats Profile
     */
    private Table Score;
    private Map<Box2DPhysicsObject, Sprite> modelsAndViews = new HashMap<Box2DPhysicsObject, Sprite>();
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

            if(((Box2DPhysicsObject) mav.getKey()).isFlaggedForDelete()) {

            }
            sprite.draw(spriteBatch);

        }
        updateAnimationsRubentxu(hero);
        animationHero.update();
        animationHero.draw(spriteBatch);
        dustParticles.update(Gdx.graphics.getDeltaTime());
        dustParticles.draw(spriteBatch);
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

    public void addModelAndBuildView(Hero hero) {


        TextureAtlas atlas = Assets.getInstance().get(Assets.getInstance().SPRITE_ATLAS);
        TextureAtlas atlasVarios = Assets.getInstance().get(Assets.getInstance().VARIOS_ATLAS);
        this.hero = hero;
        // Particles
        dustParticles = Assets.getInstance().get(Assets.PARTICLE_EFFECT);

        Array<TextureAtlas.AtlasRegion> heroRight = atlas.findRegions("Andando");
        heroJumpRight = atlas.findRegions("Saltando");
        heroFallRight = atlas.findRegions("Cayendo");
        heroIdleRight = atlas.findRegions("Parado");
        heroSwimmingRight = atlasVarios.findRegions("nadando");

        Animation walkRight = new Animation(Constants.RUNNING_FRAME_DURATION, heroRight);
        walkRight.setPlayMode(Animation.LOOP);
        walkRightAnimation = new AnimatedSprite(walkRight);

        Animation jumpRight = new Animation(Constants.RUNNING_FRAME_DURATION * 7, heroJumpRight);
        jumpRightAnimation = new AnimatedSprite(jumpRight);
        Animation fallRight = new Animation(Constants.RUNNING_FRAME_DURATION * 5, heroFallRight);
        fallRightAnimation = new AnimatedSprite(fallRight);

        Animation idleRight = new Animation(Constants.RUNNING_FRAME_DURATION * 4, heroIdleRight);
        idleRight.setPlayMode(Animation.LOOP);
        idleRightAnimation = new AnimatedSprite(idleRight);

        Animation swimmingRight = new Animation(Constants.RUNNING_FRAME_DURATION * 4, heroSwimmingRight);
        swimmingRight.setPlayMode(Animation.LOOP);
        swimmingRightAnimation = new AnimatedSprite(swimmingRight);

        walkLeftAnimation = convertToLeft(heroRight, 1);
        walkLeftAnimation.getAnimation().setPlayMode(Animation.LOOP);
        jumpLeftAnimation = convertToLeft(heroJumpRight, 7);
        fallLeftAnimation = convertToLeft(heroFallRight, 5);
        idleLeftAnimation = convertToLeft(heroIdleRight, 4);
        idleLeftAnimation.getAnimation().setPlayMode(Animation.LOOP);
        swimmingLeftAnimation = convertToLeft(heroSwimmingRight, 4);
        swimmingLeftAnimation.getAnimation().setPlayMode(Animation.LOOP);

        animationHero = new AnimatedBox2DSprite(walkRightAnimation);
        animationHero.setSize(hero.getWidth() * 1.5f, hero.getHeight());
        animationHero.setOrigin(animationHero.getWidth() / 2, animationHero.getHeight() / 1.9f);
        animationHero.setPosition(hero.getBody().getPosition().x,
                hero.getBody().getPosition().y);

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

    private void updateAnimationsRubentxu(Hero hero) {
        dustParticles.setPosition(hero.getBody().getPosition().x,
                hero.getBody().getPosition().y - hero.getHeight() / 2.2f);
        if (hero.isFacingLeft()) {
            animationHero.setAnimatedSprite(walkLeftAnimation);
        } else {
            animationHero.setAnimatedSprite(walkRightAnimation);
        }
        if (hero.getState().equals(Hero.State.IDLE)) {
            dustParticles.allowCompletion();
            if (walkRightAnimation.getTime() == 0 && walkLeftAnimation.getTime() == 0) animationHero.stop();
            timeIdle += Gdx.graphics.getDeltaTime();
            walkRightAnimation.setTime(0);
            walkLeftAnimation.setTime(0);

            if (timeIdle > 2) {
                if (hero.isFacingLeft()) {
                    animationHero.setAnimatedSprite(idleLeftAnimation);
                } else {
                    animationHero.setAnimatedSprite(idleRightAnimation);
                }
            }
        }

        if (hero.getState().equals(Hero.State.WALKING)) {

            if (dustParticles.isComplete()) dustParticles.start();
            timeIdle = 0;
            animationHero.play();
            jumpRightAnimation.setTime(0);
            fallRightAnimation.setTime(0);
            jumpLeftAnimation.setTime(0);
            fallLeftAnimation.setTime(0);
            idleLeftAnimation.setTime(0);
            idleRightAnimation.setTime(0);
            swimmingLeftAnimation.setTime(0);
            swimmingRightAnimation.setTime(0);

            if (hero.isFacingLeft()) {
                animationHero.setAnimatedSprite(walkLeftAnimation);
            } else {
                animationHero.setAnimatedSprite(walkRightAnimation);
            }
        } else if (hero.getState().equals(Hero.State.JUMPING)) {
            dustParticles.allowCompletion();
            if (hero.isFacingLeft()) {
                animationHero.setAnimatedSprite(jumpLeftAnimation);
            } else {
                animationHero.setAnimatedSprite(jumpRightAnimation);
            }
        } else if (hero.getState().equals(Hero.State.FALL)) {
            dustParticles.allowCompletion();
            if (hero.isFacingLeft()) {
                animationHero.setAnimatedSprite(fallLeftAnimation);
            } else {
                animationHero.setAnimatedSprite(fallRightAnimation);
            }
        } else if (hero.getState().equals(Hero.State.SWIMMING)) {
            if (hero.isFacingLeft()) {
                animationHero.setAnimatedSprite(swimmingLeftAnimation);
            } else {
                animationHero.setAnimatedSprite(swimmingRightAnimation);
            }
        }

        animationHero.setPosition(hero.getBody().getPosition().x - animationHero.getWidth() / 2,
                hero.getBody().getPosition().y - animationHero.getHeight() / 2);
        if (hero.getState().equals(Hero.State.HURT)) {
            animationHero.setColor(new Color(1, 0.2f, 0.2f, 0.8f));
        } else {
            animationHero.setColor(Color.WHITE);
        }
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
}
