package com.rubentxu.juegos.core.inputs;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.vista.WorldRenderer;


public class GameInputs implements InputProcessor {

    private World world;
    private WorldController controller;
    private WorldRenderer renderer;

    public GameInputs(World world, WorldController controller, WorldRenderer renderer) {
        this.world=world;
        this.controller=controller;
        this.renderer=renderer;
    }

    // * InputProcessor methods ***************************//

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.LEFT)
            controller.leftPressed();
        if (keycode == Keys.RIGHT)
            controller.rightPressed();
        if (keycode == Keys.Z)
            controller.jumpPressed();
        if (keycode == Keys.X)
            controller.firePressed();
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.LEFT)
            controller.leftReleased();
        if (keycode == Keys.RIGHT)
            controller.rightReleased();
        if (keycode == Keys.Z)
            controller.jumpReleased();
        if (keycode == Keys.X)
            controller.fireReleased();
        if (keycode == Keys.D)
            DreamsGame.DEBUG=!DreamsGame.DEBUG;
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if (!Gdx.app.getType().equals(ApplicationType.Android))
            return false;
        if (x < renderer.getWidth() / 2 && y > renderer.getHeight() / 2) {
            controller.leftPressed();
        }
        if (x > renderer.getWidth() / 2 && y > renderer.getHeight() / 2) {
            controller.rightPressed();
        }
        if ( y < renderer.getHeight() / 2) {
            controller.jumpPressed();
        }
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (!Gdx.app.getType().equals(Application.ApplicationType.Android))
            return false;
        if (x < renderer.getWidth() / 2 && y > renderer.getHeight() / 2) {
            controller.leftReleased();
        }
        if (x > renderer.getWidth() / 2 && y > renderer.getHeight() / 2) {
            controller.rightReleased();
        }
        if ( y < renderer.getHeight() / 2) {
            controller.jumpReleased();
        }

        return true;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        System.out.println("Height: " + world.getMap().getProperties().get("height", Integer.class));
       /* renderer.getCam().position.x -= Gdx.input.getDeltaX();
        renderer.getCam().position.y += Gdx.input.getDeltaY();*/
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        renderer.getCam().zoom += amount / 25f;
        System.out.println(amount);
        return true;
    }
}
