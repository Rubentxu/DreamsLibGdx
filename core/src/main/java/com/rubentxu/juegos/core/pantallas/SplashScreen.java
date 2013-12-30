package com.rubentxu.juegos.core.pantallas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.servicios.Assets;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class SplashScreen extends BaseScreen {

    private Image splashImage;
    private Texture splashTexture;

    public SplashScreen(DreamsGame dreamsGame,Assets assets,Stage stage) {
        super(dreamsGame, assets,stage);
    }


    @Override
    public void show() {
        super.show();
        assets.loadSplash();
        splashTexture = assets.get("imagenes/splash.jpg") ;
        splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.clear();
        TextureRegion splashRegion = new TextureRegion(splashTexture, 0, 0, width, height);

        splashImage = new Image(new TextureRegionDrawable(splashRegion), Scaling.stretch, Align.bottom | Align.left);
        splashImage.setWidth(width);
        splashImage.setHeight(height);
        splashImage.getColor().a = 0f;

        splashImage.addAction(sequence(fadeIn(1.75f), delay(2f, fadeOut(1.75f)), run(new Runnable() {
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
