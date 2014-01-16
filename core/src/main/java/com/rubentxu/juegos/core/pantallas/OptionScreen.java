package com.rubentxu.juegos.core.pantallas;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

public class OptionScreen extends BaseScreen {

    private Label volumeValue;

    public OptionScreen(DreamsGame game) {
        super(game, new Stage(0, 0, true));
        CURRENT_SCREEN = SCREEN.OPTIONS;
    }

    private Label label(String text, Color color, boolean scale) {
        Label label = new Label(text, styles.skin, "header", color);
        if (scale == true) label.setFontScale(1.5f);
        label.setAlignment(Align.center, Align.center);
        return label;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Gdx.input.setInputProcessor(stage);

        mainTable.setFillParent(true);
        mainTable.defaults().pad(16f);
        mainTable.add(label("Opciones del Juego", Color.CYAN, true));
        mainTable.row();
        mainTable.setBackground(new SpriteDrawable(new Sprite((Texture) assets.get(assets.MENU_BACKGROUND))));

        final CheckBox musicCheckbox = new CheckBox(" Music", styles.skin);
        musicCheckbox.align(Align.left);
        musicCheckbox.setChecked(getGame().getPreferencesManager().isMusicEnabled());
        musicCheckbox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean enabled = musicCheckbox.isChecked();
                getGame().getPreferencesManager().setMusicEnabled(enabled);
            }
        });
        mainTable.add(musicCheckbox);
        mainTable.row();

        final CheckBox touchPadCheckbox = new CheckBox(" TouchPad Control", styles.skin);
        touchPadCheckbox.align(Align.left);
        touchPadCheckbox.setChecked(getGame().getPreferencesManager().isTouchPadEnabled());
        touchPadCheckbox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean enabled = touchPadCheckbox.isChecked();
                getGame().getPreferencesManager().setTouchPadEnabled(enabled);
            }
        });
        mainTable.add(touchPadCheckbox);
        mainTable.row();

        Slider volumeSlider = new Slider(0f, 1f, 0.1f, false, styles.skin);
        volumeSlider.setValue(getGame().getPreferencesManager().getVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Slider slider = (Slider) actor;
                float value = slider.getValue();
                getGame().getPreferencesManager().setVolume(value);
                updateVolumeLabel();
            }

        });
        mainTable.add(volumeSlider);
        mainTable.row();
        volumeValue = new Label(" Volume ", styles.skin);
        updateVolumeLabel();
        mainTable.add(volumeValue);
        mainTable.row();

        TextButton backButton = new TextButton("Volver Menu", styles.skin);
        backButton.pad(20);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(DreamsGame.menuScreen);
            }
        });

        mainTable.add(backButton);

        this.stage.addActor(mainTable);

    }

    private void updateVolumeLabel() {
        float volume = (getGame().getPreferencesManager().getVolume() * 100);
        volumeValue.setText(Float.toString(volume));
    }

}
