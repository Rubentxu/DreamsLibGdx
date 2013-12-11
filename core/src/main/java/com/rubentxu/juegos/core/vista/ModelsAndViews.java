package com.rubentxu.juegos.core.vista;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rubentxu.juegos.core.modelo.Box2DPhysicsObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ModelsAndViews {

    private Map<Box2DPhysicsObject,Sprite> modelsAndViews= new HashMap<Box2DPhysicsObject,Sprite>();

    public void render(SpriteBatch spriteBatch){
        Iterator it = getModelsAndViews().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry mav = (Map.Entry)it.next();
            Sprite sprite=(Sprite) mav.getValue();
            Box2DPhysicsObject box2DPhysicsObject=(Box2DPhysicsObject)mav.getKey();
            updateModelPosition(box2DPhysicsObject,sprite);
            sprite.draw(spriteBatch);
            it.next();
        }
    }

    private void updateModelPosition(Box2DPhysicsObject box2DPhysicsObject, Sprite sprite){
        System.out.println(box2DPhysicsObject + " = " + sprite);
        sprite.setPosition(box2DPhysicsObject.getBody().getPosition().x-box2DPhysicsObject.getWidth()/2 ,
                box2DPhysicsObject.getBody().getPosition().y-box2DPhysicsObject.getHeight()/2);
    }

    public Map<Box2DPhysicsObject, Sprite> getModelsAndViews() {
        return modelsAndViews;
    }

    public void addModelAndView (Box2DPhysicsObject box2DPhysicsObject, Sprite sprite) {
        sprite.setSize(box2DPhysicsObject.getWidth(),box2DPhysicsObject.getHeight());
        sprite.setOrigin(box2DPhysicsObject.getWidth() / 2, box2DPhysicsObject.getHeight() / 2);
        this.modelsAndViews.put(box2DPhysicsObject,sprite);
    }
}
