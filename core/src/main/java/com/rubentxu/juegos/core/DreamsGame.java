package com.rubentxu.juegos.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.inputs.GameInputs;
import com.rubentxu.juegos.core.managers.EnemyManager;
import com.rubentxu.juegos.core.managers.PlatformManager;
import com.rubentxu.juegos.core.managers.RubentxuManager;
import com.rubentxu.juegos.core.managers.WaterManager;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.pantallas.GameScreen;
import com.rubentxu.juegos.core.pantallas.MenuScreen;
import com.rubentxu.juegos.core.pantallas.SplashScreen;
import com.rubentxu.juegos.core.servicios.Assets;
import com.rubentxu.juegos.core.vista.WorldRenderer;

public class DreamsGame extends Game {

    public static final String VERSION = "0.1 Pre-Alpha";
    public static final String LOG = "Rubentxu Dreams";
    public static boolean DEBUG = false;
    FPSLogger log;
    private World world;
    private WorldRenderer renderer;
    private WorldController controller;
    public GameScreen gameScreen;
    public MenuScreen menuScreen;
    private GameInputs gameInputs;
    public Assets assets;


    @Override
	public void create () {

        log = new FPSLogger();
        assets=new Assets();
        world = new World(this,assets);
        renderer=new WorldRenderer(world, true);

        RubentxuManager rubenManager = new RubentxuManager(world.getRuben());
        PlatformManager platformManager = new PlatformManager();
        platformManager.setMovingPlatforms(world.getMovingPlatformplatforms());
        platformManager.setPlatforms(world.getPlatforms());

        WaterManager waterManager= new WaterManager();
        waterManager.setWaterSensors(world.getWaterSensors());

        EnemyManager enemyManager= new EnemyManager();
        enemyManager.setEnemies(world.getEnemies());

        controller= new WorldController();
        controller.setRubenManager(rubenManager);
        controller.setPlatformManager(platformManager);
        controller.setWaterManager(waterManager);
        controller.setEnemyManager(enemyManager);
        world.getPhysics().setContactListener(controller);

        gameInputs = new GameInputs(world, controller, renderer);
        Stage stage= new Stage(0, 0, true);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(gameInputs);
        Gdx.input.setInputProcessor(multiplexer);
        renderer.setStage(stage);

        gameScreen= new GameScreen( this,world,controller,renderer);



        menuScreen=new MenuScreen(this,stage);

        setScreen(new SplashScreen(this,new Stage(0, 0, true)));
        //setScreen(menuScreen);
	}

    @Override
    public void dispose() {
        super.dispose();

    }

    @Override
    public void render() {
        super.render();
        log.log();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

}
