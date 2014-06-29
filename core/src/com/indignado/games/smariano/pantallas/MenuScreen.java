package com.indignado.games.smariano.pantallas;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.indignado.games.smariano.constantes.GameState;
import com.indignado.games.smariano.managers.game.ResourcesManager;
import com.indignado.games.smariano.pantallas.transiciones.ScreenTransition;
import com.indignado.games.smariano.pantallas.transiciones.ScreenTransitionSlide;
import com.indignado.games.smariano.utils.gui.ScaleUtil;

public class MenuScreen extends BaseScreen {

    public MenuScreen() {
        CURRENT_SCREEN = SCREEN.MENU;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        int pad = (int) (20 * ScaleUtil.getSizeRatio());
        int pad2 = (int) (60 * ScaleUtil.getSizeRatio());
        final TextButton btnStart = new TextButton("Comenzar", game.getResourcesManager().getStyles().skin);
        btnStart.pad(pad, pad2, pad, pad2);
        final TextButton btnOptions = new TextButton("Opciones", game.getResourcesManager().getStyles().skin);
        btnOptions.pad(pad, pad2, pad, pad2);
        final TextButton btnScores = new TextButton("Puntuaciones", game.getResourcesManager().getStyles().skin);
        btnScores.pad(pad, pad2, pad, pad2);
        final TextButton button3 = new TextButton("Creditos", game.getResourcesManager().getStyles().skin);
        button3.pad(pad, pad2, pad, pad2);
        button3.setChecked(false);

        btnStart.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click Comenzar...");
                game.setGameState(GameState.GAME_SHOW_LEVEL_MENU);
            }
        });

        btnOptions.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click optionScreen...");
                game.setGameState(GameState.GAME_SHOW_OPTIONS);
            }
        });

        btnScores.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click highScoreScreen...");
                game.setGameState(GameState.GAME_SHOW_HIGHSCORES);
            }
        });

        Label label = new Label("SUPER MARIANO", game.getResourcesManager().getStyles().skin, "header", Color.CYAN);
        label.setAlignment(Align.center, Align.center);
        mainTable.defaults().padBottom(pad);
        if (height < 480) mainTable.defaults().height(height / 5f - pad);
        mainTable.add(label);
        mainTable.row();
        mainTable.add(btnStart);
        mainTable.row();
        mainTable.add(btnOptions);
        mainTable.row();
        mainTable.add(btnScores);
        mainTable.row();
        mainTable.add(button3);
        mainTable.row();
        mainTable.setBackground(new SpriteDrawable(new Sprite((Texture) game.getResourcesManager().get(ResourcesManager.MENU_BACKGROUND))));
        mainTable.row();
        this.stage.addActor(mainTable);

    }

    public ScreenTransition getTransition() {
        return ScreenTransitionSlide.init(0.7f,
                ScreenTransitionSlide.RIGHT, true, Interpolation.swingOut);
    }
}
