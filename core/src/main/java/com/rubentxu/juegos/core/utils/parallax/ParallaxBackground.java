package com.rubentxu.juegos.core.utils.parallax;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class ParallaxBackground {

    private ParallaxLayer[] layers;
    private Camera camera;
    private SpriteBatch batch;


    /**
     * @param layers  The  background layers
     * @param width   The screenWith
     * @param height The screenHeight
     *
     */
    public ParallaxBackground(ParallaxLayer[] layers,float width,float height){
        this.layers = layers;
        camera = new OrthographicCamera(width, height);
        batch = new SpriteBatch();
    }

    public void render(Vector2 speed){
        this.camera.position.add(speed.x,speed.y/2, 0);
        for(ParallaxLayer layer:layers){
            batch.setProjectionMatrix(camera.projection);
            batch.begin();
            float currentX = - camera.position.x*layer.parallaxRatio.x % ( layer.region.getWidth() + layer.padding.x) ;

            if( speed.x < 0 )currentX += -( layer.region.getWidth() + layer.padding.x);
            do{
                float currentY = - camera.position.y*layer.parallaxRatio.y % ( layer.region.getHeight() + layer.padding.y) ;
                if( speed.y < 0 )currentY += - (layer.region.getHeight()+layer.padding.y);
                do{
                    batch.draw(layer.region,
                            -this.camera.viewportWidth/2+currentX + layer.startPosition.x ,
                            -this.camera.viewportHeight/2 + currentY +layer.startPosition.y);
                    currentY += ( layer.region.getHeight() + layer.padding.y );
                }while( currentY < camera.viewportHeight);
                currentX += ( layer.region.getWidth()+ layer.padding.x);
            }while( currentX < camera.viewportWidth);
            batch.end();
        }
    }
}