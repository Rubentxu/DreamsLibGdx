package com.rubentxu.juegos.core.utils.gui.mtx;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ButtonLevel extends AbstractButton {
    // Level stars (Not mandatory)
    private TextureRegion textureStarHolder;
    private TextureRegion textureStar;
    private int numberOfTotalStars = 1;
    private  int numberOfEarnedStars = 1;
    private float starSizeWidth;
    private float starSizeHeight;
    private float starPosXStart = 0;
    private float starPosYStart = 0;
    private float starSizeRatio = 5;

    // Level Number (Not mandatory)
    private int levelNumber = -999;


    public ButtonLevel(BitmapFont font,Drawable up, Drawable down) {
        super(font, up, down);

    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (isLockActive && textureLocked != null) {
            drawLocked(batch);
        }
        else if (levelNumber != -999 && bitMapFont != null) {
            super.draw(batch, parentAlpha);
            drawLevelNumber(batch);
            drawText(batch);
            drawStars(batch);
            drawExternalTexture(batch);
        }

        else {
            super.draw(batch, parentAlpha);
            drawText(batch);
            drawStars(batch);
            drawExternalTexture(batch);
        }
    }

    private void drawStars(SpriteBatch batch) {
        if(textureStarHolder != null && textureStar != null){

            float activePosXStart = (getX() + getWidth() / 2) - ((starSizeWidth * numberOfTotalStars) );
            float activePoxYStart = (getY() + starSizeHeight / 1.3f);

            float currentPosX = getX();

            for(int i = 0; i < numberOfTotalStars; i++){
                currentPosX = activePosXStart + (starSizeWidth *2* i);
                batch.draw(textureStarHolder, currentPosX + starPosXStart, activePoxYStart + starPosYStart,starSizeWidth*2, starSizeHeight*2);
            }

            for(int j = 0; j < numberOfEarnedStars; j++){
                currentPosX = activePosXStart + (starSizeWidth *2* j);
                batch.draw(textureStar, currentPosX + starPosXStart, activePoxYStart + starPosYStart,starSizeWidth*2, starSizeHeight*2);
            }
        }
    }

    private void drawText(SpriteBatch batch) {
        if(isTextActive && bitMapFont != null){
            bitMapFont.draw(batch, text, getX() +  textPosX, getY() + textPosY);
        }
    }

    private void drawLocked(SpriteBatch batch) {
        batch.draw(textureLocked, getX(), getY(), getWidth(), getHeight());
    }

    private void drawExternalTexture(SpriteBatch batch) {
        if(isExternalTextureActive && textureExternal != null){
            batch.draw(textureExternal, getX() + externalTexturePosX, getY() + externalTexturePosY, externalTextureSizeW, externalTextureSizeH);
        }
    }

    private void drawLevelNumber(SpriteBatch batch) {
        float singePositionArranger = 2.5f;
        float doublePositionArranger = 2.8f;

        if (levelNumber < 10) {
            bitMapFont.draw(batch, "" + levelNumber, getX() + getWidth() / singePositionArranger,
                    getY() + getHeight() / 1.4f);
        } else {
            bitMapFont.draw(batch, "" + levelNumber, getX() + getWidth() / doublePositionArranger,
                    getY() + getHeight() / 1.4f);
        }
    }

    public void setLevelStars(TextureRegion starHolderTexture, TextureRegion starTexture, int numberOfTotalStars, int numberOfEarnedStars){
        textureStarHolder = starHolderTexture;
        textureStar = starTexture;
        this.numberOfTotalStars = numberOfTotalStars;
        this.numberOfEarnedStars = numberOfEarnedStars;

        //
        float btnSizeW = getWidth() - (getWidth() / starSizeRatio);
        float btnSizeH = getHeight() - (getHeight() / starSizeRatio);
        starSizeWidth = btnSizeW / numberOfTotalStars;
        starSizeHeight = btnSizeH / numberOfTotalStars;
    }

    public float getLevelStarPosXStart() {
        return starPosXStart;
    }

    public void setLevelStarPosXStart(float starPosXStart) {
        this.starPosXStart = starPosXStart;
    }

    public float getLevelStarPosYStart() {
        return starPosYStart;
    }

    public void setLevelStarPosYStart(float starPosYStart) {
        this.starPosYStart = starPosYStart;
    }


    public float getLevelStarSizeRatio() {
        return starSizeRatio;
    }


    public void setLevelStarSizeRatio(float starSizeRatio) {
        this.starSizeRatio = starSizeRatio;
        float btnSizeW = getWidth() - (getWidth() / starSizeRatio);
        float btnSizeH = getHeight() - (getHeight() / starSizeRatio);
        starSizeWidth = btnSizeW / numberOfTotalStars;
        starSizeHeight = btnSizeH / numberOfTotalStars;
    }


    public int getLevelNumber() {
        return levelNumber;
    }


    public void setLevelNumber(int levelNumber, BitmapFont font) {
        this.levelNumber = levelNumber;
        bitMapFont = font;
    }

    public void setLevelNumberChange(int levelNumber){
        this.levelNumber = levelNumber;
    }

    public TextureRegion getTextureStarHolder() {
        return textureStarHolder;
    }

    public void setTextureStarHolder(TextureRegion textureStarHolder) {
        this.textureStarHolder = textureStarHolder;
    }

    public TextureRegion getTextureStar() {
        return textureStar;
    }

    public void setTextureStar(TextureRegion textureStar) {
        this.textureStar = textureStar;
    }

    public int getNumberOfTotalStars() {
        return numberOfTotalStars;
    }

    public void setNumberOfTotalStars(int numberOfTotalStars) {
        this.numberOfTotalStars = numberOfTotalStars;
    }

    public int getNumberOfEarnedStars() {
        return numberOfEarnedStars;
    }


    public void setNumberOfEarnedStars(int numberOfEarnedStars) {
        if(numberOfEarnedStars > numberOfTotalStars){
            numberOfEarnedStars = numberOfTotalStars;
        } else{
            this.numberOfEarnedStars = numberOfEarnedStars;
        }
    }
}