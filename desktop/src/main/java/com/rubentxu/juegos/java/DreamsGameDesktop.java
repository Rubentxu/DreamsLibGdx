package com.rubentxu.juegos.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;

import java.util.ArrayList;
import java.util.List;


public class DreamsGameDesktop {

    private static List<Vector2> sizes;

    public static void main (String[] args) {
        sizes = new ArrayList<Vector2>();
        sizes.add(new Vector2(480, 272));
        sizes.add(new Vector2(640, 360));
        sizes.add(new Vector2(800, 480));
        sizes.add(new Vector2(960, 540));
        sizes.add(new Vector2(1024, 600));
        sizes.add(new Vector2(1136, 640));
        sizes.add(new Vector2(1280, 720));
        sizes.add(new Vector2(1280, 800));
        sizes.add(new Vector2(1920, 1080));

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Dreams Game " + Constants.VERSION+" "+DreamsGame.log;
        DreamsGame.DEBUG= false;
        config.useGL20 = true;
        config.width = (int) sizes.get(5).x;
        config.height = (int) sizes.get(5).y;

		new LwjglApplication(new DreamsGame(), config);
	}
}
