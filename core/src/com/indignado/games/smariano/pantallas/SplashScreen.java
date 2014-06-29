package com.indignado.games.smariano.pantallas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.indignado.games.smariano.constantes.GameState;
import com.indignado.games.smariano.managers.game.ResourcesManager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class SplashScreen extends BaseScreen {
    private Texture splashTexture;


    @Override
    public void show() {
        super.show();
        splashTexture = game.getResourcesManager().get(ResourcesManager.SPLASH);
        splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        mainTable.getColor().a = 0f;
        mainTable.setBackground(new SpriteDrawable(new Sprite(splashTexture)));
        mainTable.addAction(sequence(fadeIn(0.5f), run(new Runnable() {
            public void run() {
                game.menuScreen = new MenuScreen();
            }
        }), delay(1f, fadeOut(0.5f)), run(new Runnable() {
            public void run() {
                game.setGameState(GameState.GAME_SHOW_MENU);
            }
        })));

        stage.addActor(mainTable);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void showDialog() {

    }

}
