package com.rubentxu.juegos.core.utils.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.managers.game.ResourcesManager;

public class CustomDialog extends Dialog {

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

    @Override
    public CustomDialog text(String text) {
        super.text(new Label(text, this.skin, "header"));
        return this;
    }

    public CustomDialog addButton(String buttonText, InputListener listener) {
        TextButton button = new TextButton(buttonText, this.skin);
        button(button);
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

    @Override
    protected void result (Object object) {
        Gdx.app.log(Constants.LOG, "La respuesta a la ventana de Dialogo es: "+ object+ "Tipo clase "+object.getClass() );
        if (object instanceof Boolean && object.equals(true)){
           Gdx.app.exit();
        } else if(object instanceof Boolean && object.equals(false)){
           hide();
            cancel();
        }
    }
}