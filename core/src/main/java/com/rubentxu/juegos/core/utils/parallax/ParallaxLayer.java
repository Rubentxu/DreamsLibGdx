package com.rubentxu.juegos.core.utils.parallax;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ParallaxLayer {

    private final float widthPercent;
    private final float heightPercent;
    private TextureRegion region;
    private float xRatio, yRatio;

    public ParallaxLayer(TextureRegion region, float xRatio, float yRatio,float widthPercent,float heightPercent) {
        super();
        this.region = region;
        this.xRatio = xRatio;
        this.yRatio = yRatio;
        this.widthPercent= widthPercent;
        this.heightPercent=heightPercent;
    }

    public TextureRegion getRegion() {
        return region;
    }

    public float getxRatio() {
        return xRatio;
    }

    public float getyRatio() {
        return yRatio;
    }

    public void render(float xPosition, float yPosition, float width, float height, SpriteBatch batch) {
        batch.begin();
        batch.draw(region, xPosition, yPosition, width/100*widthPercent,  height/100 *heightPercent);
        batch.end();

    }

}
