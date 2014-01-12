package com.rubentxu.juegos.core.utils.builders;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.servicios.Styles;


public class GuiBuilder {

    public static final void buildPadButtons(Stage stage, Styles styles, final WorldController controller) {
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
                controller.leftPressed();
                controller.rightReleased();
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event "+event.getType());
                controller.leftReleased();
            }
        });


        ImageButton btnRight = new ImageButton(styles.skin, "buttonRight");
        tableControlPad.add(btnRight).width(stage.getWidth() / 5).expandY().fill().padRight((stage.getWidth() / 5)*2);
        btnRight.addListener(new ClickListener() {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event "+event.getType());
                controller.leftReleased();
                controller.rightPressed();
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event "+event.getType());
                controller.rightReleased();
            }
        });

        ImageButton btnUP = new ImageButton(styles.skin, "buttonUp");
        tableControlPad.add(btnUP).width(stage.getWidth() / 5).expandY().fill();
        btnUP.addListener(new ClickListener() {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event "+event.getType());
                controller.jumpPressed();
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event "+event.getType());
                controller.jumpReleased();
            }
        });
        tableControlPad.pack();



        stage.addActor(tableControlPad);

    }

    public static final void buildTouchPad(Stage stage, Styles styles, final WorldController controller) {
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

        stage.addActor(touchpad);


    }
}
