package com.indignado.games.smariano.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.indignado.games.smariano.SMariano;
import com.indignado.games.smariano.constantes.Constants;
import com.indignado.games.smariano.constantes.GameState;
import com.indignado.games.smariano.controladores.WorldController;
import com.indignado.games.smariano.inputs.GameInputs;
import com.indignado.games.smariano.modelo.Hero;
import com.indignado.games.smariano.modelo.Profile;
import com.indignado.games.smariano.modelo.World;
import com.indignado.games.smariano.pantallas.transiciones.ScreenTransition;
import com.indignado.games.smariano.pantallas.transiciones.ScreenTransitionSlice;
import com.indignado.games.smariano.utils.builders.GuiBuilder;
import com.indignado.games.smariano.utils.gui.ScaleUtil;
import com.indignado.games.smariano.vista.WorldRenderer;


public class GameScreen extends BaseScreen {

    private World world;
    private WorldRenderer renderer;
    private WorldController controller;
    private Profile profile;
    private Table stats;


    public GameScreen() {
        CURRENT_SCREEN = SCREEN.GAME;
        profile = game.getProfileManager().getProfile();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //updates
        if (!SMariano.getGameState().equals(GameState.GAME_PAUSED) &&
                !SMariano.getGameState().equals(GameState.GAME_LEVELWIN) &&
                !SMariano.getGameState().equals(GameState.GAME_OVER)) {
            controller.update(delta);
            updateStats();
        }

        stage.act(delta);
        //render
        renderer.render();
        stage.draw();
    }

    private void updateStats() {
        Hero hero = world.getHero();
        ((Label) stats.findActor(Constants.SCORE)).setText(String.valueOf(profile.getCoinsAquired()));
        ((Label) stats.findActor(Constants.LIVES)).setText(profile.getLivesAsText());
    }

    @Override
    public void showDialog() {
        if (dialog == null) {
            dialog = new Window("Que desea hacer ?", game.getResourcesManager().getStyles().skin);

            TextButton btnSalir = new TextButton("Salir", game.getResourcesManager().getStyles().skin);
            TextButton btnContinuar = new TextButton("Continuar", game.getResourcesManager().getStyles().skin);
            btnSalir.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Click Salir...");
                    SMariano.setGameState(GameState.GAME_SHOW_MENU);
                }
            });
            btnContinuar.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Click Continuar...");
                    SMariano.setGameState(GameState.GAME_RUNNING);
                    dialog.remove();
                    dialog = null;
                }
            });

            dialog.defaults().spaceBottom(10);
            dialog.row().fill().expandX();
            dialog.add(btnContinuar);
            dialog.add(btnSalir);
            dialog.pack();
            dialog.setPosition(width / 2 - dialog.getWidth() / 2, height / 2 - dialog.getHeight() / 2);
            stage.addActor(dialog);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        world = new World(game);
        controller = new WorldController(game, world);
        renderer = new WorldRenderer(game, world);
        renderer.resize(width, height);

        stats = GuiBuilder.buildStats(stage.getWidth(), 100 * ScaleUtil.getSizeRatio(), game.getResourcesManager().getStyles(), game.getResourcesManager());
        stats.setBounds(0, height - height / 7, width, height / 7);
        stage.addActor(stats);

        if (game.getPreferencesManager().touchPadEnabled) {
            Touchpad touchPad = GuiBuilder.buildTouchPad(350 * ScaleUtil.getSizeRatio(), 350 * ScaleUtil.getSizeRatio(), game.getResourcesManager().getStyles(), controller);
            stage.addActor(touchPad);
        } else {
            stage.addActor(GuiBuilder.buildPadButtons(370 * ScaleUtil.getSizeRatio(), 190 * ScaleUtil.getSizeRatio(), game.getResourcesManager().getStyles(), controller));
        }
    }

    @Override
    public void hide() {
        super.hide();
        dispose();
    }


    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        world.dispose();
        world = null;
        renderer.dispose();
        renderer = null;
        controller.dispose();
        controller = null;
    }

    public ScreenTransition getTransition() {
        return ScreenTransitionSlice.init(0.6f, ScreenTransitionSlice.UP, 20, Interpolation.swingOut);
    }


    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public WorldRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(WorldRenderer renderer) {
        this.renderer = renderer;
    }

    public WorldController getController() {
        return controller;
    }

    public void setController(WorldController controller) {
        this.controller = controller;
    }

    @Override
    public InputProcessor getInputProcessor() {
        GameInputs gameInputs = new GameInputs(controller, renderer);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(gameInputs);
        return multiplexer;
    }
}
