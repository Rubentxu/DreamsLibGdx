package com.rubentxu.juegos.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.rubentxu.juegos.core.managers.game.LevelManager;
import com.rubentxu.juegos.core.managers.game.MusicManager;
import com.rubentxu.juegos.core.managers.game.PreferencesManager;
import com.rubentxu.juegos.core.managers.game.ProfileManager;
import com.rubentxu.juegos.core.pantallas.SplashScreen;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransition;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransitionFade;

public class DreamsGame extends BaseGame {

    public static boolean DEBUG = true;
    FPSLogger log;

    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);
        preferencesManager = PreferencesManager.instance;
        profileManager= new ProfileManager();
        levelManager=new LevelManager(this);
        musicManager=new MusicManager(this);
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

}
