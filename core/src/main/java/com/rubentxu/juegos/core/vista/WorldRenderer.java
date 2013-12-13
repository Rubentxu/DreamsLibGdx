package com.rubentxu.juegos.core.vista;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.modelo.MovingPlatform;
import com.rubentxu.juegos.core.modelo.Rubentxu;
import com.rubentxu.juegos.core.modelo.Rubentxu.State;
import com.rubentxu.juegos.core.modelo.Water;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.utils.debug.DebugWindow;
import com.rubentxu.juegos.core.utils.dermetfan.graphics.AnimatedBox2DSprite;
import com.rubentxu.juegos.core.utils.dermetfan.graphics.AnimatedSprite;


public class WorldRenderer {


    private static final float RUNNING_FRAME_DURATION = 0.02f;
    private final MovingPlatform mvp;

    /**
     * for debug rendering *
     */
    Box2DDebugRenderer debugRenderer;
    private World world;
    private OrthographicCamera cam;
    private OrthogonalTiledMapRenderer renderer;
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
    private SpriteBatch spriteBatch;
    private int width;
    private int height;
    private float timeIdle;
    private Rubentxu ruben;
    private Sprite m1,m2,w;
    private ModelsAndViews modelsAndViews;





    public WorldRenderer(final World world, boolean debug) {
        modelsAndViews=new ModelsAndViews();
        this.world = world;
        ruben = world.getRuben();
        debugRenderer = new Box2DDebugRenderer();
        renderer = new OrthogonalTiledMapRenderer(world.getMap(), world.getParser().getUnitScale());
        spriteBatch = renderer.getSpriteBatch();
        cam = new OrthographicCamera();
        loadTextures();
        mvp=(MovingPlatform)world.getMovingPlatformplatforms().toArray()[0];

        //final int tileWidth = world.getMap().getProperties().get("tilewidth", Integer.class), tileHeight = world.getMap().getProperties().get("tileheight", Integer.class);

    }

    public void setSize(int w, int h) {
        this.setWidth(w);
        this.setHeight(h);
        cam.viewportWidth = getWidth() / 42;
        cam.viewportHeight = getHeight() / 42;
    }

    private void loadTextures() {

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("imagenes/texturas/sprites.pack"));
        TextureAtlas atlasVarios = new TextureAtlas(Gdx.files.internal("imagenes/texturas/varios2.pack"));

        for(MovingPlatform mvp :world.getMovingPlatformplatforms()){
            Sprite viewSprite = new Sprite(atlasVarios.findRegion(mvp.getNombre()));
            if(viewSprite!=null){
                modelsAndViews.addModelAndView(mvp,viewSprite);
            }
        }
        for(Water w :world.getWaterSensors()){
            Sprite viewSprite = new Sprite(atlasVarios.findRegion(w.getNombre()));
            if(viewSprite!=null){
                System.out.println("Creado Sprite "+w.getNombre());
                modelsAndViews.addModelAndView(w,viewSprite);
            }
        }

        Array<TextureAtlas.AtlasRegion> rubenRight = atlas.findRegions("Andando");
        rubenJumpRight = atlas.findRegions("Saltando");
        rubenFallRight = atlas.findRegions("Cayendo");
        rubenIdleRight = atlas.findRegions("Parado");
        rubenSwimmingRight = atlasVarios.findRegions("nadando");

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

        Animation swimmingRight = new Animation(RUNNING_FRAME_DURATION * 4, rubenSwimmingRight);
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
        return new AnimatedSprite(new Animation(RUNNING_FRAME_DURATION * mul, atlasRegionsTmp));

    }

    public void render() {

        TiledMapTileLayer mtl = (TiledMapTileLayer) world.getMap().getLayers().get(0);
        drawRubentxu();

        world.getPhysics().step(Gdx.graphics.getDeltaTime(), 4, 4);
        ruben.getBody().setAwake(true);
        cam.position.set(world.getRuben().getBody().getPosition().x, world.getRuben().getBody().getPosition().y / 1.3f, 0);
        cam.update();
        spriteBatch.begin();
        world.getBackground().draw(spriteBatch);
        spriteBatch.end();
        renderer.setView(cam);
        renderer.render();

        spriteBatch.begin();
        modelsAndViews.render(spriteBatch);
        AnimationRuben.update();
        AnimationRuben.draw(spriteBatch);

        if (DreamsGame.DEBUG) {
            DebugWindow.getInstance().setPosition(cam.position.x - 11.5f, cam.position.y - 2);
            DebugWindow.myLabel.setText(mvp.toString());
            DebugWindow.getInstance().pack();
            DebugWindow.getInstance().draw(spriteBatch, 0.8f);

        }
        spriteBatch.end();
        if (DreamsGame.DEBUG) {
            debugRenderer.render(world.getPhysics(), cam.combined);
        }

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
            swimmingLeftAnimation.setTime(0);
            swimmingRightAnimation.setTime(0);

            if (ruben.isFacingLeft()) {
                AnimationRuben.setAnimatedSprite(walkLeftAnimation);
            } else {
                AnimationRuben.setAnimatedSprite(walkRightAnimation);
            }
        } else if (ruben.getState().equals(State.JUMPING)) {

            if (ruben.isFacingLeft()) {
                AnimationRuben.setAnimatedSprite(jumpLeftAnimation);
            } else {
                AnimationRuben.setAnimatedSprite(jumpRightAnimation);
            }
        } else if (ruben.getState().equals(State.FALL)) {
            if (ruben.isFacingLeft()) {
                AnimationRuben.setAnimatedSprite(fallLeftAnimation);
            } else {
                AnimationRuben.setAnimatedSprite(fallRightAnimation);
            }
        }   else if (ruben.getState().equals(State.SWIMMING)) {
            if (ruben.isFacingLeft()) {
                AnimationRuben.setAnimatedSprite(swimmingLeftAnimation);
            } else {
                AnimationRuben.setAnimatedSprite(swimmingRightAnimation);
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
