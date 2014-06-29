package com.indignado.games.smariano.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.indignado.games.smariano.constantes.Constants;
import com.indignado.games.smariano.constantes.GameState;
import com.indignado.games.smariano.modelo.Level;
import com.indignado.games.smariano.modelo.Profile;
import com.indignado.games.smariano.pantallas.transiciones.ScreenTransition;
import com.indignado.games.smariano.pantallas.transiciones.ScreenTransitionSlide;
import com.indignado.games.smariano.utils.gui.ScaleUtil;

public class ScoreScreen extends BaseScreen {

    private Profile profile;
    private Level currentLevel;
    private int coins;
    private int kills;
    private int stars;

    public ScoreScreen() {
        CURRENT_SCREEN = SCREEN.SCORE;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        profile = game.getProfileManager().getProfile();
        currentLevel = game.getLevelManager().getCurrentLevel();
        coins = profile.getCoinsAquired();
        kills = profile.getKills();
        stars = profile.getStarAquired();

        Skin skin = game.getResourcesManager().getStyles().skin;

        mainTable.defaults().spaceBottom(50 * ScaleUtil.getSizeRatio());
        mainTable.setFillParent(true);

        Label killsLabel = new Label("Kills: ", skin);
        killsLabel.setText(killsLabel.getText() + String.valueOf(kills));

        Label coinsLabel = new Label("Gold: ", skin);
        coinsLabel.setText(coinsLabel.getText() + String.valueOf(coins));

        Label starsLabel = new Label("Stars: ", skin);
        starsLabel.setText(starsLabel.getText() + String.valueOf(stars));

        int score = calculateScore();

        Label scoreLabel = new Label("Score: ", skin);
        scoreLabel.setText(scoreLabel.getText() + String.valueOf(score));

        Label highScoreLabel = new Label("HighScoreLabel: ", skin);
        highScoreLabel.setText(highScoreLabel.getText() + String.valueOf(currentLevel.getHighScore()));


        TextButton levelMenuButton = new TextButton("LevelMenu", skin);
        levelMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setGameState(GameState.GAME_SHOW_LEVEL_MENU);
            }
        });

        mainTable.add(killsLabel);
        mainTable.row();
        mainTable.add(coinsLabel);
        mainTable.row();
        mainTable.add(starsLabel);
        mainTable.row();
        mainTable.add(starsLabel);
        mainTable.row();
        mainTable.add(levelMenuButton);
        stage.addActor(mainTable);
        if (profile.getLives() > 0) profile.getLevels().get(currentLevel.getNum() + 1).setActive(true);
        profile.resetValues();
        if (score > currentLevel.getHighScore()) currentLevel.setHighScore(score);
        if (currentLevel.getAchievements() < profile.getStarAquired())
            currentLevel.setAchievements(profile.getStarAquired());
        Gdx.app.log(Constants.LOG, "ScoreScreen, score " + score + " vidas: " + profile.getLives() + " coins: " + profile.getCoinsAquired());
        game.getProfileManager().persist();

    }

    private int calculateScore() {
        int score = 0;
        score = coins * 10;
        score += kills * 25;
        score += stars * 50;
        return score;
    }


    public ScreenTransition getTransition() {
        return ScreenTransitionSlide.init(0.7f,
                ScreenTransitionSlide.DOWN, true, Interpolation.swingOut);
    }
}