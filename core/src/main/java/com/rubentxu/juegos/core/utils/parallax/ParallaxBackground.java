package com.rubentxu.juegos.core.utils.parallax;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class ParallaxBackground {

    private List<ParallaxLayer> layers;
    private float width, height;

    public ParallaxBackground(float width, float height) {
        this.layers = new ArrayList<ParallaxLayer>();
        this.width = width;
        this.height = height;
    }

    public void render(Vector3 position, SpriteBatch batch) {
        for (ParallaxLayer layer : layers) {
            float layerOffsetX = (position.x * layer.getxRatio() % width);
            float layerOffsetY = (position.y * layer.getyRatio() % height);
            layer.render(position.x - width / 2f - layerOffsetX, position.y - height / 2f - layerOffsetY, width, height, batch);
            layer.render(position.x - width / 2f - layerOffsetX , position.y - height / 2f - layerOffsetY, width, height, batch);
        }
    }

    public List<ParallaxLayer> getLayers() {
        return layers;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void addLayer(ParallaxLayer parallaxLayer) {
        layers.add(parallaxLayer);
    }

}