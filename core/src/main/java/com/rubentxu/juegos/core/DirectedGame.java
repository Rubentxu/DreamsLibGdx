package com.rubentxu.juegos.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.constantes.GameState;
import com.rubentxu.juegos.core.pantallas.BaseScreen;
import com.rubentxu.juegos.core.pantallas.BaseScreen.SCREEN;
import com.rubentxu.juegos.core.pantallas.GameScreen;
import com.rubentxu.juegos.core.pantallas.HighScoresScreen;
import com.rubentxu.juegos.core.pantallas.MenuScreen;
import com.rubentxu.juegos.core.pantallas.OptionScreen;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransition;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

public abstract class DirectedGame implements ApplicationListener {
    private boolean init=false;
    private BaseScreen currScreen;
    private BaseScreen nextScreen;
    private FrameBuffer currFbo;
    private FrameBuffer nextFbo;
    private SpriteBatch batch;
    private float t;
    private ScreenTransition screenTransition;

    public GameScreen gameScreen;
    public MenuScreen menuScreen;
    public OptionScreen optionScreen;
    public HighScoresScreen highScoreScreen;


    public void setScreen(BaseScreen screen,
                          ScreenTransition screenTransition) {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        if (!init) {
            currFbo = new FrameBuffer(Format.RGB888, w, h, false);
            nextFbo = new FrameBuffer(Format.RGB888, w, h, false);
            batch = new SpriteBatch();
            init = true;
        }

        nextScreen = screen;
        nextScreen.show(); // activate next screen
        nextScreen.resize(w, h);
        nextScreen.render(0); // let screen update() once
        nextScreen.pause();
        Gdx.input.setInputProcessor(null); // disable input
        this.screenTransition = screenTransition;
        t = 0;
        if (currScreen != null) {
            currScreen.pause();
            DreamsGame.gameState = GameState.SCREEN_TRANSITION;
        }else {
            Gdx.input.setInputProcessor(nextScreen.getInputProcessor());
            currScreen = nextScreen;
            nextScreen = null;
            screenTransition = null;
        }


    }

    @Override
    public void render() {
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f / 60.0f);

        switch (DreamsGame.gameState) {
            case GAME_RUNNING:
                Gdx.app.log(Constants.LOG, "GAME STATE: GAME_RUNNING");
                if (currScreen != null) currScreen.render(deltaTime);
            case GAME_PAUSED:
                break;
            case GAME_UPDATE:
                break;
            case GAME_OVER:
                break;
            case GAME_WIN:
                break;
            case GAME_LEVELWIN:
                break;
            case GAME_IDLE:
                break;
            case GAME_SLOWMOTION:
                break;
            case GAME_BACK:
                Gdx.app.log(Constants.LOG, "GAME STATE: GAME_BACK");
                if(currScreen instanceof MenuScreen ){
                   Gdx.app.exit();
                }else {
                    //Presentar una ventana para salir
                    //DreamsGame.gameState=GameState.GAME_PAUSED;
                    if(menuScreen!=null){
                        setScreen(menuScreen,menuScreen.getTransition());
                    }else {
                        Gdx.app.exit();
                    }




                }
                break;
            case SCREEN_TRANSITION:
                Gdx.app.log(Constants.LOG, "GAME STATE: SCREEN_TRANSITION");
                float duration = 0;
                if (screenTransition != null)
                    duration = screenTransition.getDuration();

                t = Math.min(t + deltaTime, duration);
                if (screenTransition == null || t >= duration) {

                    if (currScreen != null) currScreen.hide();
                    nextScreen.resume();

                    Gdx.input.setInputProcessor(nextScreen.getInputProcessor());
                    currScreen = nextScreen;
                    nextScreen = null;
                    screenTransition = null;
                    DreamsGame.gameState = GameState.GAME_RUNNING;
                } else {

                    currFbo.begin();
                    if (currScreen != null) currScreen.render(deltaTime);
                    currFbo.end();
                    nextFbo.begin();
                    nextScreen.render(deltaTime);
                    nextFbo.end();

                    float alpha = t / duration;
                    screenTransition.render(batch,
                            currFbo.getColorBufferTexture(),
                            nextFbo.getColorBufferTexture(),
                            alpha);


                }
                break;
        }

    }

    @Override
    public void resize(int width, int height) {
        if (currScreen != null) currScreen.resize(width, height);
        if (nextScreen != null) nextScreen.resize(width, height);
    }

    @Override
    public void pause() {
        if (currScreen != null) currScreen.pause();
    }

    @Override
    public void resume() {
        if (currScreen != null) currScreen.resume();
    }

    @Override
    public void dispose() {
        if (currScreen != null) currScreen.hide();
        if (nextScreen != null) nextScreen.hide();
        if (init) {
            currFbo.dispose();
            currScreen.dispose();
            currScreen = null;
            nextFbo.dispose();
            nextScreen = null;
            batch.dispose();
            init = false;
        }
    }

    public abstract void createScreen(SCREEN screen );

}

