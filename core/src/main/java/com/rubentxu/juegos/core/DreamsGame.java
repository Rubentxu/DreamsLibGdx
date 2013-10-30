package com.rubentxu.juegos.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.rubentxu.juegos.core.inputs.GameInputs;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.managers.PlatformManager;
import com.rubentxu.juegos.core.managers.RubentxuManager;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.pantallas.GameScreen;
import com.rubentxu.juegos.core.vista.WorldRenderer;

public class DreamsGame extends Game {

    public static final String VERSION = "0.1 Pre-Alpha";
    public static final String LOG = "Rubentxu Dreams";
    public static boolean DEBUG = false;
    FPSLogger log;
    private World world;
    private WorldRenderer renderer;
    private WorldController controller;
    private GameScreen gameScreen;
    private GameInputs gameInputs;

    @Override
	public void create () {
        log = new FPSLogger();
        world = new World();
        renderer=new WorldRenderer(world, true);

        RubentxuManager rubenManager = new RubentxuManager(world.getRuben());
        PlatformManager platformManager = new PlatformManager();
        platformManager.setMovingPlatformplatforms(world.getMovingPlatformplatforms());
        platformManager.setPlatforms(world.getPlatforms());

        controller= new WorldController();
        controller.setRubenManager(rubenManager);
        controller.setPlatformManager(platformManager);
        world.getPhysics().setContactListener(controller);


        gameScreen= new GameScreen( world,controller,renderer);
        gameInputs = new GameInputs(world, controller, renderer);
        Gdx.input.setInputProcessor(gameInputs);
        setScreen(gameScreen);
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
