package com.rubentxu.juegos.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.rubentxu.juegos.core.pantallas.GameScreen;

public class DreamsGame extends Game {

	@Override
	public void create () {
        setScreen(new GameScreen());
	}


}
