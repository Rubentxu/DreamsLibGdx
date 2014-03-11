package com.rubentxu.juegos.core.utils.gui.mtx;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class TextGui extends AbstractActorLight {
    private String text = "";
    private BitmapFont bitMapFont;
    //
    private float originalPosY;

    public TextGui(BitmapFont bitMapFont, float width, float height,
                boolean DIPActive) {
        super(width, height, DIPActive);
        this.bitMapFont = bitMapFont;
        //
        if (DIPActive) {
            bitMapFont.setScale(AppSettings.getWorldSizeRatio());
        }
    }

    public void setBitMapFont(BitmapFont bitMapFont, boolean DIPActive) {
        this.bitMapFont = bitMapFont;
        //
        if (DIPActive) {
            bitMapFont.setScale(AppSettings.getWorldSizeRatio());
        }
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        //
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