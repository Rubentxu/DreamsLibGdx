package com.rubentxu.juegos.core.pantallas;


import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.inputs.MobileInput;
import com.rubentxu.juegos.core.managers.game.ResourcesManager;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransition;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransitionSlide;
import com.rubentxu.juegos.core.utils.gui.ScaleUtil;

public class MenuScreen extends BaseScreen {

    public MenuScreen(DreamsGame game) {
        super(game, new Stage(0, 0, true));
        CURRENT_SCREEN=SCREEN.MENU;
    }


    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        int pad= (int) (20* ScaleUtil.getSizeRatio());
        final TextButton btnStart = new TextButton("Comenzar", game.getResourcesManager().getStyles().skin);
        btnStart.pad(pad);
        final TextButton btnOptions = new TextButton("Opciones", game.getResourcesManager().getStyles().skin);
        btnOptions.pad(pad);
        final TextButton btnScores = new TextButton("Puntuaciones", game.getResourcesManager().getStyles().skin);
        btnScores.pad(pad);

        btnStart.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click Comenzar...");
                game.gameScreen=null;
                game.levelScreen=new SelectLevelScreen(game);
                game.setScreen(game.levelScreen,game.levelScreen.getTransition());
            }
        });

        btnOptions.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click optionScreen...");
                game.optionScreen= new OptionScreen(game);
                game.setScreen(game.optionScreen,game.optionScreen.getTransition());
            }
        });

        btnScores.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click highScoreScreen...");
                game.highScoreScreen= new HighScoresScreen(game);
                game.setScreen(game.highScoreScreen,game.highScoreScreen.getTransition());
            }
        });

        Label label = new Label("SUPER MARIANO", game.getResourcesManager().getStyles().skin, "header", Color.CYAN);
        label.setAlignment(Align.center, Align.center);
        mainTable.setFillParent(true);
        mainTable.defaults().padBottom(pad);
        mainTable.defaults().height(height / 4.5f - pad);
        mainTable.defaults().fillX();
        mainTable.add(label);
        mainTable.row();
        mainTable.add(btnStart);
        mainTable.row();
        mainTable.add(btnOptions);
        mainTable.row();
        mainTable.add(btnScores);
        mainTable.setBackground(new SpriteDrawable(new Sprite((Texture) game.getResourcesManager().get(ResourcesManager.MENU_BACKGROUND))));

        this.stage.addActor(mainTable);
        mainTable.debug();

    }

    public ScreenTransition getTransition() {
        return ScreenTransitionSlide.init(0.7f,
                ScreenTransitionSlide.RIGHT, true, Interpolation.swingOut);
    }

    @Override
    public InputProcessor getInputProcessor() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new MobileInput());
        return multiplexer;
    }

}