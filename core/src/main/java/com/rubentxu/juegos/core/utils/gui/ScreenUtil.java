package com.rubentxu.juegos.core.utils.gui;


import com.badlogic.gdx.Gdx;
import com.rubentxu.juegos.core.constantes.Constants;

public class ScreenUtil {

    public static float getRatioX(float deviceWidth){
        return deviceWidth / Constants.VIRTUAL_WIDTH;
    }

    public static float getRatioY(float deviceHeight){
        return deviceHeight / Constants.VIRTUAL_HEIGHT;
    }

    public static float getWorldRatioX(float deviceWidth){
        return deviceWidth / Constants.WORLD_WIDTH;
    }

    public static float getWorlRatioY(float deviceHeight){
        return deviceHeight / Constants.WORLD_HEIGHT;
    }


    public static float getRatio(float deviceWidth, float deviceHeight){
        return (getRatioX(deviceWidth) + getRatioY(deviceHeight)) / 2;
    }

    public static float getRatio(){
        return (getRatioX(Gdx.graphics.getWidth()) + getRatioY(Gdx.graphics.getHeight())) / 2;
    }

    public static float getWorldRatio(){
        return (getWorldRatioX(Gdx.graphics.getWidth()) + getWorlRatioY(Gdx.graphics.getHeight())) / 2;
    }
}
