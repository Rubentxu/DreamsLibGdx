package com.rubentxu.juegos.core.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.servicios.Assets;
import com.rubentxu.juegos.core.servicios.Styles;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

public abstract class BaseScreen implements Screen {

    protected final DreamsGame game;
    protected Stage stage;
    protected Assets assets;
    public Styles styles;
    protected Table mainTable;
    protected float width;
    protected float height;
    public static SCREEN CURRENT_SCREEN = SCREEN.SPLASH;

    public DreamsGame getGame() {
        return game;
    }

    public static enum SCREEN {SPLASH, MENU, GAME, OPTIONS, HIGHSCORES, CREDITS}


    public BaseScreen(DreamsGame game, Stage stage) {
        this.game = game;
        this.stage = stage;
    }

    protected String getName() {
        return getClass().getName();
    }

    @Override
    public void show() {
        mainTable= new Table();
        Gdx.app.log(Constants.LOG, "Showing screen: " + getName() + " Current_Screen " + CURRENT_SCREEN);
        assets = Assets.getInstance();
        assets.loadAssetsScreen(CURRENT_SCREEN);
        if(!CURRENT_SCREEN.equals(SCREEN.SPLASH)) this.styles = new Styles(assets);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(Constants.LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height);
        this.width = width;
        this.height = height;
        stage.setViewport(width, height, true);
        stage.clear();
        mainTable.getColor().a = 0f;
        mainTable.addAction(fadeIn(1.2f));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        Gdx.app.log(Constants.LOG, "Hiding screen: " + getName());
    }

    @Override
    public void pause() {
        Gdx.app.log(Constants.LOG, "Pausing screen: " + getName());
    }

    @Override
    public void resume() {
        Gdx.app.log(Constants.LOG, "Resuming screen: " + getName());
    }

    @Override
    public void dispose() {
        Gdx.app.log(Constants.LOG, "Disposing screen: " + getName());
        stage.dispose();
        assets.dispose();
        styles.dispose();
        mainTable=null;
        stage=null;
        assets=null;
        styles=null;
    }
}