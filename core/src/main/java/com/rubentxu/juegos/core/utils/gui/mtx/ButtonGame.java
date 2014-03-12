package com.rubentxu.juegos.core.utils.gui.mtx;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ButtonGame extends AbstractButton {

    public ButtonGame(BitmapFont bitMapFont, Drawable up, Drawable down,float width, float height) {
        super(bitMapFont, up, down, width, height);
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (isLockActive && textureLocked != null) {
            drawLocked(batch);
        } else if (text != "" && bitMapFont != null && isTextActive) {
            super.draw(batch, parentAlpha);
            drawText(batch);
            drawExternalTexture(batch);
        } else {
            super.draw(batch, parentAlpha);
            drawExternalTexture(batch);
        }
    }

    private void drawExternalTexture(SpriteBatch batch) {
        if (isExternalTextureActive && textureExternal != null) {
            batch.draw(textureExternal, getX() + externalTexturePosX, getY() + externalTexturePosY, externalTextureSizeW, externalTextureSizeH);
        }
    }

    private void drawLocked(SpriteBatch batch) {
        batch.draw(textureLocked, getX(), getY(), getWidth(), getHeight());
    }

    private void drawText(SpriteBatch batch) {
        bitMapFont.draw(batch, text, getX() + textPosX, getY() + textPosY);
    }
}