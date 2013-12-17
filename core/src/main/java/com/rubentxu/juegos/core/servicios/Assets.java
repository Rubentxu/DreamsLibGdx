package com.rubentxu.juegos.core.servicios;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets extends AssetManager{

    public static final int SCREEN_MENU= 1;
    public static final int SCREEN_GAME= 2;
    public static final int SCREEN_SPLASH= 3;


    public Assets() {
        super();
    }

    public void loadAssetsScreen(int screen){
        switch (screen) {
            case SCREEN_MENU:
                break;

            case SCREEN_GAME:
                loadAssetsGame();
                break;
            case SCREEN_SPLASH:
                break;
        }

    }

    private void loadAssetsGame() {
        this.load("fonts/DreamOfMe-12.fnt", BitmapFont.class);
        this.load("imagenes/texturas/debug.jpg", Texture.class);
        this.load("maps/background.png", Texture.class);
        this.load("imagenes/texturas/sprites.pack", TextureAtlas.class);
        this.load("imagenes/texturas/varios.pack", TextureAtlas.class);
        this.finishLoading();
    }

	public void dispose() {
        this.dispose();
	}

}
