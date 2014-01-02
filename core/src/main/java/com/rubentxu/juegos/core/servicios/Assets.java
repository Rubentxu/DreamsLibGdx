package com.rubentxu.juegos.core.servicios;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.rubentxu.juegos.core.DreamsGame;

public class Assets extends AssetManager{

    public static final int SCREEN_SPLASH= 0;
    public static final int SCREEN_MENU= 1;
    public static final int SCREEN_GAME= 2;



    public static final String GUI_ATLAS="gui/gui.pack";
    public static final String SPRITE_ATLAS="imagenes/texturas/sprites.pack";
    public static final String VARIOS_ATLAS="imagenes/texturas/varios.pack";
    public static final String DEFAULT_FONT="fonts/DreamOfMe-32.fnt";
    public static final String BIG_FONT="fonts/DreamOfMe-40.fnt";
    public static final String HEADER_FONT="fonts/Bedbug-18.fnt";
    public static final String DEBUG_BACKGROUND="imagenes/texturas/debug.jpg";
    public static final String MENU_BACKGROUND="imagenes/menu-backgroud.jpg";
    public static final String GAME_BACKGROUND="maps/background.png";
    public static final String MAP_DEFAULT="maps/EscenarioDePruebas.tmx";




    public Assets() {
        super();
    }

    public void loadAssetsScreen(int screen){
        switch (screen) {
            case SCREEN_MENU:
                loadAssetsGame();
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
        Gdx.app.log(DreamsGame.LOG, "Loada Assets Game");
        this.load(DEFAULT_FONT, BitmapFont.class);
        this.load(BIG_FONT, BitmapFont.class);
        this.load(HEADER_FONT, BitmapFont.class);
        this.load(DEBUG_BACKGROUND, Texture.class);
        this.load(MENU_BACKGROUND, Texture.class);
        this.load(GAME_BACKGROUND, Texture.class);
        this.load(SPRITE_ATLAS, TextureAtlas.class);
        this.load(VARIOS_ATLAS, TextureAtlas.class);
        this.load(GUI_ATLAS, TextureAtlas.class);
        this.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        this.load(MAP_DEFAULT, TiledMap.class);

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
