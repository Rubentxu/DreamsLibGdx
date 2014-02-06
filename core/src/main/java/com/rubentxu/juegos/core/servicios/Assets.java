package com.rubentxu.juegos.core.servicios;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.pantallas.BaseScreen;

public class Assets extends AssetManager implements Disposable {

    private static Assets instance = new Assets();

    public static final String GUI_ATLAS = "gui/gui.pack";
    public static final String UISKIN_ATLAS = "gui/uiskin.pack";
    public static final String SPRITE_ATLAS = "imagenes/animaciones/sprites.pack";
    public static final String VARIOS_ATLAS = "imagenes/varios/varios.pack";
    public static final String SPLASH = "imagenes/fondos/splash.jpg";
    public static final String DEFAULT_FONT = "fonts/DreamOfMe-32.fnt";
    public static final String BIG_FONT = "fonts/DreamOfMe-40.fnt";
    public static final String HEADER_FONT = "fonts/Bedbug-18.fnt";
    public static final String DEBUG_BACKGROUND = "imagenes/fondos/debug.jpg";
    public static final String TREE_BACKGROUND = "imagenes/fondos/arboles.png";
    public static final String LEVEL1_BACKGROUND = "imagenes/fondos/fondo.jpg";
    public static final String STATS_BACKGROUND = "imagenes/fondos/fondoStats.png";
    public static final String CLOUD_BACKGROUND = "imagenes/fondos/nubes.png";
    public static final String MENU_BACKGROUND = "imagenes/fondos/menu-backgroud.jpg";
    public static final String MAP_DEFAULT = "maps/EscenarioDePruebas.tmx";
    public static final String PARTICLE_EFFECT = "particles/dust.pfx";

    private Assets() {
        super();
        loadSplash();
        loadAssetsGame();
    }

    public static Assets getInstance() {
        return instance;
    }

    private void loadAssetsGame() {
        Gdx.app.log(Constants.LOG, "Load Assets Game");
        this.load(DEFAULT_FONT, BitmapFont.class);
        this.load(BIG_FONT, BitmapFont.class);
        this.load(HEADER_FONT, BitmapFont.class);
        this.load(DEBUG_BACKGROUND, Texture.class);
        this.load(MENU_BACKGROUND, Texture.class);
        this.load(CLOUD_BACKGROUND, Texture.class);
        this.load(TREE_BACKGROUND, Texture.class);
        this.load(STATS_BACKGROUND, Texture.class);
        this.load(LEVEL1_BACKGROUND, Texture.class);

        this.load(SPRITE_ATLAS, TextureAtlas.class);
        this.load(VARIOS_ATLAS, TextureAtlas.class);
        this.load(GUI_ATLAS, TextureAtlas.class);
        this.load(UISKIN_ATLAS, TextureAtlas.class);
        this.load(PARTICLE_EFFECT, ParticleEffect.class);
        this.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        this.load(MAP_DEFAULT, TiledMap.class);

        this.finishLoading();
    }

    public void loadSplash() {
        this.load(SPLASH, Texture.class);
        this.finishLoading();
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }

}
