package com.indignado.games.smariano.servicios;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.indignado.games.smariano.managers.game.ResourcesManager;
import com.indignado.games.smariano.utils.gui.ScaleUtil;

public class Styles implements Disposable {
    public BitmapFont font;
    public BitmapFont font2;
    public Skin skin;
    public boolean initialize = false;

    public Styles(ResourcesManager resourcesManager) {
        createStyles(resourcesManager);
    }

    public void createStyles(ResourcesManager resourcesManager) {
        if (!initialize) {
            initialize=true;
            font = resourcesManager.get(resourcesManager.DEFAULT_FONT);
            font.setScale(ScaleUtil.getSizeRatio());
            font.setUseIntegerPositions(false);
            font2 = resourcesManager.get(resourcesManager.HEADER_FONT);
            font2.setScale(ScaleUtil.getSizeRatio());
            font2.setUseIntegerPositions(false);
            skin = new Skin();
            skin.add("default", font);
            skin.add("header", font2);

            skin.add("lt-blue", new Color(.62f, .76f, .99f, 1f));
            skin.add("lt-green", new Color(.39f, .9f, .6f, 1f));
            skin.add("dark-blue", new Color(.79f, .95f, 91f, 1f));

            skin.addRegions(resourcesManager.<TextureAtlas>get(resourcesManager.GUI_ATLAS));
            skin.addRegions(resourcesManager.<TextureAtlas>get(resourcesManager.GUI_PACK_ATLAS));


            TextureRegionDrawable touchpad_background = new TextureRegionDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.GUI_ATLAS)).findRegion("touchpad_background"));
            TextureRegionDrawable touchpad_thumb = new TextureRegionDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.GUI_ATLAS)).findRegion("touchpad_thumb"));


            TextureRegionDrawable checkox_true = new TextureRegionDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.UISKIN_ATLAS)).findRegion("check-on"));

            TextureRegionDrawable checkox_false = new TextureRegionDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.UISKIN_ATLAS)).findRegion("check-off"));

            TextureRegionDrawable slider_knob = new TextureRegionDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.UISKIN_ATLAS)).findRegion("default-slider-knob"));
            TextureRegionDrawable slider = new TextureRegionDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.UISKIN_ATLAS)).findRegion("default-slider"));

            CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle(checkox_false, checkox_true, font, Color.WHITE);


            SpriteDrawable stats = new SpriteDrawable(new Sprite((Texture) resourcesManager.get(resourcesManager.STATS_BACKGROUND)));


            SliderStyle sliderStyle = new SliderStyle(slider, slider_knob);
            skin.add("default",new WindowStyle(font2, Color.ORANGE,skin.getDrawable("debug")));
            skin.add("stats", stats);


            LabelStyle lbs = new LabelStyle();
            lbs.font = font;
            lbs.fontColor = Color.WHITE;
            skin.add("default", lbs);

            LabelStyle lbsHeader = new LabelStyle();
            lbsHeader.font = font2;
            lbsHeader.fontColor = Color.WHITE;
            skin.add("header", lbsHeader);

            TextButtonStyle tbs = new TextButtonStyle(skin.getDrawable("btnMenu"), skin.getDrawable("btnMenuPress"), skin.getDrawable("btnMenu"), font);
            tbs.fontColor = skin.getColor("dark-blue");
            tbs.pressedOffsetX = Math.round(1f * Gdx.graphics.getDensity());
            tbs.pressedOffsetY = tbs.pressedOffsetX * -1f;

            ImageButton.ImageButtonStyle ImageButtonLeft = new ImageButton.ImageButtonStyle(skin.getDrawable("buttonLeft"), skin.getDrawable("buttonLeftPress"),
                    skin.getDrawable("buttonLeft"), null, null, null);
            ImageButton.ImageButtonStyle ImageButtonRight = new ImageButton.ImageButtonStyle(skin.getDrawable("buttonRight"), skin.getDrawable("buttonRightPress"),
                    skin.getDrawable("buttonRight"), null, null, null);
            ImageButton.ImageButtonStyle ImageButtonUp = new ImageButton.ImageButtonStyle(skin.getDrawable("buttonUp"), skin.getDrawable("buttonUpPress"),
                    skin.getDrawable("buttonUp"), null, null, null);


            Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
            touchpadStyle.background = touchpad_background;
            touchpadStyle.knob = touchpad_thumb;


            skin.add("default", tbs);
            skin.add("buttonLeft", ImageButtonLeft);
            skin.add("buttonRight", ImageButtonRight);
            skin.add("buttonUp", ImageButtonUp);
            skin.add("default", touchpadStyle);
            skin.add("default", checkBoxStyle);
            skin.add("default-horizontal", sliderStyle);
        }
    }

    @Override
    public void dispose() {
        skin.dispose();
        skin = null;
    }
}