package com.rubentxu.juegos.core.utils.debug;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.rubentxu.juegos.core.managers.game.ResourcesManager;

public class DebugWindow extends Window{

    private static Window windowDebug;
    public static Label myLabel;

    public DebugWindow(String title, WindowStyle style) {
        super(title, style);
    }


    public static Window createWindowDebug(ResourcesManager resourcesManager){

        BitmapFont font = (BitmapFont) resourcesManager.get(resourcesManager.BIG_FONT);
        font.setScale(1 / 64F);
        font.setUseIntegerPositions(false);
        Window.WindowStyle style = new Window.WindowStyle();
        //style.background = background;
        style.titleFont = font;
        style.titleFontColor=new Color(1, 1, 1, 0.4f);
        Sprite background = new Sprite((Texture) resourcesManager.get(resourcesManager.DEBUG_BACKGROUND));
        background.setSize(10,8);
        background.setOrigin(background.getWidth() / 2, background.getHeight() / 2);
        background.setColor(1,1,1,0.4f);
        style.background= new SpriteDrawable(background);
        Window window = new DebugWindow("", style);
        window.setPosition(0, 0);
        window.setKeepWithinStage(false);
        window.setTransform(false);
        window.setFillParent(true);

        Label.LabelStyle labelStyle= new Label.LabelStyle(font,new Color(1, 1, 1, 1)) ;
        myLabel= new Label("",labelStyle);
        window.add(myLabel).pad(0.2f).width(8);

        return window;
    }

    public static final Window getInstance(ResourcesManager resourcesManager){
        if(windowDebug==null){
            windowDebug= createWindowDebug(resourcesManager);
        }
        return windowDebug;
    }



}
