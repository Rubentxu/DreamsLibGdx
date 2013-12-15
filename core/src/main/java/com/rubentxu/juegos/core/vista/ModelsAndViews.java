package com.rubentxu.juegos.core.vista;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rubentxu.juegos.core.modelo.Enemy;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;

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
            if(box2DPhysicsObject.getGrupo().equals(Box2DPhysicsObject.GRUPOS.PLATAFORMAS_MOVILES)
                    || box2DPhysicsObject.getGrupo().equals(Box2DPhysicsObject.GRUPOS.ENEMIGOS)) updateModelPosition(box2DPhysicsObject,sprite);
            sprite.draw(spriteBatch);

        }
    }

    private void updateModelPosition(Box2DPhysicsObject box2DPhysicsObject, Sprite sprite){
        sprite.setPosition(box2DPhysicsObject.getBody().getPosition().x ,
                box2DPhysicsObject.getBody().getPosition().y);

        if (box2DPhysicsObject.getGrupo().equals(Box2DPhysicsObject.GRUPOS.ENEMIGOS) ){
            if(((Enemy)box2DPhysicsObject).isFacingLeft() && !sprite.isFlipX()) sprite.flip(true,false);
            if(!((Enemy)box2DPhysicsObject).isFacingLeft() && sprite.isFlipX()) sprite.flip(true,false);
        }


    }

    public Map<Box2DPhysicsObject, Sprite> getModelsAndViews() {
        return modelsAndViews;
    }

    public void addModelAndView (Box2DPhysicsObject box2DPhysicsObject, Sprite sprite) {
        sprite.setSize(box2DPhysicsObject.getWidth(),box2DPhysicsObject.getHeight());
        sprite.setOrigin(box2DPhysicsObject.getWidth() / 2, box2DPhysicsObject.getHeight() / 2);
        sprite.setPosition(box2DPhysicsObject.getBody().getPosition().x,box2DPhysicsObject.getBody().getPosition().y);
        System.out.println("Nombre: "+box2DPhysicsObject.getNombre()+" Position: "+box2DPhysicsObject.getBody().getPosition()+" Grupo: "+ box2DPhysicsObject.getGrupo());
        this.modelsAndViews.put(box2DPhysicsObject,sprite);
    }
}
