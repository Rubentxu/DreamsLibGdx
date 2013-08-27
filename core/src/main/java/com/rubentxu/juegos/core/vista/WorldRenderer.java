package com.rubentxu.juegos.core.vista;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.modelo.Block;
import com.rubentxu.juegos.core.modelo.Rubentxu;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.modelo.Rubentxu.State;


public class WorldRenderer {

    private static final float CAMERA_WIDTH = 10f;
    private static final float CAMERA_HEIGHT = 7f;
    private static final float RUNNING_FRAME_DURATION = 0.02f;

    private World world;
    private OrthographicCamera cam;

    /** for debug rendering **/
    ShapeRenderer debugRenderer = new ShapeRenderer();

    /** Textures **/
    private Array<TextureAtlas.AtlasRegion> rubenIdleLeft;
    private Array<TextureAtlas.AtlasRegion> rubenIdleRight;

    private TextureRegion blockTexture;
    private TextureRegion rubenFrame;
    private Array<TextureAtlas.AtlasRegion> rubenJumpRight;
    private Array<TextureAtlas.AtlasRegion> rubenFallRight;

    /** Animations **/

    private Animation walkRightAnimation;
    private Animation jumpRightAnimation;
    private Animation fallRightAnimation;
    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private int width;
    private int height;
    private float ppuX;	// pixels per unit on the X axis
    private float ppuY;	// pixels per unit on the Y axis
    private float timeJump;
    private float timefall;
    private float framesJump;
    private float framesFall;


    public void setSize (int w, int h) {
        this.width = w;
        this.height = h;
        ppuX = (float)width / CAMERA_WIDTH;
        ppuY = (float)height / CAMERA_HEIGHT;
    }
    public boolean isDebug() {
        return debug;
    }
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public WorldRenderer(World world, boolean debug) {
        this.world = world;
        this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        this.cam.update();
        this.timeJump=0;
        this.timefall=0;
        this.debug = debug;
        spriteBatch = new SpriteBatch();
        loadTextures();
    }

    private void loadTextures() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("imagenes/texturas/sprites.pack"));
        rubenIdleRight=  atlas.findRegions("Andando");
        blockTexture = atlas.findRegion("block");
        rubenJumpRight = atlas.findRegions("Saltando");
        rubenFallRight = atlas.findRegions("SaltandoDown");
        framesFall=rubenFallRight.size;
        framesJump=rubenJumpRight.size;
        walkRightAnimation = new Animation(RUNNING_FRAME_DURATION, rubenIdleRight);
        walkRightAnimation.setPlayMode(Animation.LOOP);
        jumpRightAnimation = new Animation(RUNNING_FRAME_DURATION*5, rubenJumpRight);
        //jumpRightAnimation.setPlayMode(Animation.LOOP);
        fallRightAnimation = new Animation(RUNNING_FRAME_DURATION*5, rubenFallRight);
        //fallRightAnimation.setPlayMode(Animation.LOOP);


    }


    public void render() {
        this.cam.position.set(world.getRubentxu().getPosition().x,CAMERA_HEIGHT/1.7F, 0);
        this.cam.update();
        spriteBatch.setProjectionMatrix(cam.combined);
        spriteBatch.begin();
        drawBlocks();
        drawRubentxu();
        spriteBatch.end();
        drawCollisionBlocks();
        if (debug)
            drawDebug();
    }


    private void drawBlocks() {
        for (Block block : world.getDrawableBlocks((int)CAMERA_WIDTH, (int)CAMERA_HEIGHT)) {
            // spriteBatch.draw(blockTexture, block.getPosition().x * ppuX, block.getPosition().y * ppuY, Block.SIZE * ppuX, Block.SIZE * ppuY);
            spriteBatch.draw(blockTexture, block.getPosition().x, block.getPosition().y, Block.SIZE, Block.SIZE);
        }
    }

    private void drawRubentxu() {
        Rubentxu ruben = world.getRubentxu();

        rubenFrame =  rubenIdleRight.get(0);
        if(ruben.getState().equals(State.WALKING)) {
            timeJump=0;
            timefall=0;

            rubenFrame = walkRightAnimation.getKeyFrame(ruben.getStateTime(), true);
        } else if (ruben.getState().equals(State.JUMPING)) {
            if (ruben.getVelocity().y > 0) {
                System.out.println(timeJump);
                if(framesJump>=timeJump){
                    timeJump=ruben.getStateTime()%framesJump;
                    System.out.println("lo flipo"+framesJump);
                }
                System.out.println(timeJump);
                    rubenFrame =jumpRightAnimation.getKeyFrame(timeJump,true);


            } else {

                if(framesFall>=timefall){
                    timefall=ruben.getStateTime()%framesFall;
                }
                System.out.println(timefall);
                rubenFrame =fallRightAnimation.getKeyFrame(timefall,true);

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


        // spriteBatch.draw(rubenFrame, ruben.getPosition().x * ppuX, ruben.getPosition().y * ppuY, ruben.SIZE * ppuX, ruben.SIZE * ppuY);
        spriteBatch.draw(rubenFrame, ruben.getPosition().x-0.25f, ruben.getPosition().y, Rubentxu.SIZE, Rubentxu.SIZE);
    }

    private void drawDebug() {

        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Line);
        for (Block block : world.getDrawableBlocks((int)CAMERA_WIDTH, (int)CAMERA_HEIGHT)) {
            Rectangle rect = block.getBounds();
            debugRenderer.setColor(new Color(1, 0, 0, 1));
            debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }

        Rubentxu ruben = world.getRubentxu();
        Rectangle rect = ruben.getBounds();
        debugRenderer.setColor(new Color(0, 1, 0, 1));
        debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        debugRenderer.end();
    }

    private void drawCollisionBlocks() {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Filled);
        debugRenderer.setColor(Color.WHITE);
        for (Rectangle rect : world.getCollisionRects()) {
            debugRenderer.rect(rect.x, rect.y, rect.width, rect.height, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        }
        debugRenderer.end();

    }
}
