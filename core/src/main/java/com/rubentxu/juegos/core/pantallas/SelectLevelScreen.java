package com.rubentxu.juegos.core.pantallas;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.inputs.MobileInput;
import com.rubentxu.juegos.core.managers.game.ResourcesManager;
import com.rubentxu.juegos.core.modelo.Level;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransition;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransitionSlide;
import com.rubentxu.juegos.core.utils.gui.mtx.AppSettings;
import com.rubentxu.juegos.core.utils.gui.mtx.ButtonLevel;
import com.rubentxu.juegos.core.utils.gui.mtx.MenuCreator;

import java.util.List;

public class SelectLevelScreen extends BaseScreen {

    public SelectLevelScreen(DreamsGame game) {
        super(game, new Stage(0, 0, true));
        CURRENT_SCREEN = SCREEN.LEVELSCREEN;
    }

    private Label label(String text, Color color) {
        Label label = new Label(text, game.getResourcesManager().getStyles().skin, "header", color);
        label.setAlignment(Align.center, Align.center);
        return label;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        setUpLevelsScreen();
       /* final List<Level> levels = game.getLevelManager().getLevels();
        for (int i = 0; i < levels.size(); i++){

            final ButtonLevel levelButton =new ButtonLevel(game.getResourcesManager());
            if(!levels.get(i).isActive()) {
                levelButton.setTextureLocked(((TextureAtlas) game.getResourcesManager().get(ResourcesManager.GUI_ATLAS)).findRegion("botonCandado"),true);
            }

            levelButton.setLevelNumber(i + 1, game.getResourcesManager().getStyles().font2);

            levelButton.setLevelStars(((TextureAtlas) game.getResourcesManager().get(ResourcesManager.GUI_ATLAS)).findRegion("estrellaZocalo")
                    , ((TextureAtlas) game.getResourcesManager().get(ResourcesManager.GUI_ATLAS)).findRegion("estrella"), 4, levels.get(i).getAchievements());

            levelButton.addListener(new ActorGestureListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    Gdx.app.log(Constants.LOG,"Numero de boton presionado: "+ levelButton.getLevelNumber());
                   game.getLevelManager().setCurrentLevel(levels.get(levelButton.getLevelNumber()-1));
                   game.gameScreen= new GameScreen(game);
                   game.setScreen(game.gameScreen,game.gameScreen.getTransition());
                }
            });

            if(i % 5 == 0){
                mainTable.row();
            }

            mainTable.add(levelButton).size(150, 150).pad(5, 5, 5, 5).expand();
        }

        mainTable.setFillParent(true);
        mainTable.row();
        mainTable.setBackground(new SpriteDrawable(new Sprite((Texture) game.getResourcesManager().get(ResourcesManager.DEBUG_BACKGROUND))));

        this.stage.addActor(mainTable);
*/
    }

    private void setUpLevelsScreen() {

        mainTable = MenuCreator.createTable(true, getGame().getResourcesManager().getStyles().skin);
        mainTable.setBackground(new NinePatchDrawable(((TextureAtlas) game.getResourcesManager().get(ResourcesManager.GUI_PACK_ATLAS)).createPatch("gui_bloque_vacio")));
        stage.addActor(mainTable);
        final List<Level> levels = game.getLevelManager().getLevels();
        for (int i = 0; i < levels.size(); i++) {

            Drawable dUp = new NinePatchDrawable(game.getResourcesManager().getStyles().skin.get("btnMenu", NinePatchDrawable.class));
            Drawable dDown = new NinePatchDrawable(game.getResourcesManager().getStyles().skin.get("btnMenuPress", NinePatchDrawable.class));
            final ButtonLevel levelButton = new ButtonLevel(game.getResourcesManager().getStyles().font, dUp, dDown);
            if (!levels.get(i).isActive()) {
                levelButton.setTextureLocked(((TextureAtlas) game.getResourcesManager().get(ResourcesManager.GUI_PACK_ATLAS)).findRegion("gui_candado"), true);
            }

            levelButton.setLevelNumber(i + 1, game.getResourcesManager().getStyles().font2);

            levelButton.setLevelStars(((TextureAtlas) game.getResourcesManager().get(ResourcesManager.GUI_ATLAS)).findRegion("estrellaZocalo")
                    , ((TextureAtlas) game.getResourcesManager().get(ResourcesManager.GUI_PACK_ATLAS)).findRegion("gui_estrella"), 4, levels.get(i).getAchievements());


            levelButton.addListener(new ActorGestureListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    Gdx.app.log(Constants.LOG, "Numero de boton presionado: " + levelButton.getLevelNumber());
                    game.getLevelManager().setCurrentLevel(levels.get(levelButton.getLevelNumber() - 1));
                    game.gameScreen = new GameScreen(game);
                    game.setScreen(game.gameScreen, game.gameScreen.getTransition());
                }
            });

            if (i % 5 == 0) {
                mainTable.row();
            }
            mainTable.add(levelButton).size(150 * AppSettings.getSizeRatio(), 150 * AppSettings.getSizeRatio()).pad(5 * AppSettings.getSizeRatio()).expand();
        }
    }

    public ScreenTransition getTransition() {
        return ScreenTransitionSlide.init(0.7f,
                ScreenTransitionSlide.LEFT, true, Interpolation.swingOut);
    }

    @Override
    public InputProcessor getInputProcessor() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new MobileInput());
        return multiplexer;
    }

}
