package com.rubentxu.juegos.core.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.modelo.World;
import com.rubentxu.juegos.core.vista.WorldRenderer;


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

        // since Box2D 2.2 we need to reset the friction of any existing contacts
        Array<Contact> contacts = world.getPhysics().getContactList();
        for (int i = 0; i < world.getPhysics().getContactCount(); i++) {
            Contact contact = contacts.get(i);
            contact.resetFriction();
        }
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
