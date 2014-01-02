package com.rubentxu.juegos.core.pantallas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.rubentxu.juegos.core.DreamsGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class SplashScreen extends BaseScreen {

    private Image splashImage;
    private Texture splashTexture;

    public SplashScreen(DreamsGame dreamsGame, Stage stage) {
        super(dreamsGame,stage);
        CURRENT_SCREEN=0;
    }

    @Override
    public void show() {
        super.show();
        splashTexture = assets.get("imagenes/splash.jpg") ;
        splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.clear();

        splashImage = new Image(new SpriteDrawable(new Sprite(splashTexture)), Scaling.stretch, Align.bottom | Align.left);
        splashImage.setWidth(width);
        splashImage.setHeight(height);
        splashImage.getColor().a = 0f;

        splashImage.addAction(sequence(fadeIn(2.5f), delay(3f, fadeOut(2.5f)), run(new Runnable() {
            public void run() {
                System.out.println("Action complete!");
                game.setScreen(game.menuScreen);
            }
        })));

        stage.addActor(splashImage);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
