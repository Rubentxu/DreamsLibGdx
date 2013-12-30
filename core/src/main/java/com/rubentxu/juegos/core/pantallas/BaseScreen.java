package com.rubentxu.juegos.core.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.servicios.Assets;
import com.rubentxu.juegos.core.servicios.Styles;

public abstract class BaseScreen implements Screen
{
    protected final DreamsGame game;
    protected final BitmapFont font;
    protected final SpriteBatch batch;
    protected final Stage stage;
    protected final Styles styles;
    protected Assets assets;
    protected final Table mainTable = new Table();
    protected float width,height;

    public BaseScreen(DreamsGame game, Assets assets,Stage stage)
    {
        this.styles= new Styles(assets);
        this.game = game;
        this.font = styles.font;
        this.batch = new SpriteBatch();
        this.stage = stage;
    }

    protected String getName() {
        return getClass().getName();
    }

    @Override
    public void show()
    {
        assets = new Assets();
        Gdx.app.log(DreamsGame.LOG, "Showing screen: " + getName());
    }

    @Override
    public void resize(int width, int height) {
        this.width=width;
        this.height=height;
        Gdx.app.log(DreamsGame.LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height);
        stage.setViewport(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide()
    {
        Gdx.app.log( DreamsGame.LOG, "Hiding screen: " + getName() );
    }

    @Override
    public void pause()
    {
        Gdx.app.log( DreamsGame.LOG, "Pausing screen: " + getName() );
    }

    @Override
    public void resume()
    {
        Gdx.app.log( DreamsGame.LOG, "Resuming screen: " + getName() );
    }

    @Override
    public void dispose()
    {
        Gdx.app.log( DreamsGame.LOG, "Disposing screen: " + getName() );
        stage.dispose();
        font.dispose();
        batch.dispose();
    }
}