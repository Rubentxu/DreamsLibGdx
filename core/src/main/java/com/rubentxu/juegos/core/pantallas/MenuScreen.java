package com.rubentxu.juegos.core.pantallas;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.inputs.MobileInput;
import com.rubentxu.juegos.core.managers.game.ResourcesManager;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransition;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransitionSlide;
import com.rubentxu.juegos.core.utils.gui.mtx.AppSettings;
import com.rubentxu.juegos.core.utils.gui.mtx.ButtonGame;
import com.rubentxu.juegos.core.utils.gui.mtx.EffectCreator;
import com.rubentxu.juegos.core.utils.gui.mtx.MenuCreator;
import com.rubentxu.juegos.core.utils.gui.mtx.TableModel;
import com.rubentxu.juegos.core.utils.gui.mtx.TextGui;

public class MenuScreen extends BaseScreen {

    public MenuScreen(DreamsGame game) {
        super(game, new Stage(0, 0, true));
        CURRENT_SCREEN=SCREEN.MENU;
    }

    private Label label(String text, Color color) {
        Label label = new Label(text, game.getResourcesManager().getStyles().skin, "header", color);

        label.setAlignment(Align.center, Align.center);
        return label;
    }

    @Override
    public void show() {
        super.show();
        Gdx.app.log(Constants.LOG, "Showing screen: " + getName() + " Current_Screen " + CURRENT_SCREEN);
        mainTable=new TableModel(new TextureRegion( game.getResourcesManager().get(ResourcesManager.MENU_BACKGROUND,Texture.class)),Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),true);
        mainTable.setPosition(0,-240 * AppSettings.getWorldPositionYRatio());
        stage.addActor(mainTable);

        float centerScreenX=Gdx.graphics.getWidth()/2;
        float centerScreenY=Gdx.graphics.getHeight()/2;
        float btnWidth = 470f;
        float btnHeight = 180f;
        ResourcesManager resourcesManager = game.getResourcesManager();

        TextGui titulo= new TextGui(resourcesManager.get(ResourcesManager.HEADER_FONT, BitmapFont.class), width, height,true);
        titulo.setPosition(centerScreenX,centerScreenY);
        titulo.setText("SUPER MARIANO");
        stage.addActor(titulo);

        // Botones Menu
        final ButtonGame btnStart = MenuCreator.createCustomGameButton(resourcesManager.get(ResourcesManager.DEFAULT_FONT, BitmapFont.class),
                resourcesManager.getStyles().skin.get("btnMenu", NinePatchDrawable.class), resourcesManager.getStyles()
                .skin.get("btnMenuPress", NinePatchDrawable.class),btnWidth,btnHeight,true) ;
        btnStart.setText("Comenzar",true);
        btnStart.setPosition(centerScreenX, centerScreenY);
        btnStart.setTextPosXY(20,50);
        btnStart.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click Comenzar...");
                EffectCreator.create_SC_SHK_BTN(btnStart,1.3f, 1.3f, 5f, 0, 0.05f, null, false);
                game.gameScreen=null;
                game.levelScreen=new SelectLevelScreen(game);
                game.setScreen(game.levelScreen,game.levelScreen.getTransition());

            }
        });
        stage.addActor(btnStart);

        final ButtonGame btnOptions = MenuCreator.createCustomGameButton(resourcesManager.get(ResourcesManager.DEFAULT_FONT, BitmapFont.class),
                resourcesManager.getStyles().skin.get("btnMenu", NinePatchDrawable.class), resourcesManager.getStyles()
                .skin.get("btnMenuPress", NinePatchDrawable.class),btnWidth,btnHeight,true) ;
        btnOptions.setText("Comenzar",true);
        btnOptions.setPosition(centerScreenX, centerScreenY);
        btnOptions.setTextPosXY(20,50);
        btnOptions.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                EffectCreator.create_SC_SHK_BTN(btnOptions,1.3f, 1.3f, 5f, 0, 0.05f, null, false);
                System.out.println("Click optionScreen...");
                game.optionScreen= new OptionScreen(game);
                game.setScreen(game.optionScreen,game.optionScreen.getTransition());

            }
        });
        stage.addActor(btnOptions);


        final ButtonGame btnScores = MenuCreator.createCustomGameButton(resourcesManager.get(ResourcesManager.DEFAULT_FONT, BitmapFont.class),
                resourcesManager.getStyles().skin.get("btnMenu", NinePatchDrawable.class), resourcesManager.getStyles()
                .skin.get("btnMenuPress", NinePatchDrawable.class),btnWidth,btnHeight,true) ;
        btnScores.setText("Comenzar",true);
        btnScores.setPosition(centerScreenX, centerScreenY);
        btnScores.setTextPosXY(20,50);
        btnScores.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                EffectCreator.create_SC_SHK_BTN(btnScores,1.3f, 1.3f, 5f, 0, 0.05f, null, false);
                System.out.println("Click highScoreScreen...");
                game.highScoreScreen= new HighScoresScreen(game);
                game.setScreen(game.highScoreScreen,game.highScoreScreen.getTransition());

            }
        });
        stage.addActor(btnScores);




    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

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
