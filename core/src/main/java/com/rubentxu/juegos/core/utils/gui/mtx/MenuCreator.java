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
        Table table = new TableModel(skin);
        table.setFillParent(fillParent);
        return table;
    }

    public static TableModel createTable(TextureRegion textureBackground, float width,float height) {
        TableModel table = new TableModel(textureBackground, width,height);
        return table;
    }

    public static ButtonGame createCustomGameButton(BitmapFont bitmapFont,Drawable up,Drawable down,float width,float height) {
        return new ButtonGame(bitmapFont, up, down, width, height);
    }

    public static ButtonToggle createCustomToggleButton(BitmapFont bitmapFont, TextureRegion tOn, TextureRegion tOff,
                                                        boolean isToggleActive) {
        Drawable dUp = new TextureRegionDrawable(tOn);
        Drawable dDown = new TextureRegionDrawable(tOff);
        return new ButtonToggle(bitmapFont, dUp, dDown, tOn, tOff, isToggleActive);
    }

    public static ButtonToggle createCustomToggleButton(BitmapFont bitmapFont,TextureRegion tOn, TextureRegion tOff,
                                              boolean isToggleActive, float width, float height) {
        Drawable dUp = new TextureRegionDrawable(tOn);
        Drawable dDown = new TextureRegionDrawable(tOff);
        return new ButtonToggle(bitmapFont, dUp, dDown, tOn, tOff, isToggleActive, width, height);
    }

    public static ButtonSlider createCustomSlider(TextureRegion tBackground,
                                                  TextureRegion tKnob, boolean isVertical, float min, float max,
                                                  float stepSize, float width, float height) {

        Drawable dBg = new TextureRegionDrawable(tBackground);
        Drawable dKnob = new TextureRegionDrawable(tKnob);
        SliderStyle sliderStyle = new SliderStyle(dBg, dKnob);
        return new ButtonSlider(min, max, stepSize, isVertical, sliderStyle,width, height);
    }

}