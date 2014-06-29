package com.indignado.games.smariano.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.indignado.games.smariano.SMariano;
import com.indignado.games.smariano.constantes.Constants;
import com.indignado.games.smariano.constantes.GameState;
import com.indignado.games.smariano.pantallas.transiciones.ScreenTransition;
import com.indignado.games.smariano.pantallas.transiciones.ScreenTransitionSlide;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

public abstract class BaseScreen implements Screen {

    public static final Stage stage = new Stage(new ExtendViewport(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT));
    public static SMariano game;
    public static SCREEN CURRENT_SCREEN = SCREEN.SPLASH;
    protected static int height = Gdx.graphics.getHeight();
    protected static int width = Gdx.graphics.getWidth();
    protected Table mainTable;
    protected Window dialog;
    protected Label message;
    protected Stack container;
    //private ScreenTransition transition = ScreenTransitionFade.init(0.75f);
    private ScreenTransition transition = ScreenTransitionSlide.init(0.7f,
            ScreenTransitionSlide.DOWN, false, Interpolation.swingOut);


    // ScreenTransition transition = ScreenTransitionSlice.init(2,ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);

    protected String getName() {
        return this.getClass().getName();
    }

    @Override
    public void show() {
        mainTable = new Table();
        mainTable.setFillParent(true);
        Gdx.app.log(Constants.LOG, "Showing screen: " + getName() + " Current_Screen " + CURRENT_SCREEN);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(Constants.LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height);
        stage.clear();
        stage.getViewport().update(width, height, true);
        setBackBackButton();
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

    }

    public SMariano getGame() {
        return game;
    }

    public ScreenTransition getTransition() {
        return transition;
    }

    public void showDialog() {
        if (dialog == null) {
            dialog = new Window("Que desea hacer ?", game.getResourcesManager().getStyles().skin);

            TextButton btnSalir = new TextButton("Salir", game.getResourcesManager().getStyles().skin);
            TextButton btnContinuar = new TextButton("Continuar", game.getResourcesManager().getStyles().skin);
            btnSalir.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Click Salir...");
                    SMariano.setGameState(GameState.GAME_EXIT);
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

    public void showMessage(String text, float time, final GameState gameState) {
        Gdx.app.debug(Constants.LOG, "Show Message !");
        if (message == null) {
            message = new Label(text, game.getResourcesManager().getStyles().skin, "header", Color.ORANGE);
            container = new Stack();
            container.add(message);
            container.setPosition(Gdx.graphics.getWidth() / 2 - container.getWidth() / 2,
                    Gdx.graphics.getHeight() + container.getHeight());
            container.setVisible(false);
            stage.addActor(container);

        } else if (!container.isVisible()) {
            message.setText(text);
            container.pack();
            MoveToAction action = Actions.action(MoveToAction.class);
            action.setPosition(Gdx.graphics.getWidth() / 2 - container.getWidth() / 2,
                    Gdx.graphics.getHeight() / 2 - container.getHeight() / 2);
            action.setDuration(time);
            action.setInterpolation(Interpolation.bounceIn);

            container.addAction(Actions.sequence(action, Actions.delay(1f),
                    Actions.run(new Runnable() {
                        public void run() {
                            Gdx.app.log(Constants.LOG, "Show Message Actions complete!");
                            SMariano.setGameState(gameState);
                            container.setVisible(false);
                        }
                    })
            ));
            container.setVisible(true);
        }

    }

    public InputProcessor getInputProcessor() {
        return stage;
    }

    private void setBackBackButton() {
        stage.addListener(new InputListener() {

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK)) {
                    Gdx.app.log(Constants.LOG, "PRESS BUTTON: GAME_BACK");
                    SMariano.setGameState(GameState.GAME_BACK);
                }

                return false;
            }
        });
    }

    public static enum SCREEN {SPLASH, MENU, GAME, OPTIONS, HIGHSCORES, SCORE, CREDITS, LEVELSCREEN}


}