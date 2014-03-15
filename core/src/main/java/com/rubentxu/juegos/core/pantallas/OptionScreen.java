package com.rubentxu.juegos.core.pantallas;


import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.inputs.MobileInput;
import com.rubentxu.juegos.core.managers.game.ResourcesManager;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransition;
import com.rubentxu.juegos.core.pantallas.transiciones.ScreenTransitionSlide;
import com.rubentxu.juegos.core.utils.gui.ScaleUtil;

public class OptionScreen extends BaseScreen {

    private Label volumeValueSound;
    private Label volumeValueMusic;

    public OptionScreen(DreamsGame game) {
        super(game, new Stage(0, 0, true));
        CURRENT_SCREEN = SCREEN.OPTIONS;
    }

    private Label label(String text, Color color) {
        Label label = new Label(text, game.getResourcesManager().getStyles().skin, "header", color);
        label.setAlignment(Align.center, Align.center);
        return label;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        game.getPreferencesManager().load();

        mainTable.defaults().height(height / 5f);
        mainTable.setFillParent(true);
        mainTable.defaults().pad(6*ScaleUtil.getSizeRatio());
        mainTable.defaults().padLeft(50*ScaleUtil.getSizeRatio());
        mainTable.add(label("Opciones del Juego", Color.CYAN)).colspan(3);
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

<<<<<<< HEAD
=======

>>>>>>> master
        TextButton backButton = new TextButton("Volver Menu", game.getResourcesManager().getStyles().skin);
        backButton.pad(50* ScaleUtil.getSizeRatio());
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getPreferencesManager().save();
                game.setScreen(game.menuScreen, game.menuScreen.getTransition());
            }
        });

        mainTable.add(backButton).colspan(3).fillX();

        this.stage.addActor(mainTable);

    }


    public ScreenTransition getTransition() {
        return ScreenTransitionSlide.init(0.7f,
                ScreenTransitionSlide.DOWN, true, Interpolation.swingOut);
    }


    @Override
    public InputProcessor getInputProcessor() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new MobileInput());
        return multiplexer;
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
