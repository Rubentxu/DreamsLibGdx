package com.rubentxu.juegos.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;


public class DreamsGameDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Dreams Game " + Constants.VERSION;
        DreamsGame.DEBUG= false;
        config.useGL20 = true;
        config.width = 480;
        config.height = 270;
		new LwjglApplication(new DreamsGame(), config);
	}
}
