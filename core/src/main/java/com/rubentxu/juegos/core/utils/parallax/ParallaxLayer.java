package com.rubentxu.juegos.core.utils.parallax;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;


public class ParallaxLayer {

    public Texture region ;
    public Vector2 parallaxRatio;
    public Vector2 startPosition;
    public Vector2 padding ;
    public ParallaxLayer(Texture region,Vector2 parallaxRatio,Vector2 padding){
        this(region, parallaxRatio, new Vector2(0,0),padding);
    }
    /**
     * @param region   the TextureRegion to draw , this can be any width/height
     * @param parallaxRatio   the relative speed of x,y {@link ParallaxBackground#(ParallaxLayer[], float, float, Vector2)}
     * @param startPosition the init position of x,y
     * @param padding  the padding of the region at x,y
     */
    public ParallaxLayer(Texture region,Vector2 parallaxRatio,Vector2 startPosition,Vector2 padding){
        this.region  = region;
        this.parallaxRatio = parallaxRatio;
        this.startPosition = startPosition;
        this.padding = padding;
    }
}