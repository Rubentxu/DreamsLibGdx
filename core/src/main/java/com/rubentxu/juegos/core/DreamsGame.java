package com.rubentxu.juegos.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.rubentxu.juegos.core.constantes.GameState;
import com.rubentxu.juegos.core.managers.game.PreferencesManager;
import com.rubentxu.juegos.core.managers.game.ProfileManager;
import com.rubentxu.juegos.core.pantallas.BaseScreen.SCREEN;
import com.rubentxu.juegos.core.pantallas.GameScreen;
import com.rubentxu.juegos.core.pantallas.HighScoresScreen;
import com.rubentxu.juegos.core.pantallas.MenuScreen;
import com.rubentxu.juegos.core.pantallas.OptionScreen;
import com.rubentxu.juegos.core.pantallas.SplashScreen;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransition;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransitionFade;
import com.rubentxu.juegos.core.servicios.Assets;
import com.rubentxu.juegos.core.servicios.Styles;

public class DreamsGame extends DirectedGame {

    public static boolean DEBUG = false;
    FPSLogger log;
    private PreferencesManager preferencesManager;

    public static GameState gameState= GameState.GAME_RUNNING;
    private ProfileManager profileManager;


    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);
        preferencesManager = PreferencesManager.instance;
        profileManager= new ProfileManager();
        log = new FPSLogger();

        ScreenTransition transition = ScreenTransitionFade.init(0.75f);
        setScreen(new SplashScreen(this),transition);
        //setScreen(gameScreen);
    }

    @Override
    public void dispose() {
        super.dispose();

    }

    @Override
    public void createScreen(SCREEN screen) {
        switch (screen) {

            case SPLASH:
                break;
            case MENU:
                break;
            case GAME:
                break;
            case OPTIONS:
                break;
            case HIGHSCORES:
                break;
            case CREDITS:
                break;
        }
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

    public PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }


    public ProfileManager getProfileManager() {
        return profileManager;
    }
}
