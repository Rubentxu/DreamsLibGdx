package com.rubentxu.juegos.core.pantallas;


import com.badlogic.gdx.Gdx;
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

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

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
        stage.clear();
        final TextButton button = new TextButton("Comenzar", styles.skin);
        button.pad(30);
        button.setChecked(false);
        final TextButton button2 = new TextButton("Opciones", styles.skin);
        button2.pad(30);
        button2.setChecked(false);
        final TextButton button3 = new TextButton("Creditos", styles.skin);
        button3.pad(30);
        button3.setChecked(false);
        button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click Comenzar...");
                game.gameScreen= new GameScreen(game);
                game.setScreen(DreamsGame.gameScreen);
            }
        });

        mainTable.setFillParent(true);
        mainTable.defaults().pad(16f);
        mainTable.add(label("Rubentxu Dreams", Color.CYAN, true));
        mainTable.row();
        mainTable.add(button);
        mainTable.row();
        mainTable.add(button2);
        mainTable.row();
        mainTable.add(button3);
        mainTable.row();
        mainTable.add(label("Pulsa en comenzar, para iniciar la partida.", Color.LIGHT_GRAY, false));
        mainTable.getColor().a = 0f;
        mainTable.addAction(fadeIn(2f));
        mainTable.setBackground(new SpriteDrawable(new Sprite((Texture) assets.get(assets.MENU_BACKGROUND))));

        this.stage.addActor(mainTable);

    }

}
