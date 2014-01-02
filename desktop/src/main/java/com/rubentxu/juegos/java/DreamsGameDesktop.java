package com.rubentxu.juegos.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.rubentxu.juegos.core.DreamsGame;

public class DreamsGameDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Dreams Game " + DreamsGame.VERSION;
        DreamsGame.DEBUG= true;
        config.useGL20 = true;
        config.width = 600;
        config.height = 480;
		new LwjglApplication(new DreamsGame(), config);
	}
}
