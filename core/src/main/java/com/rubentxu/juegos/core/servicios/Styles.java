package com.rubentxu.juegos.core.servicios;

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
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.rubentxu.juegos.core.managers.game.ResourcesManager;
<<<<<<< HEAD
import com.rubentxu.juegos.core.utils.gui.ScaleUtil;
=======
>>>>>>> master

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
<<<<<<< HEAD
            font.setScale(ScaleUtil.getSizeRatio());
            font.setUseIntegerPositions(false);
            font2 = resourcesManager.get(resourcesManager.HEADER_FONT);
            font2.setScale(ScaleUtil.getSizeRatio());
=======
            //font.setScale(AppSettings.getSizeRatio());
            font.setUseIntegerPositions(false);
            font2 = resourcesManager.get(resourcesManager.HEADER_FONT);
            //font2.setScale(AppSettings.getSizeRatio());
>>>>>>> master
            font2.setUseIntegerPositions(false);
            skin = new Skin();
            skin.add("default", font);
            skin.add("header", font2);

            skin.add("lt-blue", new Color(.62f, .76f, .99f, 1f));
            skin.add("lt-green", new Color(.39f, .9f, .6f, 1f));
            skin.add("dark-blue", new Color(.79f, .95f, 91f, 1f));

            NinePatchDrawable btnMenu = new NinePatchDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.GUI_ATLAS)).createPatch("btnMenu"));
            NinePatchDrawable btnMenuPress = new NinePatchDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.GUI_ATLAS)).createPatch("btnMenuPress"));
            NinePatchDrawable buttonLeft = new NinePatchDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.GUI_ATLAS)).createPatch("buttonLeft"));
            NinePatchDrawable buttonLeftPress = new NinePatchDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.GUI_ATLAS)).createPatch("buttonLeftPress"));
            NinePatchDrawable buttonRight = new NinePatchDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.GUI_ATLAS)).createPatch("buttonRight"));
            NinePatchDrawable buttonRightPress = new NinePatchDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.GUI_ATLAS)).createPatch("buttonRightPress"));
            NinePatchDrawable buttonUp = new NinePatchDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.GUI_ATLAS)).createPatch("buttonUp"));
            NinePatchDrawable buttonUpPress = new NinePatchDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.GUI_ATLAS)).createPatch("buttonUpPress"));
            NinePatchDrawable debug = new NinePatchDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.GUI_ATLAS)).createPatch("debug"));


            TextureRegionDrawable touchpad_background = new TextureRegionDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.GUI_ATLAS)).findRegion("touchpad_background"));
            TextureRegionDrawable touchpad_thumb = new TextureRegionDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.GUI_ATLAS)).findRegion("touchpad_thumb"));


            TextureRegionDrawable checkox_true = new TextureRegionDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.UISKIN_ATLAS)).findRegion("check-on"));

            TextureRegionDrawable checkox_false = new TextureRegionDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.UISKIN_ATLAS)).findRegion("check-off"));

            TextureRegionDrawable slider_knob = new TextureRegionDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.UISKIN_ATLAS)).findRegion("default-slider-knob"));
            TextureRegionDrawable slider = new TextureRegionDrawable(((TextureAtlas) resourcesManager.get(resourcesManager.UISKIN_ATLAS)).findRegion("default-slider"));

            CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle(checkox_false, checkox_true, font, Color.WHITE);


            SpriteDrawable stats = new SpriteDrawable(new Sprite((Texture) resourcesManager.get(resourcesManager.STATS_BACKGROUND)));


            SliderStyle sliderStyle = new SliderStyle(slider, slider_knob);
            skin.add("default",new WindowStyle(font2,Color.LIGHT_GRAY,debug));

            skin.add("stats", stats);
            skin.add("btnMenu", btnMenu);
            skin.add("btnMenuPress", btnMenuPress);
            skin.add("buttonLeft", buttonLeft);
            skin.add("buttonLeftPress", buttonLeftPress);
            skin.add("buttonRight", buttonRight);
            skin.add("buttonRightPress", buttonRightPress);
            skin.add("buttonUp", buttonUp);
            skin.add("buttonUpPress", buttonUpPress);
            skin.add("debug", debug);

            LabelStyle lbs = new LabelStyle();
            lbs.font = font;
            lbs.fontColor = Color.WHITE;
            skin.add("default", lbs);

            LabelStyle lbsHeader = new LabelStyle();
            lbsHeader.font = font2;
            lbsHeader.fontColor = Color.WHITE;
            skin.add("header", lbsHeader);

            TextButtonStyle tbs = new TextButtonStyle(btnMenu, btnMenuPress, btnMenu, font);
            tbs.fontColor = skin.getColor("dark-blue");
            tbs.pressedOffsetX = Math.round(1f * Gdx.graphics.getDensity());
            tbs.pressedOffsetY = tbs.pressedOffsetX * -1f;

            ImageButton.ImageButtonStyle ImageButtonLeft = new ImageButton.ImageButtonStyle(buttonLeft, buttonLeftPress, buttonLeft, null, null, null);
            ImageButton.ImageButtonStyle ImageButtonRight = new ImageButton.ImageButtonStyle(buttonRight, buttonRightPress, buttonRight, null, null, null);
            ImageButton.ImageButtonStyle ImageButtonUp = new ImageButton.ImageButtonStyle(buttonUp, buttonUpPress, buttonUp, null, null, null);


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