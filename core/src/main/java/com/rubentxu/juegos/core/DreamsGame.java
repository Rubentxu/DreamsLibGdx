package com.rubentxu.juegos.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
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

    public static GameScreen gameScreen;
    public static MenuScreen menuScreen;
    public static OptionScreen optionScreen;
    public static HighScoresScreen highScoreScreen;
    private ProfileManager profileManager;


    @Override
    public void create() {
        preferencesManager = new PreferencesManager();
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
