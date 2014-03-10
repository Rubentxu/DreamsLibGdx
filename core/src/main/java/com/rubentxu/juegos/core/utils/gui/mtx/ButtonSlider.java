package com.rubentxu.juegos.core.utils.gui.mtx;

import com.badlogic.gdx.scenes.scene2d.ui.Slider;

public class ButtonSlider extends Slider {

    public ButtonSlider(float min, float max, float stepSize, boolean vertical,
                        SliderStyle style, float width, float height, boolean DIPActive) {
        super(min, max, stepSize, vertical, style);
        //
        if (DIPActive) {
            setSize(width * AppSettings.getWorldSizeRatio(), height
                    * AppSettings.getWorldSizeRatio());
        }
    }
}