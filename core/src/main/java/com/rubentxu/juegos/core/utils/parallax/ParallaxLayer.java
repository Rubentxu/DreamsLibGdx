package com.rubentxu.juegos.core.utils.parallax;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParallaxLayer {

    private final float widthPercent;
    private final float heightPercent;
    private Texture fondo;
    private float xRatio, yRatio;

    public ParallaxLayer(Texture fondo, float xRatio, float yRatio,float widthPercent,float heightPercent) {
        super();
        this.fondo = fondo;
        this.xRatio = xRatio;
        this.yRatio = yRatio;
        this.widthPercent= widthPercent;
        this.heightPercent=heightPercent;
    }

    public Texture getFondo() {
        return fondo;
    }

    public float getxRatio() {
        return xRatio;
    }

    public float getyRatio() {
        return yRatio;
    }

    public void render(float xPosition, float yPosition, float width, float height, SpriteBatch batch) {
        batch.begin();
        batch.draw(fondo, xPosition, yPosition, width/100*widthPercent,  height/100 *heightPercent);
        batch.end();

    }

}
