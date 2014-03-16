package com.rubentxu.juegos.core.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.constantes.GameState;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.inputs.GameInputs;
import com.rubentxu.juegos.core.inputs.MobileInput;
import com.rubentxu.juegos.core.modelo.Hero;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransition;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransitionSlice;
import com.rubentxu.juegos.core.utils.builders.GuiBuilder;
import com.rubentxu.juegos.core.utils.gui.ScaleUtil;
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
        if(!DreamsGame.getGameState().equals(GameState.GAME_PAUSED)){
            controller.update(delta);
            updateStats();
        }
        stage.act(delta);
        //render
        renderer.render();
        stage.draw();
    }

    private void updateStats(){
        Hero hero=world.getHero();
        ((Label)stats.findActor(Constants.SCORE)).setText(hero.getProfile().getCreditsAsText());
        ((Label)stats.findActor(Constants.LIVES)).setText(hero.getProfile().getLivesAsText());
    }

    @Override
    public void showDialog() {
        if(dialog==null ){
            dialog = new Window("Que desea hacer ?", game.getResourcesManager().getStyles().skin);

            TextButton btnSalir = new TextButton("Salir", game.getResourcesManager().getStyles().skin);
            TextButton btnContinuar = new TextButton("Continuar", game.getResourcesManager().getStyles().skin);
            btnSalir.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Click Salir...");
                    DreamsGame.setGameState(GameState.GAME_SHOW_MENU);
                }
            });
            btnContinuar.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Click Continuar...");
                    DreamsGame.setGameState(GameState.GAME_RUNNING);
                    dialog.remove();
                    dialog=null;
                }
            });

            dialog.defaults().spaceBottom(10);
            dialog.row().fill().expandX();
            dialog.add(btnContinuar);
            dialog.add(btnSalir);
            dialog.pack();
            dialog.setPosition(width/2-dialog.getWidth()/2, height/2-dialog.getHeight()/2);
            stage.addActor(dialog);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        world=new World(game);
        Hero hero= world.getHero();
        hero.setProfile(game.getProfileManager().retrieveProfile());
        controller=new WorldController(game,world);
        renderer=new WorldRenderer(game,world);
        renderer.resize(width, height);

        stats=GuiBuilder.buildStats(stage.getWidth(), 100* ScaleUtil.getSizeRatio(), game.getResourcesManager().getStyles(),game.getResourcesManager());
        stats.setBounds(0,height-height/7,width,height/7);
        stage.addActor(stats);

        if(game.getPreferencesManager().touchPadEnabled){
            Touchpad touchPad = GuiBuilder.buildTouchPad(350 * ScaleUtil.getSizeRatio(), 350 * ScaleUtil.getSizeRatio(), game.getResourcesManager().getStyles(), controller);
            //touchPad.scale(ScaleUtil.getSizeRatio());
            stage.addActor(touchPad);
        } else {
            stage.addActor(GuiBuilder.buildPadButtons(370*ScaleUtil.getSizeRatio(),190*ScaleUtil.getSizeRatio(), game.getResourcesManager().getStyles(), controller));
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
    public InputProcessor getInputProcessor () {
        GameInputs gameInputs = new GameInputs(controller, renderer);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new MobileInput());
        multiplexer.addProcessor(gameInputs);
        return multiplexer;
    }
}
