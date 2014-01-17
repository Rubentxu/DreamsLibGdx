package com.rubentxu.juegos.core.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.modelo.Profile;

public class HighScoresScreen extends BaseScreen {

    public HighScoresScreen(DreamsGame game) {
        super(game,new Stage(0, 0, true));
        CURRENT_SCREEN = SCREEN.HIGHSCORES;
    }

    @Override
    public void resize(int width, int height) {

        super.resize(width, height);
        Profile profile = getGame().getProfileManager().retrieveProfile();
        Gdx.input.setInputProcessor(stage);

        mainTable.setFillParent(true);

        mainTable.defaults().spaceBottom(30);
        mainTable.add(label("Puntuaciones: ", Color.CYAN, true)).colspan(2);

        // episode 1 high-score
        String level1Highscore = String.valueOf(profile.getHighScore(0));
        Label episode1HighScore = new Label(level1Highscore, styles.skin);
        mainTable.row();
        mainTable.add(label("Episode 1", Color.CYAN, false));
        mainTable.add(episode1HighScore);

        String level2Highscore = String.valueOf(profile.getHighScore(1));
        Label episode2HighScore = new Label(level2Highscore, styles.skin);
        mainTable.row();
        mainTable.add(label("Episode 2", Color.CYAN, false)).center();
        mainTable.add(episode2HighScore);

        String level3Highscore = String.valueOf(profile.getHighScore(2));
        Label episode3HighScore = new Label(level3Highscore, styles.skin);
        mainTable.row();
        mainTable.add(label("Episode 3", Color.CYAN, false));
        mainTable.add(episode3HighScore);

        TextButton backButton = new TextButton("Volver Menu", styles.skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                game.setScreen(game.menuScreen);
            }
        });
        backButton.pad(20);
        mainTable.row();
        mainTable.add(backButton).size(250, 60).colspan(2);

        this.stage.addActor(mainTable);

    }

    private Label label(String text, Color color, boolean scale) {
        Label label = new Label(text, styles.skin, "header", color);
        if (scale == true) label.setFontScale(1.5f);
        label.setAlignment(Align.center, Align.center);
        return label;
    }
}