package com.rubentxu.juegos.core.utils.builders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.servicios.Assets;
import com.rubentxu.juegos.core.servicios.Styles;


public class GuiBuilder {

    public static final Table buildPadButtons(float width,float height, Styles styles, final WorldController controller) {

        Table tableControlPad = new Table();


        tableControlPad.row().height(height /6);
        ImageButton btnUpLeft = new ImageButton( styles.skin, "buttonLeft");
        tableControlPad.add(btnUpLeft).width(width / 5).expandY().fill();

        btnUpLeft.addListener(new ClickListener() {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event "+event.getType());
                super.touchDown(event,x,y,pointer,button);
                controller.leftPressed();
                controller.rightReleased();
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event "+event.getType());
                super.touchUp(event,x,y,pointer,button);
                controller.leftReleased();
            }

            @Override
            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                System.out.println("Event- "+event.getType());
                super.touchDragged(event,x,y,pointer);
                if(isOver(event.getListenerActor(), x, y)){
                    controller.rightReleased();
                    controller.leftPressed();
                }else {
                    controller.leftReleased();
                }
            }
        });


        ImageButton btnRight = new ImageButton(styles.skin, "buttonRight");
        tableControlPad.add(btnRight).width(width / 5).expandY().fill().padRight((width / 5)*2);
        btnRight.addListener(new ClickListener() {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event "+event.getType());
                super.touchDown(event,x,y,pointer,button);
                controller.leftReleased();
                controller.rightPressed();
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event "+event.getType());
                super.touchUp(event,x,y,pointer,button);
                controller.rightReleased();
            }

            @Override
            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                System.out.println("Event- "+event.getType());
                super.touchDragged(event, x, y, pointer);
                if(isOver(event.getListenerActor(), x, y)){
                    controller.rightPressed();
                    controller.leftReleased();
                }else {
                    controller.rightReleased();
                }
            }
        });

        ImageButton btnUP = new ImageButton(styles.skin, "buttonUp");
        tableControlPad.add(btnUP).width(width / 5).expandY().fill();
        btnUP.addListener(new ClickListener() {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event "+event.getType());
                super.touchDown(event,x,y,pointer,button);
                controller.jumpPressed();
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event "+event.getType());
                super.touchUp(event,x,y,pointer,button);
                controller.jumpReleased();
            }
            @Override
            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                System.out.println("Event- "+event.getType());
                super.touchDragged(event, x, y, pointer);
                if(isOver(event.getListenerActor(), x, y)){
                    controller.jumpPressed();
                }else {
                    controller.jumpReleased();
                }
            }
        });

        tableControlPad.setBounds(0,0,width,height /6);
        return tableControlPad;

    }


    public static final Touchpad buildTouchPad(float width,float height, Styles styles, final WorldController controller) {
        Touchpad touchpad = new Touchpad(10, styles.skin);
        touchpad.setBounds(15, 15, width / 6, width / 6);

        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("PercentX "+((Touchpad) actor).getKnobPercentX()+"PercentY "+((Touchpad) actor).getKnobPercentY());
                if (((Touchpad) actor).getKnobPercentX() == 0 || ((Touchpad) actor).getKnobPercentX() < 0.5
                        && ((Touchpad) actor).getKnobPercentX() > -0.5) {
                    controller.rightReleased();
                    controller.leftReleased();
                }
                if (((Touchpad) actor).getKnobPercentX() > 0.5) {
                    controller.rightPressed();
                    controller.leftReleased();
                }
                if (((Touchpad) actor).getKnobPercentX() < -0.5){
                    controller.leftPressed();
                    controller.rightReleased();
                }
                if (((Touchpad) actor).getKnobPercentY() > 0.5) {
                    controller.jumpPressed();
                } else {
                    controller.jumpReleased();
                }

            }
        });

        return touchpad;
    }

    public static final Table buildStats(float width,float height, Styles styles) {

        Table tableProfile = new Table();
        tableProfile.setBounds(0, 0, width , height /10);
        SpriteDrawable drawable=  styles.skin.get("stats", SpriteDrawable.class);
        tableProfile.setBackground(drawable);


        Image imageLives = new Image(((TextureAtlas) Assets.getInstance().get(Assets.getInstance().GUI_ATLAS)).findRegion("vidas"));
        imageLives.setName(Constants.IMAGE_LIVES);

        Label lives = new Label("0", styles.skin, "default", Color.ORANGE);
        lives.setName(Constants.LIVES);

        Label labelScore = new Label("Puntuacion: ", styles.skin, "default", Color.ORANGE);
        labelScore.setName(Constants.LABEL_SCORE);

        Label score = new Label("0000", styles.skin, "default", Color.ORANGE);
        score.setName(Constants.SCORE);

        tableProfile.add(imageLives).padRight(2);
        tableProfile.add(lives).width(width / 5).expandY().fill();

        tableProfile.add(labelScore).right().expandY().fill();
        tableProfile.add(score).right().expandY().fill();

        return tableProfile;
    }
}
