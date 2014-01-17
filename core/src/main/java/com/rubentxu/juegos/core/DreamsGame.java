package com.rubentxu.juegos.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.rubentxu.juegos.core.constantes.GameState;
import com.rubentxu.juegos.core.managers.game.PreferencesManager;
import com.rubentxu.juegos.core.managers.game.ProfileManager;
import com.rubentxu.juegos.core.pantallas.GameScreen;
import com.rubentxu.juegos.core.pantallas.HighScoresScreen;
import com.rubentxu.juegos.core.pantallas.MenuScreen;
import com.rubentxu.juegos.core.pantallas.OptionScreen;
import com.rubentxu.juegos.core.pantallas.SplashScreen;

public class DreamsGame extends Game {

    public static boolean DEBUG = false;
    FPSLogger log;
    private PreferencesManager preferencesManager;

    public GameScreen gameScreen;
    public MenuScreen menuScreen;
    public OptionScreen optionScreen;
    public HighScoresScreen highScoreScreen;
    public static GameState gameState= GameState.GAME_RUNNING;
    private ProfileManager profileManager;



    @Override
    public void create() {
        preferencesManager = PreferencesManager.instance;
        profileManager= new ProfileManager();
        log = new FPSLogger();
        setScreen(new SplashScreen(this));
        //setScreen(gameScreen);
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

    public PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }


    public ProfileManager getProfileManager() {
        return profileManager;
    }
}
