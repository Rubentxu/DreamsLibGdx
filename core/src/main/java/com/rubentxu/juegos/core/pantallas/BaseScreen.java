package com.rubentxu.juegos.core.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.constantes.GameState;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransition;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransitionSlide;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

public abstract class BaseScreen implements Screen {

    protected final DreamsGame game;
    protected Stage stage;
    protected Table mainTable;
    protected Window dialog;
    protected float width;
    protected float height;
    public static SCREEN CURRENT_SCREEN = SCREEN.SPLASH;
    //private ScreenTransition transition = ScreenTransitionFade.init(0.75f);
    private ScreenTransition transition = ScreenTransitionSlide.init(0.7f,
            ScreenTransitionSlide.DOWN, false, Interpolation.swingOut);


    // ScreenTransition transition = ScreenTransitionSlice.init(2,ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);


    public static enum SCREEN {SPLASH, MENU, GAME, OPTIONS, HIGHSCORES, CREDITS, LEVELSCREEN}


    public BaseScreen(DreamsGame game, Stage stage) {
        this.game = game;
        this.stage = stage;
    }

    protected String getName() {
        return getClass().getName();
    }

    @Override
    public void show() {
        mainTable = new Table();
        Gdx.app.log(Constants.LOG, "Showing screen: " + getName() + " Current_Screen " + CURRENT_SCREEN);


    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(Constants.LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height);
        this.width = width;
        this.height = height;
        stage.setViewport(width, height, true);
        stage.clear();
        mainTable.getColor().a = 0f;
        mainTable.addAction(fadeIn(0.2f));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        Gdx.app.log(Constants.LOG, "Hiding screen: " + getName());
    }

    @Override
    public void pause() {
        Gdx.app.log(Constants.LOG, "Pausing screen: " + getName());
    }

    @Override
    public void resume() {
        Gdx.app.log(Constants.LOG, "Resuming screen: " + getName());
    }

    @Override
    public void dispose() {
        Gdx.app.log(Constants.LOG, "Disposing screen: " + getName());
        stage.dispose();
        mainTable = null;
        stage = null;
    }


    public DreamsGame getGame() {
        return game;
    }

    public ScreenTransition getTransition() {
        return transition;
    }

    public abstract InputProcessor getInputProcessor();

    public void showDialog() {
        if(dialog==null ){
            dialog = new Window("Que desea hacer ?", game.getResourcesManager().getStyles().skin);

            TextButton btnSalir = new TextButton("Salir", game.getResourcesManager().getStyles().skin);
            TextButton btnContinuar = new TextButton("Continuar", game.getResourcesManager().getStyles().skin);
            btnSalir.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Click Salir...");
                    DreamsGame.setGameState(GameState.GAME_EXIT);
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

}