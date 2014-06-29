package com.indignado.games.smariano.utils.gui;

import com.badlogic.gdx.Gdx;
import com.indignado.games.smariano.constantes.Constants;

public class ScaleUtil {

    public static float getRatioX(){
        return Gdx.graphics.getWidth() / Constants.VIRTUAL_WIDTH;
    }

    public static float getRatioY(){
        return Gdx.graphics.getHeight() / Constants.VIRTUAL_HEIGHT;
    }

    public static float getWorldRatioX(){
        return Gdx.graphics.getWidth() / Constants.WORLD_WIDTH;
    }

    public static float getWorldRatioY(){
        return Gdx.graphics.getHeight() / Constants.WORLD_HEIGHT;
    }

    public static float getSizeRatio(){
        if(getRatioX() < getRatioY())
            return getRatioX();
        else
            return getRatioY();
    }

    public static float getWorldSizeRatio() {
        if(getWorldRatioX() < getWorldRatioY())
            return getWorldRatioX();
        else
            return getWorldRatioY();
    }
}
