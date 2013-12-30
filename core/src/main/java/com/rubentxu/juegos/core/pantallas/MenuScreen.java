package com.rubentxu.juegos.core.pantallas;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.servicios.Assets;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class MenuScreen extends BaseScreen {

    public MenuScreen(final DreamsGame game,Assets assets,Stage stage) {
        super(game,assets,stage);
        final TextButton button = new TextButton("Comenzar...", styles.skin);
        button.pad(20);
        button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click Comenzar...");
                game.setScreen(game.gameScreen);
                button.setChecked(true);
            }
        });
        //mainTable.setSize(600, 480);
        mainTable.setPosition(width/2, height/2);
        mainTable.setFillParent(true);
        mainTable.defaults().pad(16f);
        mainTable.setBackground(styles.skin.getDrawable("window1"));
        mainTable.setColor(styles.skin.getColor("lt-blue"));
        mainTable.add(label("gdx-ui-app: test!", Color.GREEN));
        mainTable.row();
        mainTable.add(button);
        mainTable.row();
        mainTable.add(label("To play:\nclick the objects moving around\nbefore they turn fully red.", Color.LIGHT_GRAY));
        mainTable.row();
        mainTable.add(label("If you don't and one turns red,\nyou will get a strike.", Color.LIGHT_GRAY));
        mainTable.row();
        mainTable.add(label("5 strikes and you are out!", Color.RED));
        mainTable.getColor().a = 0f;
        mainTable.addAction(fadeIn(1.75f));


        this.stage.addActor(mainTable);
    }

    private Label label(String text, Color color) {
        Label label = new Label(text, styles.skin);
        label.setAlignment(Align.center, Align.center);
        label.setColor(color);
        return label;
    }

}
