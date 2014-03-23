package com.rubentxu.juegos.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.rubentxu.juegos.core.constantes.GameState;
import com.rubentxu.juegos.core.managers.game.AudioManager;
import com.rubentxu.juegos.core.managers.game.LevelManager;
import com.rubentxu.juegos.core.managers.game.PreferencesManager;
import com.rubentxu.juegos.core.managers.game.ProfileManager;
import com.rubentxu.juegos.core.managers.game.ResourcesManager;

public class DreamsGame extends BaseGame {

    public static boolean DEBUG = false;
    public static FPSLogger log;

    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);
        resourcesManager =new ResourcesManager();
        preferencesManager = PreferencesManager.instance;
        profileManager= new ProfileManager();
        levelManager=new LevelManager(this);
        audioManager =new AudioManager(this);

        log = new FPSLogger();
        setGameState(GameState.GAME_SHOW_SPLASH);
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
