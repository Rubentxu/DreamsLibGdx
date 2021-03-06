package com.rubentxu.juegos.core.pantallas;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.constantes.GameState;
import com.rubentxu.juegos.core.managers.game.ResourcesManager;
import com.rubentxu.juegos.core.modelo.Level;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransition;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransitionSlide;
import com.rubentxu.juegos.core.utils.gui.ScaleUtil;
import com.rubentxu.juegos.core.utils.gui.mtx.ButtonLevel;

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
        Skin skin = game.getResourcesManager().getStyles().skin;
        game.getProfileManager().getProfile().resetValues();
        super.resize(width, height);
        final List<Level> levels = game.getLevelManager().getLevels();
        int size = (int) (350 * ScaleUtil.getSizeRatio());
        int pad = (int) (30 * ScaleUtil.getSizeRatio());
        for (int i = 0; i < levels.size(); i++) {

            final ButtonLevel levelButton = new ButtonLevel(game.getResourcesManager());
            if (!levels.get(i).isActive()) {
                levelButton.setTextureLocked(skin.getRegion("gui_candado"), true);
            }

            levelButton.setLevelNumber(levels.get(i).getNum(), game.getResourcesManager().getStyles().font2);

            levelButton.setLevelStars(((TextureAtlas) game.getResourcesManager().get(ResourcesManager.GUI_ATLAS)).findRegion("estrellaZocalo"),
                    skin.getRegion("gui_estrella"), 5, levels.get(i).getAchievements(), size);

            levelButton.addListener(new ActorGestureListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    Gdx.app.log(Constants.LOG, "Numero de boton presionado: " + levelButton.getLevelNumber());
                    game.getLevelManager().setCurrentLevel(levels.get(levelButton.getLevelNumber() - 1));
                    game.setGameState(GameState.GAME_SHOW_GAME);
                }
            });

            if (i % 5 == 0) {
                mainTable.row();
            }

            mainTable.add(levelButton).size(size, size).pad(pad).expand();
        }

        mainTable.setFillParent(true);
        mainTable.row();
        mainTable.setBackground(skin.getDrawable("debug"));

        this.stage.addActor(mainTable);

    }

    public ScreenTransition getTransition() {
        return ScreenTransitionSlide.init(0.7f,
                ScreenTransitionSlide.LEFT, true, Interpolation.swingOut);
    }


}
