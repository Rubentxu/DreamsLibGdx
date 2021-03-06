package com.rubentxu.juegos.core.pantallas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.GameState;
import com.rubentxu.juegos.core.modelo.Profile;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransition;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransitionSlide;
import com.rubentxu.juegos.core.utils.gui.ScaleUtil;

public class HighScoresScreen extends BaseScreen {

    public HighScoresScreen(DreamsGame game) {
        super(game, new Stage(0, 0, true));
        CURRENT_SCREEN = SCREEN.HIGHSCORES;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Profile profile = getGame().getProfileManager().getProfile();

        mainTable.setFillParent(true);

        mainTable.defaults().spaceBottom(50 * ScaleUtil.getSizeRatio());
        mainTable.add(label("Puntuaciones: ", Color.ORANGE)).colspan(2);

        String level1Highscore = String.valueOf(profile.getHighScore(0));
        Label episode1HighScore = new Label(level1Highscore, game.getResourcesManager().getStyles().skin);
        mainTable.row();
        mainTable.add(label("Nivel 1", Color.CYAN));
        mainTable.add(episode1HighScore);

        String level2Highscore = String.valueOf(profile.getHighScore(1));
        Label episode2HighScore = new Label(level2Highscore, game.getResourcesManager().getStyles().skin);
        mainTable.row();
        mainTable.add(label("Nivel 2", Color.CYAN)).center();
        mainTable.add(episode2HighScore);

        String level3Highscore = String.valueOf(profile.getHighScore(2));
        Label episode3HighScore = new Label(level3Highscore, game.getResourcesManager().getStyles().skin);
        mainTable.row();
        mainTable.add(label("Nivel 3", Color.CYAN));
        mainTable.add(episode3HighScore);

        TextButton backButton = new TextButton("Volver Menu", game.getResourcesManager().getStyles().skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                game.setGameState(GameState.GAME_SHOW_MENU);
            }
        });

        int pad = (int) (20 * ScaleUtil.getSizeRatio());
        int pad2 = (int) (60 * ScaleUtil.getSizeRatio());
        backButton.pad(pad, pad2, pad, pad2);
        mainTable.row();
        mainTable.add(backButton).colspan(2);

        this.stage.addActor(mainTable);

    }

    public ScreenTransition getTransition() {
        return ScreenTransitionSlide.init(0.7f,
                ScreenTransitionSlide.UP, true, Interpolation.swingOut);
    }

    private Label label(String text, Color color) {
        Label label = new Label(text, game.getResourcesManager().getStyles().skin, "header", color);
        label.setAlignment(Align.center, Align.center);
        return label;
    }
}