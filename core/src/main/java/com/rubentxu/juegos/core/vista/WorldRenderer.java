package com.rubentxu.juegos.core.vista;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.modelo.Rubentxu;
import com.rubentxu.juegos.core.modelo.Rubentxu.State;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.utils.dermetfan.graphics.AnimatedBox2DSprite;
import com.rubentxu.juegos.core.utils.dermetfan.graphics.AnimatedSprite;


public class WorldRenderer {


    private static final float RUNNING_FRAME_DURATION = 0.02f;
    private World world;
    private OrthographicCamera cam;
    private OrthogonalTiledMapRenderer renderer;

    /**
     * for debug rendering *
     */
    Box2DDebugRenderer debugRenderer;

    /**
     * Textures *
     */
    private Array<TextureAtlas.AtlasRegion> rubenJumpRight;
    private Array<TextureAtlas.AtlasRegion> rubenFallRight;
    private Array<TextureAtlas.AtlasRegion> rubenIdleRight;

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

    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private int width;
    private int height;
    private float timeIdle;
    private Rubentxu ruben;
    private float framesJump;
    private float framesFall;
    Vector3 point = new Vector3();


    public void setSize(int w, int h) {
        this.width = w;
        this.height = h;
        cam.viewportWidth = width / 25;
        cam.viewportHeight = height / 25;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public WorldRenderer(final World world, boolean debug) {
        this.world = world;
        ruben = world.getRuben();
        debugRenderer = new Box2DDebugRenderer();
        renderer = new OrthogonalTiledMapRenderer(world.getMap(), world.getParser().getUnitScale());
        spriteBatch = renderer.getSpriteBatch();
        cam = new OrthographicCamera();
        loadTextures();

        final int tileWidth = world.getMap().getProperties().get("tilewidth", Integer.class), tileHeight = world.getMap().getProperties().get("tileheight", Integer.class);

    }

    private void loadTextures() {

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("imagenes/texturas/sprites.pack"));
        Array<TextureAtlas.AtlasRegion> rubenRight = atlas.findRegions("Andando");
        rubenJumpRight = atlas.findRegions("Saltando");
        rubenFallRight = atlas.findRegions("Cayendo");
        rubenIdleRight = atlas.findRegions("Parado");

        Animation walkRight = new Animation(RUNNING_FRAME_DURATION, rubenRight);
        walkRight.setPlayMode(Animation.LOOP);
        walkRightAnimation = new AnimatedSprite(walkRight);

        Animation jumpRight = new Animation(RUNNING_FRAME_DURATION * 7, rubenJumpRight);
        jumpRightAnimation = new AnimatedSprite(jumpRight);
        Animation fallRight = new Animation(RUNNING_FRAME_DURATION * 5, rubenFallRight);
        fallRightAnimation = new AnimatedSprite(fallRight);

        Animation idleRight = new Animation(RUNNING_FRAME_DURATION * 4, rubenIdleRight);
        idleRight.setPlayMode(Animation.LOOP);
        idleRightAnimation = new AnimatedSprite(idleRight);

        walkLeftAnimation = convertToLeft(rubenRight, 1);
        walkLeftAnimation.getAnimation().setPlayMode(Animation.LOOP);
        jumpLeftAnimation = convertToLeft(rubenJumpRight, 7);
        fallLeftAnimation = convertToLeft(rubenFallRight, 5);
        idleLeftAnimation = convertToLeft(rubenIdleRight, 4);
        idleLeftAnimation.getAnimation().setPlayMode(Animation.LOOP);

        AnimationRuben = new AnimatedBox2DSprite(walkRightAnimation);
        AnimationRuben.setSize(2.65f, 4f);
        AnimationRuben.setOrigin(AnimationRuben.getWidth() / 2, AnimationRuben.getHeight() / 1.9f);
        AnimationRuben.setPosition(ruben.getBody().getPosition().x - AnimationRuben.getWidth() / 2,
                ruben.getBody().getPosition().y - AnimationRuben.getHeight() / 1.9f);

        world.getFont().setScale(1 / 16F);
        world.getFont().setUseIntegerPositions(false);


    }

