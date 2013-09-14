package com.rubentxu.juegos.core.vista;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.modelo.Rubentxu;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.modelo.Rubentxu.State;
import net.dermetfan.libgdx.graphics.AnimatedSprite;
import net.dermetfan.libgdx.graphics.Box2DSprite;


public class WorldRenderer {

    private static final float cam_WIDTH = 10f;
    private static final float cam_HEIGHT = 7f;
    private static final float RUNNING_FRAME_DURATION = 0.02f;


    private World world;
    private OrthographicCamera cam;
    private OrthogonalTiledMapRenderer renderer;


    /** for debug rendering **/
    Box2DDebugRenderer debugRenderer ;

    /** Textures **/
    private Array<TextureAtlas.AtlasRegion> rubenIdleLeft;
    private Array<TextureAtlas.AtlasRegion> rubenRight;

    private TextureRegion blockTexture;
    private TextureRegion rubenFrame;
    private Array<TextureAtlas.AtlasRegion> rubenJumpRight;
    private Array<TextureAtlas.AtlasRegion> rubenFallRight;

    /** Animations **/


    private AnimatedSprite walkRightAnimation;
    private AnimatedSprite jumpRightAnimation;
    private AnimatedSprite fallRightAnimation;
    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private int width;
    private int height;     
    private float timeJump;
    private float timefall;
    private float framesJump;
    private float framesFall;
    private Rubentxu ruben;


    public void setSize (int w, int h) {
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
        System.out.println("Cantidad de capas:: "+world.getMap().getLayers().getCount());
        ruben = world.getRuben();
        debugRenderer = new Box2DDebugRenderer();
        renderer = new OrthogonalTiledMapRenderer(world.getMap(), world.getParser().getUnitScale());
        spriteBatch = renderer.getSpriteBatch();
        cam = new OrthographicCamera();
        loadTextures();

        final int tileWidth =  world.getMap().getProperties().get("tilewidth", Integer.class), tileHeight =  world.getMap().getProperties().get("tileheight", Integer.class);

    }


    private void loadTextures() {

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("imagenes/texturas/sprites.pack"));
        rubenRight=  atlas.findRegions("Andando");
        blockTexture = atlas.findRegion("block");
        rubenJumpRight = atlas.findRegions("Saltando");
        rubenFallRight = atlas.findRegions("SaltandoDown");
        framesFall=rubenFallRight.size;
        framesJump=rubenJumpRight.size;

        Animation walkRight = new Animation(RUNNING_FRAME_DURATION, rubenRight);
        walkRight.setPlayMode(Animation.LOOP);
        walkRightAnimation = new AnimatedSprite(walkRight);
        walkRightAnimation.setCenterFrames(true);
        walkRightAnimation.setSize(rubenRight.peek().getRegionWidth(), rubenRight.peek().getRegionHeight());
        walkRightAnimation.setOrigin(walkRightAnimation.getWidth() / 2, walkRightAnimation.getHeight() / 2);
        walkRightAnimation.setPosition(Gdx.graphics.getWidth() - walkRightAnimation.getWidth(), 0);

        Animation jumpRight = new Animation(RUNNING_FRAME_DURATION*5, rubenJumpRight);
        jumpRight.setPlayMode(Animation.LOOP);
        jumpRightAnimation = new AnimatedSprite(jumpRight);
        jumpRightAnimation.setCenterFrames(true);
        jumpRightAnimation.setSize(rubenJumpRight.peek().getRegionWidth(), rubenJumpRight.peek().getRegionHeight());
        jumpRightAnimation.setOrigin(jumpRightAnimation.getWidth() / 2, jumpRightAnimation.getHeight() / 2);
        jumpRightAnimation.setPosition(Gdx.graphics.getWidth() - jumpRightAnimation.getWidth(), 0);

        Animation fallRight = new Animation(RUNNING_FRAME_DURATION*5, rubenFallRight);
        fallRight.setPlayMode(Animation.LOOP);
        fallRightAnimation = new AnimatedSprite(fallRight);
        fallRightAnimation.setCenterFrames(true);
        fallRightAnimation.setSize(rubenFallRight.peek().getRegionWidth(), rubenFallRight.peek().getRegionHeight());
        fallRightAnimation.setOrigin(fallRightAnimation.getWidth() / 2, fallRightAnimation.getHeight() / 2);
        fallRightAnimation.setPosition(Gdx.graphics.getWidth() - fallRightAnimation.getWidth(), 0);


    }


    public void render() {

        TiledMapTileLayer mtl= (TiledMapTileLayer) world.getMap().getLayers().get(0);
        drawRubentxu();
        spriteBatch.setProjectionMatrix(cam.combined);

        renderer.setView(getCam());
        renderer.render();
        world.getPhysics().step(Gdx.graphics.getDeltaTime(), 4, 4);

        cam.position.set( world.getRuben().getBody().getPosition().x, cam.viewportHeight/2, 0);
        cam.update();

        renderer.setView(cam);
        renderer.render();

        spriteBatch.begin();

        world.getFont().drawMultiLine(spriteBatch, "friction: " + world.getRuben().getRubenPhysicsFixture().getFriction() + "\ngrounded: "
                + ruben.isGrounded(), ruben.getBody().getPosition().x+20, ruben.getBody().getPosition().y);

        Box2DSprite.draw(spriteBatch, world.getPhysics());

        spriteBatch.draw(rubenFrame, ruben.getBody().getPosition().x , ruben.getBody().getPosition().y , ruben.getWidth()/2, ruben.getHeight()/2);
        spriteBatch.end();


        debugRenderer.render(world.getPhysics(), cam.combined);
        /*spriteBatch.begin();
        drawBlocks();

        spriteBatch.end();
       // drawCollisionBlocks();
       // renderer.render(foreground);*/


    }




    private void drawRubentxu() {


        rubenFrame =  rubenRight.get(0);
        if(ruben.getState().equals(State.WALKING)) {
            timeJump=0;
            timefall=0;

            rubenFrame = walkRightAnimation.getAnimation().getKeyFrame(ruben.getStateTime(), true);
        } else if (ruben.getState().equals(State.JUMPING)) {
            if (ruben.getVelocity().y > 0) {
                System.out.println(timeJump);
                if(framesJump>=timeJump){
                    timeJump=ruben.getStateTime()%framesJump;
                    System.out.println("lo flipo"+framesJump);
                }
                System.out.println(timeJump);
                    rubenFrame =jumpRightAnimation.getAnimation().getKeyFrame(timeJump,true);


            } else {

                if(framesFall>=timefall){
                    timefall=ruben.getStateTime()%framesFall;
                }
                System.out.println(timefall);
                rubenFrame =fallRightAnimation.getAnimation().getKeyFrame(timefall,true);

            }
        } else  {
            timeJump=0;
            timefall=0;

        }
        if(ruben.isFacingLeft() && !rubenFrame.isFlipX()) {
           rubenFrame.flip(true,false);
        }else if (!ruben.isFacingLeft() && rubenFrame.isFlipX()) {
            rubenFrame.flip(true,false);
        }

    }


    public void dispose() {
        renderer.dispose();
    }

    public OrthographicCamera getCam() {
        return cam;
    }

}
