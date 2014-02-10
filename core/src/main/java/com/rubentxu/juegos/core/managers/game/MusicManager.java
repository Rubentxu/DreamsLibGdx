package com.rubentxu.juegos.core.managers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.servicios.Assets;

public class MusicManager {


    private DreamsGame game;
    private Music currentMusicPlaying;

    public MusicManager(DreamsGame game) {
        this.game = game;
    }

    public void stop() {
        if (currentMusicPlaying != null) {
            Gdx.app.log(Constants.LOG, "Stopping current music");
            currentMusicPlaying.stop();
            currentMusicPlaying.dispose();
            currentMusicPlaying = null;
        }
    }

    public void play(String music) {
        if (!game.getPreferencesManager().music) {
            stop();
            return;
        }
        currentMusicPlaying = Assets.getInstance().getMusic(music);
        if (currentMusicPlaying != null) {
            currentMusicPlaying.setVolume(game.getPreferencesManager().volMusic);
            currentMusicPlaying.setLooping(true);
            currentMusicPlaying.play();
        }
    }
}