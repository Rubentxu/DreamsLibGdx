package com.indignado.games.smariano;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.indignado.games.smariano.constantes.GameState;
import com.indignado.games.smariano.managers.game.*;
import com.indignado.games.smariano.pantallas.BaseScreen;

public class SMariano extends BaseGame {
    public static boolean DEBUG = false;
    public static FPSLogger log;

    @Override
    public void create() {
        BaseScreen.game=this;
        Gdx.input.setCatchBackKey(true);
        resourcesManager =new ResourcesManager();
        preferencesManager = PreferencesManager.instance;
        profileManager= new ProfileManager();
        levelManager=new LevelManager(this);
        audioManager =new AudioManager(this);

        BaseScreen.game=this;

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
