package com.rubentxu.juegos.core.pantallas;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.rubentxu.juegos.core.DreamsGame;

public class MenuScreen extends BaseScreen {

    public MenuScreen(DreamsGame game) {
        super(game, new Stage(0, 0, true));
        CURRENT_SCREEN=SCREEN.MENU;
    }

    private Label label(String text, Color color, boolean scale) {
        Label label = new Label(text, styles.skin, "header", color);
        if (scale == true) label.setFontScale(2);
        label.setAlignment(Align.center, Align.center);
        return label;
    }

    @Override
    public void resize(int width, int height) {
        Gdx.input.setInputProcessor(stage);
        super.resize(width, height);

        final TextButton btnStart = new TextButton("Comenzar", styles.skin);
        btnStart.pad(30);
        final TextButton btnOptions = new TextButton("Opciones", styles.skin);
        btnOptions.pad(30);
        final TextButton btnScores = new TextButton("Puntuaciones", styles.skin);
        btnScores.pad(30);
        final TextButton button3 = new TextButton("Creditos", styles.skin);
        button3.pad(30);
        button3.setChecked(false);

        btnStart.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click Comenzar...");
                game.gameScreen= new GameScreen(game);
                game.setScreen(game.gameScreen,getTransition());
            }
        });

        btnOptions.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click optionScreen...");
                game.optionScreen= new OptionScreen(game);
                game.setScreen(game.optionScreen,getTransition());
            }
        });

        btnScores.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click highScoreScreen...");
                game.highScoreScreen= new HighScoresScreen(game);
                game.setScreen(game.highScoreScreen,getTransition());
            }
        });


        mainTable.setFillParent(true);
        mainTable.defaults().pad(16f);
        mainTable.add(label("Hero Dreams", Color.CYAN, true));
        mainTable.row();
        mainTable.add(btnStart);
        mainTable.row();
        mainTable.add(btnOptions);
        mainTable.row();
        mainTable.add(btnScores);
        mainTable.row();
        mainTable.add(button3);
        mainTable.row();
        mainTable.add(label("Pulsa en comenzar, para iniciar la partida.", Color.LIGHT_GRAY, false));
        mainTable.setBackground(new SpriteDrawable(new Sprite((Texture) assets.get(assets.MENU_BACKGROUND))));

        this.stage.addActor(mainTable);

    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

}
