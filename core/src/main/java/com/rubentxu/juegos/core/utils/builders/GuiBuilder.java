package com.rubentxu.juegos.core.utils.builders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.modelo.Profile;
import com.rubentxu.juegos.core.servicios.Styles;


public class GuiBuilder {

    public static final Actor buildPadButtons(Stage stage, Styles styles, final WorldController controller) {
        stage.clear();
        Table tableControlPad = new Table();
        tableControlPad.left().bottom();

        tableControlPad.row().height(stage.getHeight() /6);
        ImageButton btnUpLeft = new ImageButton( styles.skin, "buttonLeft");
        tableControlPad.add(btnUpLeft).width(stage.getWidth() / 5).expandY().fill();

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
        tableControlPad.add(btnRight).width(stage.getWidth() / 5).expandY().fill().padRight((stage.getWidth() / 5)*2);
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
        tableControlPad.add(btnUP).width(stage.getWidth() / 5).expandY().fill();
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
        tableControlPad.pack();

        return tableControlPad;

    }


    public static final Actor buildTouchPad(Stage stage, Styles styles, final WorldController controller) {
        stage.clear();
        Touchpad touchpad = new Touchpad(10, styles.skin);
        touchpad.setBounds(15, 15, stage.getWidth() / 6, stage.getWidth() / 6);

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

    public static final Actor buildStats(Stage stage, Styles styles, Profile profile) {

        Table tableProfile = new Table();
        tableProfile.left().top();
        tableProfile.setBackground((Drawable) styles.skin.get("debug", NinePatchDrawable.class));

        tableProfile.row().height(stage.getHeight() /7).pad(stage.getHeight()/100* 5);
        Image live = new Image( styles.skin, "btnMenu");
        live.setName("live1");
        Image live2 = new Image( styles.skin, "btnMenu");
        live2.setName("live2");
        Image live3 = new Image( styles.skin, "btnMenu");
        live3.setName("live3");
        Label labelScore = new Label("Puntuacio: ", styles.skin, "default", Color.ORANGE);
        labelScore.setName("labelScore");
        Label score = new Label("000", styles.skin, "default", Color.ORANGE);
        labelScore.setName("labelScore");
        tableProfile.add(live);
        tableProfile.add(live2);
        tableProfile.add(live3);


        return tableProfile;
    }

}
