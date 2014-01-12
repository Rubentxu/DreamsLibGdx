package com.rubentxu.juegos.core.servicios;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Styles {
        public BitmapFont font;
        public BitmapFont font2;
        public Skin skin;

    public Styles(Assets assets) {
        createStyles(assets);
    }

    private void createStyles(Assets assets) {
        font = assets.get(assets.DEFAULT_FONT);
        font2 = assets.get(assets.HEADER_FONT);
        skin = new Skin();
        skin.add("default", font);
        skin.add("header", font2);

        skin.add("lt-blue", new Color(.62f, .76f, .99f, 1f));
        skin.add("lt-green", new Color(.39f, .9f, .6f, 1f));
        skin.add("dark-blue", new Color(.79f, .95f, 91f, 1f));

        NinePatchDrawable btnMenu = new NinePatchDrawable(((TextureAtlas)assets.get(assets.GUI_ATLAS)).createPatch("btnMenu"));
        NinePatchDrawable btnMenuPress = new NinePatchDrawable(((TextureAtlas)assets.get(assets.GUI_ATLAS)).createPatch("btnMenuPress"));
        NinePatchDrawable buttonLeft = new NinePatchDrawable(((TextureAtlas)assets.get(assets.GUI_ATLAS)).createPatch("buttonLeft"));
        NinePatchDrawable buttonLeftPress = new NinePatchDrawable(((TextureAtlas)assets.get(assets.GUI_ATLAS)).createPatch("buttonLeftPress"));
        NinePatchDrawable buttonRight = new NinePatchDrawable(((TextureAtlas)assets.get(assets.GUI_ATLAS)).createPatch("buttonRight"));
        NinePatchDrawable buttonRightPress = new NinePatchDrawable(((TextureAtlas)assets.get(assets.GUI_ATLAS)).createPatch("buttonRightPress"));
        NinePatchDrawable buttonUp = new NinePatchDrawable(((TextureAtlas)assets.get(assets.GUI_ATLAS)).createPatch("buttonUp"));
        NinePatchDrawable buttonUpPress = new NinePatchDrawable(((TextureAtlas)assets.get(assets.GUI_ATLAS)).createPatch("buttonUpPress"));
        NinePatchDrawable debug = new NinePatchDrawable(((TextureAtlas)assets.get(assets.GUI_ATLAS)).createPatch("debug"));


        TextureRegionDrawable touchpad_background = new TextureRegionDrawable(((TextureAtlas) assets.get(assets.GUI_ATLAS)).findRegion("touchpad_background"));
        TextureRegionDrawable touchpad_thumb = new TextureRegionDrawable(((TextureAtlas) assets.get(assets.GUI_ATLAS)).findRegion("touchpad_thumb"));


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

        TextButtonStyle tbs = new TextButtonStyle(btnMenu, btnMenuPress, btnMenu, font);
        tbs.fontColor = skin.getColor("dark-blue");
        tbs.pressedOffsetX = Math.round(1f * Gdx.graphics.getDensity());
        tbs.pressedOffsetY = tbs.pressedOffsetX * -1f;

        ImageButton.ImageButtonStyle ImageButtonLeft = new ImageButton.ImageButtonStyle(buttonLeft, buttonLeftPress, buttonLeft,null,null,null);
        ImageButton.ImageButtonStyle ImageButtonRight = new ImageButton.ImageButtonStyle(buttonRight, buttonRightPress, buttonRight,null,null,null);
        ImageButton.ImageButtonStyle ImageButtonUp = new ImageButton.ImageButtonStyle(buttonUp, buttonUpPress, buttonUp,null,null,null);


        Touchpad.TouchpadStyle touchpadStyle= new Touchpad.TouchpadStyle();
        touchpadStyle.background=touchpad_background;
        touchpadStyle.knob=touchpad_thumb;

        skin.add("default", tbs);
        skin.add("buttonLeft", ImageButtonLeft);
        skin.add("buttonRight", ImageButtonRight);
        skin.add("buttonUp", ImageButtonUp);
        skin.add("default", touchpadStyle);
    }
}