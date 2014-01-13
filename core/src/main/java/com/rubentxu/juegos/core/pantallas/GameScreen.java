package com.rubentxu.juegos.core.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.inputs.GameInputs;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.utils.builders.GuiBuilder;
import com.rubentxu.juegos.core.vista.WorldRenderer;


public class GameScreen extends BaseScreen {

    private World world;
    private WorldRenderer renderer;
    private WorldController controller;

    public GameScreen(DreamsGame dreamsGame) {
        super(dreamsGame,new Stage(0, 0, true));
        CURRENT_SCREEN= BaseScreen.SCREEN.MENU;
        world = new World();
        controller = new WorldController(world);
        world.getPhysics().setContactListener(controller);
        renderer = new WorldRenderer(world, true);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        controller.update(delta);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        GameInputs gameInputs = new GameInputs(controller, renderer);
        renderer.setStage(stage);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(gameInputs);
        Gdx.input.setInputProcessor(multiplexer);
        renderer.setSize(width, height);
        if(game.getPreferencesManager().isTouchPadEnabled()){
            GuiBuilder.buildTouchPad(renderer.getStage(), styles, controller);
        } else {
            GuiBuilder.buildPadButtons(renderer.getStage(), styles, controller);
        }
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        assets.dispose();
        world.dispose();
        renderer.dispose();
        controller.dispose();
    }


}
