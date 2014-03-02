package com.rubentxu.juegos.core.pantallas;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.inputs.MobileInput;
import com.rubentxu.juegos.core.managers.game.ResourcesManager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class SplashScreen extends BaseScreen {


    private Texture splashTexture;

    public SplashScreen(DreamsGame dreamsGame) {
        super(dreamsGame, new Stage());
    }

    @Override
    public void show() {
        super.show();
        splashTexture = game.getResourcesManager().get(ResourcesManager.SPLASH);
        splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        Image splashImage = new Image(new SpriteDrawable(new Sprite(splashTexture)), Scaling.stretch, Align.bottom | Align.left);
        splashImage.setWidth(width);
        splashImage.setHeight(height);
        splashImage.getColor().a = 0f;

        splashImage.addAction(sequence(fadeIn(0.5f), run(new Runnable() {
            public void run() {
                game.menuScreen = new MenuScreen(game);
            }
        }), delay(1f, fadeOut(0.5f)), run(new Runnable() {
            public void run() {
                game.setScreen(game.menuScreen,getTransition());
            }
        })));

        stage.addActor(splashImage);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public InputProcessor getInputProcessor() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new MobileInput());
        return multiplexer;
    }

    @Override
    public void showDialog(){

    }

}
