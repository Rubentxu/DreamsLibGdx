package com.rubentxu.juegos.core.utils.gui.mtx;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuCreator {
    public static Table createTable(boolean fillParent, Skin skin) {
        Table table = new Table(skin);
        table.setFillParent(fillParent);
        return table;
    }

    public static ButtonGame createCustomGameButton(BitmapFont bitmapFont,
                                                    TextureRegion up, TextureRegion down, float width, float height,
                                                    boolean DIPActive) {
        Drawable dUp = new TextureRegionDrawable(up);
        Drawable dDown = new TextureRegionDrawable(down);
        return new ButtonGame(bitmapFont, dUp, dDown, width, height, true);
    }

    public static ButtonGame createCustomGameButton(BitmapFont bitmapFont,
                                                    TextureRegion up, TextureRegion down) {
        Drawable dUp = new TextureRegionDrawable(up);
        Drawable dDown = new TextureRegionDrawable(down);
        return new ButtonGame(bitmapFont, dUp, dDown);
    }

    public static ButtonToggle createCustomToggleButton(BitmapFont bitmapFont,
                                                        TextureRegion tOn, TextureRegion tOff, boolean isToggleActive) {
        Drawable dUp = new TextureRegionDrawable(tOn);
        Drawable dDown = new TextureRegionDrawable(tOff);
        return new ButtonToggle(bitmapFont, dUp, dDown, tOn, tOff,
                isToggleActive);
    }

    public static ButtonToggle createCustomToggleButton(BitmapFont bitmapFont,
                                                        TextureRegion tOn, TextureRegion tOff, boolean isToggleActive,
                                                        float width, float height, boolean DIPActive) {
        Drawable dUp = new TextureRegionDrawable(tOn);
        Drawable dDown = new TextureRegionDrawable(tOff);
        return new ButtonToggle(bitmapFont, dUp, dDown, tOn, tOff,
                isToggleActive, width, height, DIPActive);
    }

    public static ButtonSlider createCustomSlider(TextureRegion tBackground,
                                                  TextureRegion tKnob, boolean isVertical, float min, float max,
                                                  float stepSize, float width, float height, boolean DIPActive) {

        Drawable dBg = new TextureRegionDrawable(tBackground);
        Drawable dKnob = new TextureRegionDrawable(tKnob);
        SliderStyle sliderStyle = new SliderStyle(dBg, dKnob);
        return new ButtonSlider(min, max, stepSize, isVertical, sliderStyle,
                width, height, DIPActive);
    }

}