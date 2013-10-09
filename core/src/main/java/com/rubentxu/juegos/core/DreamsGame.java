package com.rubentxu.juegos.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.rubentxu.juegos.core.pantallas.GameScreen;

public class DreamsGame extends Game {

    public static final String VERSION = "0.1 Pre-Alpha";
    public static final String LOG = "Rubentxu Dreams";
    public static boolean DEBUG = false;
    FPSLogger log;

	@Override
	public void create () {
        log = new FPSLogger();
        setScreen(new GameScreen());
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
