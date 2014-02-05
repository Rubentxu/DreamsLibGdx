package com.rubentxu.juegos.core.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.constantes.GameState;


public class MobileInput extends InputAdapter {

    @Override
    public boolean keyDown(int keycode) {
        Gdx.app.log(Constants.LOG, "PRESS BUTTON: KEY MOBILE INPUT");
        if ((keycode == Keys.ESCAPE) || (keycode == Keys.BACK) ) {
            Gdx.app.log(Constants.LOG, "PRESS BUTTON: GAME_BACK");
            DreamsGame.gameState= GameState.GAME_BACK;
        }


        return false;
    }
}
