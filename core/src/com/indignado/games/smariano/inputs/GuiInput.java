package com.indignado.games.smariano.inputs;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.indignado.games.smariano.controladores.WorldController;

public class GuiInput extends InputListener {

    private Stage stage;
    private WorldController controller;


    public GuiInput(WorldController controller, Stage stage) {
        this.controller = controller;
        this.stage= stage;
    }


    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            /* if (!Gdx.app.getType().equals(ApplicationType.Android))
                return false;*/
        System.out.println("TouchDown"+ event.getStageX() +" y " +event.getStageY()+" pointer "+pointer+" button "+button);
        if (event.getStageX() < stage.getWidth() /2 && event.getStageY() < stage.getHeight() * 1/3) {
            controller.leftPressed();
        }
        if (event.getStageX() >stage.getWidth() /2 && event.getStageY() < stage.getHeight()* 1/3) {
            controller.rightPressed();
        }
        if ( event.getStageY() > stage.getHeight()* 1/3) {
            controller.jumpPressed();
        }
        return true;
    }

    @Override
    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

          /*  if (!Gdx.app.getType().equals(Application.ApplicationType.Android))
                return false;*/
        System.out.println("TouchUp "+ event.getStageX() +" y " +event.getStageY()+" pointer "+pointer+" button "+button);
        if (event.getStageX() < stage.getWidth() /2 && event.getStageY() < stage.getHeight() * 1/3) {
            controller.leftReleased();
        }
        if (event.getStageX() > stage.getWidth() /2 && event.getStageY() < stage.getHeight()* 1/3) {
            controller.rightReleased();
        }
        if ( event.getStageY()> stage.getHeight()* 1/3) {
            controller.jumpReleased();
        }

    }
}