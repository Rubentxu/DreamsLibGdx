package com.rubentxu.juegos.core.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.inputs.GameInputs;
import com.rubentxu.juegos.core.inputs.MobileInput;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.utils.builders.GuiBuilder;
import com.rubentxu.juegos.core.vista.WorldRenderer;


public class GameScreen extends BaseScreen {

    private World world;
    private WorldRenderer renderer;
    private WorldController controller;
    private Table stats;


    public GameScreen(DreamsGame dreamsGame) {
        super(dreamsGame,new Stage(0, 0, true));
        CURRENT_SCREEN= SCREEN.GAME;

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //updates
        controller.update(delta);
        updateStats();
        stage.act(delta);

        //render
        renderer.render();
        stage.draw();

        world.destroyFlaggedEntities();
    }

    private void updateStats(){
        ((Label)stats.findActor(Constants.SCORE)).setText(world.getHero().getProfile().getCreditsAsText());
        ((Label)stats.findActor(Constants.LIVES)).setText(world.getHero().getProfile().getLivesAsText());
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        world=new World(game.getLevelManager().getCurrentLevel());
        world.getHero().setProfile(game.getProfileManager().retrieveProfile());
        controller=new WorldController(world);
        renderer=new WorldRenderer(this);
        renderer.resize(width, height);

        stats=GuiBuilder.buildStats(stage.getWidth(), stage.getHeight(), styles);
        stats.setBounds(0,height-height/7,width,height/7);
        stage.addActor(stats);

        if(game.getPreferencesManager().touchPadEnabled){
            stage.addActor(GuiBuilder.buildTouchPad(stage.getWidth(), stage.getHeight(), styles, controller));
        } else {
            stage.addActor(GuiBuilder.buildPadButtons(stage.getWidth(), stage.getHeight(), styles, controller));
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
        world=null;
        renderer.dispose();
        renderer=null;
        controller.dispose();
        controller=null;
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
    public InputProcessor getInputProcessor () {
        GameInputs gameInputs = new GameInputs(controller, renderer);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new MobileInput());
        multiplexer.addProcessor(gameInputs);
        return multiplexer;
    }
}
