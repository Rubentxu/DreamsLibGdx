package com.rubentxu.juegos.core.vista;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Item;
import com.rubentxu.juegos.core.modelo.MovingPlatform;
import com.rubentxu.juegos.core.modelo.Water;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.pantallas.GameScreen;
import com.rubentxu.juegos.core.servicios.Assets;
import com.rubentxu.juegos.core.utils.debug.DebugWindow;
import com.rubentxu.juegos.core.utils.parallax.ParallaxBackground;
import com.rubentxu.juegos.core.utils.parallax.ParallaxLayer;


public class WorldRenderer {

    /**
     * for debug rendering *
     */
    Box2DDebugRenderer debugRenderer;
    private final GameScreen gameScreen;

    private World world;
    private OrthographicCamera cam;
    private OrthogonalTiledMapRenderer renderer;

    private SpriteBatch spriteBatch;
    private float width;
    private float height;

    private ModelsAndViews modelsAndViews;

    private ParallaxBackground background;



    public WorldRenderer(GameScreen gameScreen) {
        this.gameScreen= gameScreen;
        this.world = gameScreen.getWorld();

        modelsAndViews=new ModelsAndViews();

        debugRenderer = new Box2DDebugRenderer();
        renderer = new OrthogonalTiledMapRenderer(world.getMap(), world.getParser().getUnitScale());
        world.removeParser();
        spriteBatch = (SpriteBatch) renderer.getSpriteBatch();
        cam = new OrthographicCamera();
        loadTextures();
    }

    public void resize(int w, int h) {
        this.width=w;
        this.height=h;

        cam.viewportWidth = Constants.VIEWPORT_WIDTH;
        cam.viewportHeight = (Constants.VIEWPORT_WIDTH / width) * height;

    }

    private void loadTextures() {

        background=new ParallaxBackground(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
        background.addLayer(new ParallaxLayer(world.getLevelBackground(),0.4f,0,100,100));
        background.addLayer(new ParallaxLayer(world.getCloudBackground(),0.6f,0,100,100));
        background.addLayer(new ParallaxLayer(world.getTreeBackground(),0.8f,0.02f,100,100));

        TextureAtlas atlasVarios = Assets.getInstance().get(Assets.getInstance().VARIOS_ATLAS);

        modelsAndViews.addEntity(world.getHero());

        for(MovingPlatform mvp :world.getMovingPlatforms()){
            String nombreRegion= (atlasVarios.findRegion(mvp.getNombre())!=null)? mvp.getNombre(): mvp.getGrupo().toString();
            Sprite viewSprite = new Sprite(atlasVarios.findRegion(nombreRegion));
            if(viewSprite!=null){
                modelsAndViews.addModelAndView(mvp,viewSprite);
            }
        }

        for(Water w :world.getWaterSensors()){
            String nombreRegion= (atlasVarios.findRegion(w.getNombre())!=null)? w.getNombre(): w.getGrupo().toString();
            Sprite viewSprite = new Sprite(atlasVarios.findRegion(nombreRegion));
            if(viewSprite!=null){
                System.out.println("Creado Sprite "+w.getNombre());
                modelsAndViews.addModelAndView(w,viewSprite);
            }
        }

        for(Enemy e :world.getEnemies()){
            String nombreRegion= (atlasVarios.findRegion(e.getNombre())!=null)? e.getNombre(): e.getGrupo().toString();
            Sprite viewSprite = new Sprite(atlasVarios.findRegion(nombreRegion));
            if(viewSprite!=null){
                modelsAndViews.addModelAndView(e,viewSprite);
            }
        }

        for(Item i :world.getItems()){
            String nombreRegion= (atlasVarios.findRegion(i.getNombre())!=null)? i.getNombre(): i.getGrupo().toString();
            Sprite viewSprite = new Sprite(atlasVarios.findRegion("agua2"));
            if(viewSprite!=null){
                modelsAndViews.addModelAndView(i,viewSprite);
            }
        }
    }

    public void render() {

        background.render(cam.position,spriteBatch);

        world.getPhysics().step(Gdx.graphics.getDeltaTime(), Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
        cam.position.set(world.getHero().getBody().getPosition().x, cam.viewportHeight/2 -cam.viewportHeight/12, 0);
        cam.update();

        renderer.setView(cam);
        renderer.render();

        spriteBatch.begin();
        modelsAndViews.update(world);
        modelsAndViews.render(spriteBatch);

        if (DreamsGame.DEBUG) {
            DebugWindow.getInstance().setPosition(cam.position.x - 13f, cam.position.y - 5);
            DebugWindow.myLabel.setText("Modo Debug:\n\n"+world.getHero().toString());
            DebugWindow.getInstance().pack();
            DebugWindow.getInstance().draw(spriteBatch, 1f);

        }

        spriteBatch.end();

        if (DreamsGame.DEBUG) {
            debugRenderer.render(world.getPhysics(), cam.combined);

        }
    }


    public void dispose() {
        renderer.dispose();
    }

    public OrthographicCamera getCam() {
        return cam;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


}
