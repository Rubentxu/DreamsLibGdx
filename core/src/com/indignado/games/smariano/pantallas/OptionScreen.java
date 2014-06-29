package com.indignado.games.smariano.pantallas;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.indignado.games.smariano.constantes.GameState;
import com.indignado.games.smariano.managers.game.ResourcesManager;
import com.indignado.games.smariano.pantallas.transiciones.ScreenTransition;
import com.indignado.games.smariano.pantallas.transiciones.ScreenTransitionSlide;
import com.indignado.games.smariano.utils.gui.ScaleUtil;

public class OptionScreen extends BaseScreen {

    private Label volumeValueSound;
    private Label volumeValueMusic;

    public OptionScreen() {
        CURRENT_SCREEN = SCREEN.OPTIONS;
    }


    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        game.getPreferencesManager().load();
        Label label = new Label("Opciones del Juego", game.getResourcesManager().getStyles().skin, "header", Color.ORANGE);
        label.setAlignment(Align.center, Align.center);
        mainTable.setFillParent(true);
        mainTable.defaults().pad(6 * ScaleUtil.getSizeRatio());
        mainTable.defaults().padLeft(50 * ScaleUtil.getSizeRatio());
        mainTable.add(label).colspan(3);
        mainTable.row();
        mainTable.setBackground(new SpriteDrawable(new Sprite((Texture) game.getResourcesManager().get(ResourcesManager.MENU_BACKGROUND))));

        final CheckBox musicCheckbox = new CheckBox(" Music", game.getResourcesManager().getStyles().skin);
        musicCheckbox.align(Align.left);
        musicCheckbox.setChecked(game.getPreferencesManager().music);
        musicCheckbox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean enabled = musicCheckbox.isChecked();
                game.getPreferencesManager().music = enabled;
                game.getAudioManager().playMusic(ResourcesManager.MUSIC_MENU);
            }
        });
        mainTable.add(musicCheckbox);


        Slider volumeSliderMusic = new Slider(0f, 1f, 0.1f, false, game.getResourcesManager().getStyles().skin);
        volumeSliderMusic.setValue(game.getPreferencesManager().volMusic);
        volumeSliderMusic.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Slider slider = (Slider) actor;
                float value = slider.getValue();
                game.getPreferencesManager().volMusic = value;
                updateVolumeLabelMusic();
            }

        });
        mainTable.add(volumeSliderMusic);

        volumeValueMusic = new Label(" Volume ", game.getResourcesManager().getStyles().skin);
        updateVolumeLabelMusic();
        mainTable.add(volumeValueMusic);
        mainTable.row();


        final CheckBox soundCheckbox = new CheckBox(" Sound", game.getResourcesManager().getStyles().skin);
        soundCheckbox.align(Align.left);
        soundCheckbox.setChecked(game.getPreferencesManager().sound);
        soundCheckbox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean enabled = soundCheckbox.isChecked();
                game.getPreferencesManager().sound = enabled;

            }
        });
        mainTable.add(soundCheckbox);


        Slider volumeSliderSound = new Slider(0f, 1f, 0.1f, false, game.getResourcesManager().getStyles().skin);
        volumeSliderSound.setValue(game.getPreferencesManager().volSound);
        volumeSliderSound.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Slider slider = (Slider) actor;
                float value = slider.getValue();
                game.getPreferencesManager().volSound = value;
                updateVolumeLabelSound();
            }

        });
        mainTable.add(volumeSliderSound);

        volumeValueSound = new Label(" Volume ", game.getResourcesManager().getStyles().skin);
        updateVolumeLabelSound();
        mainTable.add(volumeValueSound);
        mainTable.row();

        final CheckBox touchPadCheckbox = new CheckBox(" TouchPad Control", game.getResourcesManager().getStyles().skin);
        touchPadCheckbox.align(Align.left);
        touchPadCheckbox.setChecked(game.getPreferencesManager().touchPadEnabled);
        touchPadCheckbox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean enabled = touchPadCheckbox.isChecked();
                game.getPreferencesManager().touchPadEnabled = enabled;
            }
        });
        mainTable.add(touchPadCheckbox).colspan(3);
        mainTable.row();


        TextButton backButton = new TextButton("Volver Menu", game.getResourcesManager().getStyles().skin);
        backButton.pad(20);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getPreferencesManager().save();
                game.setGameState(GameState.GAME_SHOW_MENU);
            }
        });

        mainTable.add(backButton).colspan(3);

        this.stage.addActor(mainTable);

    }


    public ScreenTransition getTransition() {
        return ScreenTransitionSlide.init(0.7f,
                ScreenTransitionSlide.DOWN, true, Interpolation.swingOut);
    }

    private void updateVolumeLabelMusic() {
        int volume = (int) (game.getPreferencesManager().volMusic * 100);
        volumeValueMusic.setText("Volume " + Integer.toString(volume));
    }

    private void updateVolumeLabelSound() {
        int volume = (int) (game.getPreferencesManager().volSound * 100);
        volumeValueSound.setText("Volume " + Integer.toString(volume));
    }

}
