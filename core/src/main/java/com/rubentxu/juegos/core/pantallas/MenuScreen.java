package com.rubentxu.juegos.core.pantallas;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.inputs.MobileInput;
import com.rubentxu.juegos.core.managers.game.ResourcesManager;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransition;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransitionSlide;
<<<<<<< HEAD
import com.rubentxu.juegos.core.utils.gui.ScaleUtil;
=======
import com.rubentxu.juegos.core.utils.gui.mtx.ButtonGame;
import com.rubentxu.juegos.core.utils.gui.mtx.EffectCreator;
import com.rubentxu.juegos.core.utils.gui.mtx.MenuCreator;
import com.rubentxu.juegos.core.utils.gui.mtx.TableModel;
import com.rubentxu.juegos.core.utils.gui.mtx.TextGui;
>>>>>>> master

public class MenuScreen extends BaseScreen {

    public MenuScreen(DreamsGame game) {
        super(game, new Stage(0, 0, true));
<<<<<<< HEAD
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
=======
        CURRENT_SCREEN = SCREEN.MENU;
    }

    @Override
    public void show() {
        super.show();
        Gdx.app.log(Constants.LOG, "Showing screen: " + getName() + " Current_Screen " + CURRENT_SCREEN);
        createMenu();
    }
    
    private void createMenu(){
        ResourcesManager resourcesManager = game.getResourcesManager();
        float centerScreenX = Gdx.graphics.getWidth() / 2;
        float centerScreenY = Gdx.graphics.getHeight() / 2;
        float btnWidth = 470f;
        float btnHeight = 180f;

        mainTable =new Table();
        mainTable.setFillParent(true);
        mainTable.debug();
       // mainTable.addAction(Actions.moveTo(stage.getWidth() - 550, mainTable.getY(), 0.9f));

        TextGui titulo = new TextGui(resourcesManager.get(ResourcesManager.HEADER_FONT, BitmapFont.class), width, height);
        titulo.setPosition(centerScreenX, 40);
        titulo.setText("SUPER MARIANO");
        mainTable.add(titulo);
        mainTable.row();
>>>>>>> master

        final ButtonGame btnStart = MenuCreator.createCustomGameButton(resourcesManager.get(ResourcesManager.DEFAULT_FONT, BitmapFont.class),
                resourcesManager.getStyles().skin.get("btnMenu", NinePatchDrawable.class), resourcesManager.getStyles().skin.get("btnMenuPress",
                NinePatchDrawable.class), btnWidth, btnHeight);
        btnStart.setText("Comenzar", true);
        btnStart.setPosition(centerScreenX, centerScreenY);
        //btnStart.setTextPosXY(10, 20);
        btnStart.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click Comenzar...");
<<<<<<< HEAD
                game.gameScreen=null;
                game.levelScreen=new SelectLevelScreen(game);
                game.setScreen(game.levelScreen,game.levelScreen.getTransition());
=======
                EffectCreator.create_SC_SHK_BTN(btnStart, 1.3f, 1.3f, 5f, 0, 0.05f, null, false);
                game.gameScreen = null;
                game.levelScreen = new SelectLevelScreen(game);
                game.setScreen(game.levelScreen, game.levelScreen.getTransition());

>>>>>>> master
            }
        });

        mainTable.add(btnStart).pad(4);
        mainTable.row();

        final ButtonGame btnOptions = MenuCreator.createCustomGameButton(resourcesManager.get(ResourcesManager.DEFAULT_FONT, BitmapFont.class),
                resourcesManager.getStyles().skin.get("btnMenu", NinePatchDrawable.class), resourcesManager.getStyles().skin.get("btnMenuPress",
                NinePatchDrawable.class), btnWidth, btnHeight);
        btnOptions.setText("Opciones", true);
        btnOptions.setPosition(centerScreenX, centerScreenY);
        btnOptions.setTextPosXY(10, 0);
        btnOptions.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                EffectCreator.create_SC_SHK_BTN(btnOptions, 1.3f, 1.3f, 5f, 0, 0.05f, null, false);
                System.out.println("Click optionScreen...");
                game.optionScreen = new OptionScreen(game);
                game.setScreen(game.optionScreen, game.optionScreen.getTransition());

            }
        });

        mainTable.add(btnOptions).uniform().pad(4);
        mainTable.row();


        final ButtonGame btnScores = MenuCreator.createCustomGameButton(resourcesManager.get(ResourcesManager.DEFAULT_FONT, BitmapFont.class),
                resourcesManager.getStyles().skin.get("btnMenu", NinePatchDrawable.class), resourcesManager.getStyles().skin.get("btnMenuPress",
                NinePatchDrawable.class), btnWidth, btnHeight);
        btnScores.setText("Puntuación", true);
        btnScores.setPosition(centerScreenX, centerScreenY);
        btnScores.setTextPosXY(10, 0);
        btnScores.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                EffectCreator.create_SC_SHK_BTN(btnScores, 1.3f, 1.3f, 5f, 0, 0.05f, null, false);
                System.out.println("Click highScoreScreen...");
                game.highScoreScreen = new HighScoresScreen(game);
                game.setScreen(game.highScoreScreen, game.highScoreScreen.getTransition());

            }
        });

<<<<<<< HEAD
        Label label = new Label("SUPER MARIANO", game.getResourcesManager().getStyles().skin, "header",  Color.CYAN);
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
=======
        mainTable.add(btnScores).uniform().pad(4);

        stage.addActor(mainTable);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
>>>>>>> master

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
