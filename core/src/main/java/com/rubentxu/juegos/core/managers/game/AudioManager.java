package com.rubentxu.juegos.core.managers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.modelo.Hero;
import com.rubentxu.juegos.core.modelo.Item;
import com.rubentxu.juegos.core.modelo.base.State;

import java.util.Observable;
import java.util.Observer;

public class AudioManager implements Observer {


    private DreamsGame game;
    private Music currentMusicPlaying;
    private Sound soundToPlay;

    public AudioManager(DreamsGame game) {
        this.game = game;
    }

    public void stopMusic() {
        if (currentMusicPlaying != null) {
            Gdx.app.log(Constants.LOG, "Stopping current music");
            currentMusicPlaying.stop();
            currentMusicPlaying.dispose();
            currentMusicPlaying = null;
        }
    }

    public void playMusic(String music) {
        if (!game.getPreferencesManager().music) {
            stopMusic();
            return;
        }
        stopMusic();
        currentMusicPlaying = game.getResourcesManager().getMusic(music);
        if (currentMusicPlaying != null) {
            Gdx.app.log(Constants.LOG, "Volume Music: " + game.getPreferencesManager().volMusic);
            Gdx.app.log(Constants.LOG, "Current Music vol: " + currentMusicPlaying.getVolume());
            currentMusicPlaying.play();
            currentMusicPlaying.setLooping(true);
            currentMusicPlaying.setVolume(game.getPreferencesManager().volMusic);
        }
    }

    public void playSound(String sound) {
        if (!game.getPreferencesManager().sound) {
            return;
        }

        soundToPlay = game.getResourcesManager().getSound(sound);
        if (soundToPlay != null) {
            Gdx.app.log(Constants.LOG, "Volume Music: " + game.getPreferencesManager().volMusic);
            soundToPlay.play(game.getPreferencesManager().volSound);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Item){
            switch (((Item) arg).getType()){
                case COIN:
                    Gdx.app.log(Constants.LOG, "Play Sound: Collect COIN.");
                    playSound(ResourcesManager.PICKUP_COIN_SOUND);
                    break;
                case POWERUP:
                    break;
                case KEY:
                    break;
            }
        }
        if (arg instanceof State) {
            if (((State)arg).equals(Hero.StateHero.JUMPING)) playSound(ResourcesManager.JUMP_SOUND);
            if (((State)arg).equals(Hero.StateHero.HURT)) playSound(ResourcesManager.HIT_SOUND);
        }
    }
}

