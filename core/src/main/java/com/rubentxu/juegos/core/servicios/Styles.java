package com.rubentxu.juegos.core.servicios;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class Styles {
        public BitmapFont font;
        public Skin skin;

    public Styles(Assets assets) {
        createStyles(assets);
    }

    private void createStyles(Assets assets) {
        font = assets.get("fonts/DreamOfMe-32.fnt");
        skin = new Skin();
        skin.add("default", font);

        skin.add("lt-blue", new Color(.62f, .76f, .99f, 1f));
        skin.add("lt-green", new Color(.39f, .9f, .6f, 1f));
        skin.add("dark-blue", new Color(.79f, .95f, 91f, 1f));

        NinePatchDrawable btn1up = new NinePatchDrawable(((TextureAtlas)assets.get("gui/tex.atlas")).createPatch("patchThick"));
        NinePatchDrawable btn1down = new NinePatchDrawable(((TextureAtlas)assets.get("gui/tex.atlas")).createPatch("patchThickDown"));
        NinePatch window1patch = ((TextureAtlas)assets.get("gui/tex.atlas")).createPatch("window1");
        skin.add("btn1up", btn1up);
        skin.add("btn1down", btn1down);
        skin.add("window1", window1patch);
        skin.add("white-pixel", ((TextureAtlas)assets.get("gui/tex.atlas")).findRegion("white-pixel"), TextureRegion.class);

        LabelStyle lbs = new LabelStyle();
        lbs.font = font;
        lbs.fontColor = Color.WHITE;
        skin.add("default", lbs);

        TextButtonStyle tbs = new TextButtonStyle(btn1up, btn1down, btn1down, font);
        tbs.fontColor = skin.getColor("dark-blue");
        tbs.pressedOffsetX = Math.round(1f * Gdx.graphics.getDensity());
        tbs.pressedOffsetY = tbs.pressedOffsetX * -1f;
        skin.add("default", tbs);
    }
}