package com.rubentxu.juegos.core.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.servicios.Assets;

public class SplashScreen implements Screen {
    protected Stage stage;
    private DreamsGame dreamsGame;
    private Image splashImage;
    private Assets assets;

    public SplashScreen(DreamsGame dreamsGame) {
        this.dreamsGame = dreamsGame;
        this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 0.6f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.stage = new Stage(width, height, true);
    }

    @Override
    public void show() {
        assets = new Assets();

        Drawable splashDrawable = new SpriteDrawable(new Sprite(assets.loadSplash()));
        splashImage = new Image(splashDrawable, Scaling.stretch);
        splashImage.setFillParent(true);
        //splashImage.getColor().a = 0.8f;

        stage.addActor(splashImage);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        assets.dispose();
    }
}
