package com.rubentxu.juegos.core.utils.gui.mtx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.tablelayout.Cell;
import com.esotericsoftware.tablelayout.Value.FixedValue;

public class TableModel extends Table {
    public TextureRegion textureBackground;
    public boolean isBackgroundTextureActive;

    public TableModel(TextureRegion textureBackground, float width, float height) {
        isBackgroundTextureActive = true;
        this.textureBackground = textureBackground;
        setSize(width, height);
    }

    public TableModel(TextureRegion textureBackground, float x, float y,float width, float height) {
        isBackgroundTextureActive = true;
        this.textureBackground = textureBackground;
        setBounds(x, y, width, height);
        setPosition(x, y);
        setHeight(height);
        setWidth(width);
    }

    public TableModel(TextureRegion textureBackground) {
        isBackgroundTextureActive = true;
        this.textureBackground = textureBackground;
    }

    public TableModel(Skin skin) {
        super(skin);
    }

    public TableModel(float width, float height) {
        setHeight(height);
        setWidth(width);
    }

    public TableModel() {
        super();
    }

    public TextureRegion getTextureBackground() {
        return textureBackground;
    }

    public void setTextureBackground(TextureRegion textureBackground, boolean isBackgroundTextureActive) {
        this.textureBackground = textureBackground;
        this.isBackgroundTextureActive = isBackgroundTextureActive;
    }

    public void setBackgroundTextureActive(boolean isBackgroundTextureActive) {
        this.isBackgroundTextureActive = isBackgroundTextureActive;
    }

    @SuppressWarnings("rawtypes")
    public Cell add(Actor actor, float width, float height) {
        return super.add(actor).size(width * AppSettings.getSizeRatio(),height * AppSettings.getSizeRatio());
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {

        if (textureBackground != null && isBackgroundTextureActive) {
            batch.draw(textureBackground, getX(), getY(), getOriginX(),
                    getOriginY(), getWidth(), getHeight(), getScaleX(),
                    getScaleY(), getRotation());
        }
        super.draw(batch, parentAlpha);
    }
}