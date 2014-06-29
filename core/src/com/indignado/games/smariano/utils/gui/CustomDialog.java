package com.indignado.games.smariano.utils.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.indignado.games.smariano.SMariano;
import com.indignado.games.smariano.constantes.Constants;
import com.indignado.games.smariano.constantes.GameState;
import com.indignado.games.smariano.managers.game.ResourcesManager;

public class CustomDialog extends Window {

    private Skin skin;
    public CustomDialog (String title,ResourcesManager resourcesManager) {
        super(title,resourcesManager.getStyles().skin );
        this.skin=resourcesManager.getStyles().skin;
        initialize();
    }

    private void initialize() {

        padTop(60);
        getButtonTable().defaults().height(60);
        setModal(true);
        setMovable(false);
    }

    public CustomDialog addButton(String buttonText, InputListener listener) {
        TextButton button = new TextButton(buttonText, this.skin);
        button.addListener(listener);
        add(button);
        return this;
    }

    @Override
    public float getPrefWidth() {
        return 480f;
    }

    @Override
    public float getPrefHeight() {
        return 240f;
    }


    protected void result (Object object) {
        SMariano.setGameState(GameState.GAME_RUNNING);
        Gdx.app.log(Constants.LOG, "La respuesta a la ventana de Dialogo es: "+ object+ "Tipo clase "+object.getClass() );
        if (object instanceof Boolean && object.equals(true)){
           Gdx.app.exit();
        } else if(object instanceof Boolean && object.equals(false)){
            Gdx.app.log(Constants.LOG, "Se pulso en continuar la partida: ");

            this.remove();
             this.setVisible(false);
            Gdx.app.log(Constants.LOG, "La ventana de dialogo se cerro?... "+remove());
        }
    }

}