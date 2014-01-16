package com.rubentxu.juegos.core.utils.builders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.controladores.WorldController;
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
                if(isOver()){
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
                if(isOver()){
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
                if(isOver()){
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

        tableProfile.setBackground((Drawable) styles.skin.get("debug", NinePatchDrawable.class));
        tableProfile.row().setWidgetHeight(height / 7);
        tableProfile.setHeight(height /7);
        tableProfile.pad(height / 100 * 5);
        Image live = new Image( styles.skin.get("buttonLeft", NinePatchDrawable.class));
        live.setName(Constants.NAME_LIVES1);
        Image live2 = new Image( styles.skin.get("buttonLeft", NinePatchDrawable.class));
        live2.setName(Constants.NAME_LIVES2);
        Image live3 = new Image(styles.skin.get("buttonLeft", NinePatchDrawable.class));
        live3.setName(Constants.NAME_LIVES3);
        Label labelScore = new Label("Puntuacion: ", styles.skin, "default", Color.ORANGE);
        labelScore.setName(Constants.NAME_LABEL_SCORE);
        Label score = new Label("000", styles.skin, "default", Color.ORANGE);
        score.setName(Constants.NAME_SCORE);
        tableProfile.add(live);
        tableProfile.add(live2);
        tableProfile.add(live3);
        tableProfile.add(labelScore);
        tableProfile.add(score);

        return tableProfile;
    }
}
