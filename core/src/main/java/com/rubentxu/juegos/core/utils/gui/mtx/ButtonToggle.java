package com.rubentxu.juegos.core.utils.gui.mtx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ButtonToggle extends AbstractButton {

    private TextureRegion textureToggleOn;
    private TextureRegion textureToggleOff;
    private boolean isToggleActive = false;

    public ButtonToggle(BitmapFont bitMapFont, Drawable up, Drawable down,TextureRegion toggleOn, TextureRegion toggleOff,
                        boolean isToggleActive) {
        super(bitMapFont, up, down);
        textureToggleOn = toggleOn;
        textureToggleOff = toggleOff;
        this.isToggleActive = isToggleActive;
    }

    public ButtonToggle(BitmapFont bitMapFont, Drawable up, Drawable down,TextureRegion toggleOn, TextureRegion toggleOff,
                        boolean isToggleActive, float width, float height) {
        super(bitMapFont, up, down, width, height);
        textureToggleOn = toggleOn;
        textureToggleOff = toggleOff;
        this.isToggleActive = isToggleActive;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (isToggleActive && textureLocked != null) {
            drawLocked(batch);
        }

        else if (text != "" && bitMapFont != null && isTextActive) {
            drawToggle(batch);
            drawText(batch);
            drawExternalTexture(batch);
        }

        else {
            drawToggle(batch);
            drawExternalTexture(batch);
        }
    }

    @Override
    public Actor hit(float x, float y, boolean t) {
        if (!isLockActive) {
            return super.hit(x, y, t);
        } else {
            return null;
        }
    }

    private void drawToggle(SpriteBatch batch) {
        if (isToggleActive) {
            batch.draw(textureToggleOn, getX(), getY(), getWidth(), getHeight());
        } else {
            batch.draw(textureToggleOff, getX(), getY(), getWidth(),
                    getHeight());
        }
    }

    private void drawExternalTexture(SpriteBatch batch) {
        if (isExternalTextureActive && textureExternal != null) {
            batch.draw(textureExternal, getX() + externalTexturePosX, getY()
                    + externalTexturePosY, externalTextureSizeW,
                    externalTextureSizeH);
        }
    }

    private void drawLocked(SpriteBatch batch) {
        batch.draw(textureLocked, getX(), getY(), getWidth(), getHeight());
    }

    private void drawText(SpriteBatch batch) {
        bitMapFont.draw(batch, text, getX() + textPosX, getY() + textPosY);
    }

    public TextureRegion getTextureToggleOn() {
        return textureToggleOn;
    }

    public void setTextureToggleOn(TextureRegion textureToggleOn) {
        this.textureToggleOn = textureToggleOn;
    }

    public TextureRegion getTextureToggleOff() {
        return textureToggleOff;
    }

    public void setTextureToggleOff(TextureRegion textureToggleOff) {
        this.textureToggleOff = textureToggleOff;
    }

    public boolean isToggleActive() {
        return isToggleActive;
    }

    public void setToggleActive(boolean isToggleActive) {
        this.isToggleActive = isToggleActive;
    }

    public void setToggleSwitch() {
        if (isToggleActive) {
            isToggleActive = false;
        } else {
            isToggleActive = true;
        }
    }
}