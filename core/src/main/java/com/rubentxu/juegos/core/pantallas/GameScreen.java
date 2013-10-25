package com.rubentxu.juegos.core.pantallas;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.vista.WorldRenderer;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;


public class GameScreen implements Screen {

    private World world;
    private WorldRenderer renderer;
    private WorldController controller;

    public  GameScreen(World world,WorldController controller, WorldRenderer renderer){
        this.world= world;
        this.controller= controller;
        this.renderer= renderer;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        controller.update(delta);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.setSize(width, height);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        world.dispose();
        renderer.dispose();
        controller.dispose();
    }




}
