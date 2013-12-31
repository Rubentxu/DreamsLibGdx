package com.rubentxu.juegos.core.servicios;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Assets extends AssetManager{

    public static final int SCREEN_MENU= 1;
    public static final int SCREEN_GAME= 2;
    public static final int SCREEN_SPLASH= 3;

    public static final String GUI_ATLAS="gui/gui.pack";


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
                loadSplash();
                break;
        }

    }

    private void loadAssetsGame() {
        this.load("fonts/DreamOfMe-32.fnt", BitmapFont.class);
        this.load("fonts/DreamOfMe-40.fnt", BitmapFont.class);
        this.load("fonts/Bedbug-18.fnt", BitmapFont.class);
        this.load("imagenes/texturas/debug.jpg", Texture.class);
        this.load("maps/background.png", Texture.class);
        this.load("imagenes/texturas/sprites.pack", TextureAtlas.class);
        this.load("imagenes/texturas/varios.pack", TextureAtlas.class);
        this.load(GUI_ATLAS, TextureAtlas.class);
        this.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        this.load("maps/EscenarioDePruebas.tmx", TiledMap.class);

        this.finishLoading();
    }

    public void loadSplash(){
        this.load("imagenes/splash.jpg", Texture.class);
        this.finishLoading();
    }

	public void dispose() {
        super.dispose();
	}

}
