package com.rubentxu.juegos.core.vista;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.modelo.Rubentxu;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.modelo.Rubentxu.State;
import net.dermetfan.libgdx.box2d.Box2DUtils;
import net.dermetfan.libgdx.graphics.AnimatedBox2DSprite;
import net.dermetfan.libgdx.graphics.AnimatedSprite;
import net.dermetfan.libgdx.graphics.Box2DSprite;

import java.util.ArrayList;


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
    private TextureRegion frameRuben;
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
    public static final float PIXEL_METER= 64 ;
    public static final float METER_PIXEL= 0.015625f ;
    private AnimatedBox2DSprite AnimationRuben;
    private AnimatedSprite walkLeftAnimation;

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



        rubenJumpRight = atlas.findRegions("Saltando");

        rubenFallRight = atlas.findRegions("SaltandoDown");
        framesFall=rubenFallRight.size;
        framesJump=rubenJumpRight.size;

        Animation walkRight = new Animation(RUNNING_FRAME_DURATION, rubenRight);
        walkRight.setPlayMode(Animation.LOOP);
        walkRightAnimation = new AnimatedSprite(walkRight);


        Array<TextureAtlas.AtlasRegion>  rubenLeft = new Array<TextureAtlas.AtlasRegion>();
        for (TextureAtlas.AtlasRegion r: rubenRight)      {

            TextureAtlas.AtlasRegion r2 = new TextureAtlas.AtlasRegion(r);
            r2.flip(true,false);
            rubenLeft.add(r2);
        }

        Animation walkLeft = new Animation(RUNNING_FRAME_DURATION, rubenLeft);
        walkLeft.setPlayMode(Animation.LOOP);
        walkLeftAnimation= new AnimatedSprite(walkLeft) ;

        AnimationRuben=new AnimatedBox2DSprite(walkRightAnimation);
        AnimationRuben.setSize(4,4f);
        AnimationRuben.setOrigin(AnimationRuben.getWidth()/2,AnimationRuben.getHeight()/1.9f);
        AnimationRuben.setPosition(ruben.getBody().getPosition().x-AnimationRuben.getWidth()/2,
                ruben.getBody().getPosition().y-AnimationRuben.getHeight()/1.9f);


        Animation jumpRight = new Animation(RUNNING_FRAME_DURATION*5, rubenJumpRight);
        jumpRight.setPlayMode(Animation.LOOP);
        jumpRightAnimation = new AnimatedSprite(jumpRight);


        Animation fallRight = new Animation(RUNNING_FRAME_DURATION*5, rubenFallRight);
        fallRight.setPlayMode(Animation.LOOP);
        fallRightAnimation = new AnimatedSprite(fallRight);



    }


    public void render() {

        TiledMapTileLayer mtl= (TiledMapTileLayer) world.getMap().getLayers().get(0);
        drawRubentxu();
        spriteBatch.setProjectionMatrix(cam.combined);


        world.getPhysics().step(Gdx.graphics.getDeltaTime(), 4, 4);

        cam.position.set( world.getRuben().getBody().getPosition().x, cam.viewportHeight/2, 0);
        cam.update();

        renderer.setView(cam);
        renderer.render();

        spriteBatch.begin();

//        world.getFont().drawMultiLine(spriteBatch, "friction: " + world.getRuben().getRubenPhysicsFixture().getFriction() + "\ngrounded: "
//                + ruben.isGrounded(), ruben.getBody().getPosition().x+20, ruben.getBody().getPosition().y);

        //Box2DSprite.draw(spriteBatch, world.getPhysics());


        world.getFont().drawMultiLine(spriteBatch, "RegionWidth: " + AnimationRuben.isPlaying()+ "\ngrounded: "
                + Float.toString( AnimationRuben.getTime()), ruben.getBody().getPosition().x+20, ruben.getBody().getPosition().y);

        AnimationRuben.update();
        AnimationRuben.draw(spriteBatch);







        spriteBatch.end();


        debugRenderer.render(world.getPhysics(), cam.combined);
        /*spriteBatch.begin();
        drawBlocks();

        spriteBatch.end();
       // drawCollisionBlocks();
       // renderer.render(foreground);*/


    }




    private void drawRubentxu() {

        if(ruben.isFacingLeft() ) {
            System.out.println("A la izquierda");
            AnimationRuben.setAnimatedSprite(walkLeftAnimation);
        }else  {
            System.out.println("A la Derecha");
            AnimationRuben.setAnimatedSprite(walkRightAnimation);
        }
        if(ruben.getState().equals(State.IDLE)) {
            System.out.println("Parado: ");
            AnimationRuben.stop();


        } else {
            System.out.println("Andando: ");
            AnimationRuben.play();
        }


        if(ruben.getState().equals(State.WALKING)) {
            timeJump=0;
            timefall=0;



            if(ruben.isFacingLeft() ) {
                System.out.println("A la izquierda");
                AnimationRuben.setAnimatedSprite(walkLeftAnimation);
            }else   {
                System.out.println("A la Derecha");
                AnimationRuben.setAnimatedSprite(walkRightAnimation);
            }
        } else if (ruben.getState().equals(State.JUMPING)) {
            if (ruben.getVelocity().y > 0) {

                if(framesJump>=timeJump){
                    timeJump=ruben.getStateTime()%framesJump;
                }

                AnimationRuben.setAnimatedSprite( jumpRightAnimation);



            } else {

                if(framesFall>=timefall){
                    timefall=ruben.getStateTime()%framesFall;
                }


                AnimationRuben.setAnimatedSprite( fallRightAnimation);

            }
        } else  {
            timeJump=0;
            timefall=0;

        }

        AnimationRuben.setPosition(ruben.getBody().getPosition().x-AnimationRuben.getWidth()/2,
                ruben.getBody().getPosition().y-AnimationRuben.getHeight()/1.9f);

    }


    public void dispose() {
        renderer.dispose();
    }

    public OrthographicCamera getCam() {
        return cam;
    }

}
