package com.rubentxu.juegos.core;

import com.badlogic.gdx.Game;
import com.rubentxu.juegos.core.pantallas.GameScreen;

public class DreamsGame extends Game {

	@Override
	public void create () {
        setScreen(new GameScreen());
	}
    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void render() {
        super.render();
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
