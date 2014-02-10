package com.rubentxu.juegos.core.utils.gui.mtx;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public abstract class AbstractButton extends Button {
    // Locked (Not mandatory)
    protected boolean isLockActive = false;
    protected TextureRegion textureLocked;

    // Text (Not mandatory)
    protected boolean isTextActive = false;
    protected float textPosX = 0.0f;
    protected float textPosY = 0.0f;
    protected String text = "";
    protected BitmapFont bitMapFont;

    // External texture (Not mandatory)
    protected boolean isExternalTextureActive = false;
    protected float externalTexturePosX = 0.0f;
    protected float externalTexturePosY = 0.0f;
    protected float externalTextureSizeW = 50.0f;
    protected float externalTextureSizeH = 50.0f;
    protected TextureRegion textureExternal;

    //
    private boolean DIPActive = false;

    public AbstractButton(BitmapFont bitMapFont, Drawable up, Drawable down) {
        super(up, down);
        this.bitMapFont = bitMapFont;
    }

    public AbstractButton(BitmapFont bitMapFont, Drawable up, Drawable down,
                          float width, float height, boolean DIPActive) {
        super(up, down);
        this.bitMapFont = bitMapFont;
        this.DIPActive = DIPActive;
        //

    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        // return super.hit(x, y, touchable);

        if (!isLockActive) {
            // If not locked detect the inputs
            return super.hit(x, y, touchable);
        } else {
            // If locked do not detect any hit by returning NULL
            return null;
        }
    }

    /**
     * Get if the button lock active
     * */
    public boolean isLockActive() {
        return isLockActive;
    }

    /**
     * Set the lock, it overrides the hit method, so it wont detect hits, also
     * if it is active, lock texture will be drawn
     *
     * @see
     * */
    public void setLockActive(boolean isLockActive) {
        this.isLockActive = isLockActive;
    }

    /**
     * Get lock texture
     * */
    public TextureRegion getTextureLocked() {
        return textureLocked;
    }

    /**
     * Set lock texture
     *
     * @param textureLocked
     *            the lock texture to draw
     * @param isLockActive
     *            to enable lock or not
     * */
    public void setTextureLocked(TextureRegion textureLocked,
                                 boolean isLockActive) {
        this.textureLocked = textureLocked;
        this.isLockActive = isLockActive;
    }

    /**
     * Get if text active
     * */
    public boolean isTextActive() {
        return isTextActive;
    }

    /**
     * Set the text active to be drawn
     */
    public void setTextActive(boolean isTextActive) {
        this.isTextActive = isTextActive;
    }

    /**
     * Get text
     */
    public String getText() {
        return text;
    }

    /**
     * Set text (first initiation), later to change text use setTextChange
     *
     * @param text
     *            the text to be written
     * @param isTextActive
     *            to write/draw the text or not
     *
     */
    public void setText(String text, boolean isTextActive) {
        this.text = text;
        this.isTextActive = isTextActive;
    }

    /**
     * Set the new text
     */
    public void setTextChange(String newText) {
        this.text = newText;
    }

    /**
     * Set text position of text (it adds to original button positions' x and y)
     */
    public void setTextPosXY(float x, float y) {
        textPosX = x;
        textPosY = y;

        if (DIPActive) {
            textPosX = x;
            textPosY = y;
        }
    }

    /**
     * Get the font
     */
    public BitmapFont getBitMapFont() {
        return bitMapFont;
    }

    /**
     * Set the font
     */
    public void setBitMapFont(BitmapFont bitMapFont) {
        this.bitMapFont = bitMapFont;

    }

    /**
     * Is external texture active or not
     */
    public boolean isExternalTextureActive() {
        return isExternalTextureActive;
    }

    /**
     * Set external texture active to be drawn
     */
    public void setExternalTextureActive(boolean isExternalTextureActive) {
        this.isExternalTextureActive = isExternalTextureActive;
    }

    /**
     * Ger external texture
     */
    public TextureRegion getTextureExternal() {
        return textureExternal;
    }

    /**
     * Set external texture, it is all optional. This is for extra texture
     * region to be drawn over everything
     * <p>
     * EXAMPLE<br>
     * A menu button and there is a mini coin texture over the button in the
     * right side
     */
    public void setTextureExternal(TextureRegion textureExternal,
                                   boolean isExternalTextureActive) {
        this.textureExternal = textureExternal;
        this.isExternalTextureActive = isExternalTextureActive;
    }

    /**
     * Set external texture position
     */
    public void setTextureExternalPosXY(float x, float y) {
        externalTexturePosX = x;
        externalTexturePosY = y;

    }

    /**
     * Set external texture size
     */
    public void setTextureExternalSize(float width, float height) {
        externalTextureSizeW = width;
        externalTextureSizeH = height;
    }

    public boolean isDIPActive() {
        return DIPActive;
    }

    public void setDIPActive(boolean dIPActive) {
        DIPActive = dIPActive;
    }
}