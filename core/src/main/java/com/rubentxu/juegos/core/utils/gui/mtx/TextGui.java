package com.rubentxu.juegos.core.utils.gui.mtx;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class TextGui extends AbstractActorLight {
    private String text = "";
    private BitmapFont bitMapFont;
    private float originalPosY;

    public TextGui(BitmapFont bitMapFont, float width, float height) {
        super(width, height);
        this.bitMapFont = bitMapFont;
        bitMapFont.setScale(AppSettings.getSizeRatio());
    }

    public void setBitMapFont(BitmapFont bitMapFont) {
        this.bitMapFont = bitMapFont;
        bitMapFont.setScale(AppSettings.getSizeRatio());

    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        drawText(batch);
    }

    private void drawText(SpriteBatch batch) {
        if (bitMapFont != null) {
            bitMapFont.draw(batch, text, getX(), getY());
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BitmapFont getBitMapFont() {
        return bitMapFont;
    }

    public float getOriginalPosY() {
        return originalPosY;
    }

    public void setOriginalPosY(float originalPosY) {
        this.originalPosY = originalPosY;
    }
}