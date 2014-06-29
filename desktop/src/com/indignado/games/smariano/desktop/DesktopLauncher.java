package com.indignado.games.smariano.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.indignado.games.smariano.SMariano;
import com.indignado.games.smariano.constantes.Constants;

import java.util.ArrayList;
import java.util.List;

public class DesktopLauncher {

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
        config.title = "Super Mariano Game " + Constants.VERSION+" "+SMariano.log;
        SMariano.DEBUG= false;
        config.useGL30 = true;
        config.width = (int) sizes.get(7).x;
        config.height = (int) sizes.get(7).y;

        new LwjglApplication(new SMariano(), config);
    }
}
