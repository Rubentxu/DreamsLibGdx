package com.rubentxu.juegos.core.pantallas;


import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.inputs.MobileInput;
import com.rubentxu.juegos.core.modelo.Level;
import com.rubentxu.juegos.core.servicios.Assets;
import com.rubentxu.juegos.core.utils.gui.mtx.ButtonLevel;

import java.util.List;

public class SelectLevelScreen extends BaseScreen {

    public SelectLevelScreen(DreamsGame game) {
        super(game, new Stage(0, 0, true));
        CURRENT_SCREEN=SCREEN.LEVELSCREEN;
    }

    private Label label(String text, Color color, boolean scale) {
        Label label = new Label(text, styles.skin, "header", color);
        if (scale == true) label.setFontScale(2);
        label.setAlignment(Align.center, Align.center);
        return label;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        final List<Level> levels = game.getLevelManager().getLevels();
        for (int i = 0; i < levels.size(); i++){

            final ButtonLevel levelButton =new ButtonLevel(styles.skin.get("btnMenu",NinePatchDrawable.class),styles.skin.get("btnMenuPress",NinePatchDrawable.class));
            if(!levels.get(i).isActive()) {
                levelButton.setTextureLocked(((TextureAtlas) Assets.getInstance().get(Assets.getInstance().GUI_ATLAS)).findRegion("botonCandado"),true);
            }

            levelButton.setLevelNumber(i + 1, styles.font2);

            levelButton.setLevelStars(((TextureAtlas) Assets.getInstance().get(Assets.getInstance().GUI_ATLAS)).findRegion("estrellaZocalo")
                    , ((TextureAtlas) Assets.getInstance().get(Assets.getInstance().GUI_ATLAS)).findRegion("estrella"), 4, levels.get(i).getAchievements());

            levelButton.addListener(new ActorGestureListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                   game.getLevelManager().setCurrentLevel(levels.get(levelButton.getLevelNumber()));
                   game.gameScreen= new GameScreen(game);
                   game.setScreen(game.gameScreen,getTransition());
                }
            });

            if(i % 5 == 0){
                mainTable.row();
            }

            mainTable.add(levelButton).size(150, 150).pad(5, 5, 5, 5).expand();
        }

        mainTable.setFillParent(true);
        mainTable.row();
        mainTable.setBackground(new SpriteDrawable(new Sprite((Texture) assets.get(assets.DEBUG_BACKGROUND))));

        this.stage.addActor(mainTable);

    }

    @Override
    public InputProcessor getInputProcessor() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new MobileInput());
        return multiplexer;
    }

}
