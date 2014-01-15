package com.rubentxu.juegos.core.vista;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.MovingPlatform;
import com.rubentxu.juegos.core.modelo.Water;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.servicios.Assets;
import com.rubentxu.juegos.core.utils.debug.DebugWindow;
import com.rubentxu.juegos.core.utils.parallax.ParallaxBackground;
import com.rubentxu.juegos.core.utils.parallax.ParallaxLayer;


public class WorldRenderer {

    /**
     * for debug rendering *
     */
    Box2DDebugRenderer debugRenderer;
    private World world;
    private OrthographicCamera cam;
    private OrthogonalTiledMapRenderer renderer;

    private SpriteBatch spriteBatch;
    private float width;
    private float height;

    private ModelsAndViews modelsAndViews;
    private Stage stage;
    private ParallaxBackground background;


    public WorldRenderer(final World world,Stage stage,boolean debug) {
        modelsAndViews=new ModelsAndViews(stage);

        this.world = world;
        debugRenderer = new Box2DDebugRenderer();
        renderer = new OrthogonalTiledMapRenderer(world.getMap(), world.getParser().getUnitScale());
        world.removeParser();
        spriteBatch = (SpriteBatch) renderer.getSpriteBatch();
        cam = new OrthographicCamera();
        loadTextures();

    }

    public void setSize(int w, int h) {
        this.setWidth(w);
        this.setHeight(h);
        stage.clear();
        cam.viewportWidth = Constants.VIEWPORT_WIDTH;
        cam.viewportHeight = (Constants.VIEWPORT_WIDTH / width) * height;

        stage.setViewport(getWidth() ,getHeight() );
    }

    private void loadTextures() {

        background=new ParallaxBackground(new ParallaxLayer[]{new ParallaxLayer(world.getBackground(),new Vector2(0.5f,0.2f),new Vector2(0, 500))
        }, 800, 480);

        TextureAtlas atlasVarios = Assets.getInstance().get(Assets.getInstance().VARIOS_ATLAS);

        modelsAndViews.addModelAndBuildView(world.getHero());

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
    }

    public void render() {

        background.render(world.getHero().getVelocity().cpy());

        //TiledMapTileLayer mtl = (TiledMapTileLayer) world.getMap().getLayers().get(0);

        world.getPhysics().step(Gdx.graphics.getDeltaTime(), Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
        cam.position.set(world.getHero().getBody().getPosition().x, world.getHero().getBody().getPosition().y , 0);
        cam.update();

        renderer.setView(cam);
        renderer.render();

        spriteBatch.begin();
        modelsAndViews.render(spriteBatch);


        if (DreamsGame.DEBUG) {
            DebugWindow.getInstance().setPosition(cam.position.x - 11.5f, cam.position.y - 2);
            DebugWindow.myLabel.setText("Modo Debug:\n\n"+world.getHero().toString());
            DebugWindow.getInstance().pack();
            DebugWindow.getInstance().draw(spriteBatch, 1f);

        }

        spriteBatch.end();
        stage.draw();
        Table.drawDebug(stage);
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }


}
