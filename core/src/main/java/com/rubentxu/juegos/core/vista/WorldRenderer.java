package com.rubentxu.juegos.core.vista;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.constantes.GameState;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.Item;
import com.rubentxu.juegos.core.modelo.MovingPlatform;
import com.rubentxu.juegos.core.modelo.Water;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.utils.debug.DebugWindow;
import com.rubentxu.juegos.core.utils.parallax.ParallaxBackground;
import com.rubentxu.juegos.core.utils.parallax.ParallaxLayer;


public class WorldRenderer implements Disposable {

    /**
     * for debug rendering *
     */
    Box2DDebugRenderer debugRenderer;
    private final DreamsGame game;

    private World world;
    private OrthographicCamera cam;
    private OrthogonalTiledMapRenderer renderer;

    private SpriteBatch spriteBatch;
    private float width;
    private float height;

    private ModelsAndViews modelsAndViews;
    private ParallaxBackground background;



    public WorldRenderer(DreamsGame game,World world) {
        this.game= game;
        this.world = world;

        modelsAndViews=new ModelsAndViews(game.getResourcesManager());

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

        background=new ParallaxBackground(cam.viewportWidth, cam.viewportHeight);
        background.addLayer(new ParallaxLayer(world.getBackground_01(),0.4f,0,100,100));
        background.addLayer(new ParallaxLayer(world.getBackground_03(),0.6f,0,100,100));
        background.addLayer(new ParallaxLayer(world.getBackground_02(), 0.8f, 0.02f, 100, 100));

    }

    private void loadTextures() {

        for(MovingPlatform mvp :world.getMovingPlatforms()){
            modelsAndViews.addEntity(mvp);
        }

        for(Water w :world.getWaterSensors()){
            modelsAndViews.addEntity(w);
        }

        for(Enemy e :world.getEnemies()){
            modelsAndViews.addEntity(e);
        }

        for(Item i :world.getItems()){
            modelsAndViews.addEntity(i);
        }
        modelsAndViews.addEntity(world.getHero());
    }

    public void render() {

        background.render(cam.position,spriteBatch);

        if(!DreamsGame.getGameState().equals(GameState.GAME_PAUSED))
            world.getPhysics().step(Gdx.graphics.getDeltaTime(), Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);

        cam.position.set(world.getHero().getBody().getPosition().x, cam.viewportHeight / 2 - cam.viewportHeight / 12, 0);
        cam.update();

        renderer.setView(cam);
        renderer.render();

        spriteBatch.begin();
        modelsAndViews.render(spriteBatch);

        if (DreamsGame.DEBUG) {
            DebugWindow.getInstance(game.getResourcesManager()).setPosition(cam.position.x - 13f, cam.position.y - 5);
            DebugWindow.myLabel.setText("Modo Debug:\n\n"+world.getHero().toString());
            DebugWindow.getInstance(game.getResourcesManager()).pack();
            DebugWindow.getInstance(game.getResourcesManager()).draw(spriteBatch, 1f);

        }

        spriteBatch.end();

        if (DreamsGame.DEBUG) {
            debugRenderer.render(world.getPhysics(), cam.combined);

        }
    }

    @Override
    public void dispose() {
        renderer.dispose();
        renderer=null;
        modelsAndViews=null;
        debugRenderer.dispose();
        debugRenderer=null;
        cam=null;
        spriteBatch=null;
        background=null;
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