    public AnimatedSprite convertToLeft(Array<TextureAtlas.AtlasRegion> atlasRegions, int mul) {
        Array<TextureAtlas.AtlasRegion> atlasRegionsTmp = new Array<TextureAtlas.AtlasRegion>();
        for (TextureAtlas.AtlasRegion r : atlasRegions) {
            TextureAtlas.AtlasRegion r2 = new TextureAtlas.AtlasRegion(r);
            r2.flip(true, false);
            atlasRegionsTmp.add(r2);
        }
        return new AnimatedSprite(new Animation(RUNNING_FRAME_DURATION * mul, atlasRegionsTmp));

    }

    public void render() {

        TiledMapTileLayer mtl = (TiledMapTileLayer) world.getMap().getLayers().get(0);
        drawRubentxu();

        world.getPhysics().step(Gdx.graphics.getDeltaTime(), 4, 4);

        cam.position.set(world.getRuben().getBody().getPosition().x, cam.viewportHeight / 2, 0);
        cam.update();

        renderer.setView(cam);
        renderer.render();

        spriteBatch.begin();
        AnimationRuben.update();
        AnimationRuben.draw(spriteBatch);
        if (DreamsGame.DEBUG) {
            world.getFont().drawMultiLine(spriteBatch, "Friccion del cuerpo: " + ruben.getRubenSensorFixture().getFriction()
                    + "\nIsGround: " + ruben.isGround(), cam.position.x - 9.5f,
                    cam.position.y + 5.5f);
        }
        spriteBatch.end();

        if (DreamsGame.DEBUG) {
            debugRenderer.render(world.getPhysics(), cam.combined);
        }
        spriteBatch.setProjectionMatrix(cam.combined);

    }

    private void drawRubentxu() {

        if (ruben.isFacingLeft()) {
            AnimationRuben.setAnimatedSprite(walkLeftAnimation);
        } else {
            AnimationRuben.setAnimatedSprite(walkRightAnimation);
        }
        if (ruben.getState().equals(State.IDLE)) {
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

        if (ruben.getState().equals(State.WALKING)) {
            timeIdle = 0;
            AnimationRuben.play();
            jumpRightAnimation.setTime(0);
            fallRightAnimation.setTime(0);
            jumpLeftAnimation.setTime(0);
            fallLeftAnimation.setTime(0);
            idleLeftAnimation.setTime(0);
            idleRightAnimation.setTime(0);

            if (ruben.isFacingLeft()) {
                AnimationRuben.setAnimatedSprite(walkLeftAnimation);
            } else {
                AnimationRuben.setAnimatedSprite(walkRightAnimation);
            }
        } else if (ruben.getState().equals(State.JUMPING)) {
            if (ruben.getVelocity().y > 0) {
                if (ruben.isFacingLeft()) {
                    AnimationRuben.setAnimatedSprite(jumpLeftAnimation);
                } else {
                    AnimationRuben.setAnimatedSprite(jumpRightAnimation);
                }

            } else {

                if (ruben.isFacingLeft()) {
                    AnimationRuben.setAnimatedSprite(fallLeftAnimation);
                } else {
                    AnimationRuben.setAnimatedSprite(fallRightAnimation);
                }
            }
        } else if (ruben.getState().equals(State.FALL)) {
            if (ruben.isFacingLeft()) {
                AnimationRuben.setAnimatedSprite(fallLeftAnimation);
            } else {
                AnimationRuben.setAnimatedSprite(fallRightAnimation);
            }
        }

        AnimationRuben.setPosition(ruben.getBody().getPosition().x - AnimationRuben.getWidth() / 2,
                ruben.getBody().getPosition().y - AnimationRuben.getHeight() / 1.9f);

    }


    public void dispose() {
        renderer.dispose();
    }

    public OrthographicCamera getCam() {
        return cam;
    }

}
