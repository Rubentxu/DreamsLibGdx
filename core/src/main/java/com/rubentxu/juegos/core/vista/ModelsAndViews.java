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
            if(box2DPhysicsObject.getGrupo().equals(Box2DPhysicsObject.GRUPOS.PLATAFORMAS_MOVILES)) updateModelPosition(box2DPhysicsObject,sprite);
            sprite.draw(spriteBatch);

        }
    }

    private void updateModelPosition(Box2DPhysicsObject box2DPhysicsObject, Sprite sprite){
        //System.out.println(box2DPhysicsObject.getNombre() + " = " + sprite);
        sprite.setPosition(box2DPhysicsObject.getBody().getPosition().x ,
                box2DPhysicsObject.getBody().getPosition().y);
    }

    public Map<Box2DPhysicsObject, Sprite> getModelsAndViews() {
        return modelsAndViews;
    }

    public void addModelAndView (Box2DPhysicsObject box2DPhysicsObject, Sprite sprite) {
        sprite.setSize(box2DPhysicsObject.getWidth(),box2DPhysicsObject.getHeight());
        sprite.setOrigin(box2DPhysicsObject.getWidth() / 2, box2DPhysicsObject.getHeight() / 2);
        sprite.setPosition(box2DPhysicsObject.getBody().getPosition().x,box2DPhysicsObject.getBody().getPosition().y);
        System.out.println("Tipo: "+box2DPhysicsObject.getNombre()+" Position: "+box2DPhysicsObject.getBody().getPosition());
        this.modelsAndViews.put(box2DPhysicsObject,sprite);
    }
}
