package com.rubentxu.juegos.core.managers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;

public class MusicManager {


    private DreamsGame game;
    private Music currentMusicPlaying;
    private float volume = 1f;
    private boolean enabled = true;

    public MusicManager(DreamsGame game) {
        this.game=game;
    }


    public void setCurrentMusicPlaying(Music musicPlaying){
        currentMusicPlaying= musicPlaying;
    }

    public void stop()
    {
        if( currentMusicPlaying != null ) {
            Gdx.app.log(Constants.LOG, "Stopping current music");
            currentMusicPlaying.stop();
            currentMusicPlaying.dispose();
            currentMusicPlaying=null;
        }
    }

    public void play(){
        if(!game.getPreferencesManager().music){
            stop();
        } else if(currentMusicPlaying!=null) {
            currentMusicPlaying.setVolume(game.getPreferencesManager().volMusic);
            currentMusicPlaying.setLooping(true);
            currentMusicPlaying.play();
        }
    }

    public void setVolume(float volume )
    {
        Gdx.app.log(Constants.LOG, "Adjusting music volume to: " + volume);

        if( volume < 0 || volume > 1f ) {
            Gdx.app.error(Constants.LOG, "The volume must be inside the range: [0,1]", new IllegalArgumentException());
            volume = 0;
        }
        this.volume = volume;

        if( currentMusicPlaying != null ) {
            currentMusicPlaying.setVolume( volume );
        }
    }


    public void setEnabled(boolean enabled){
        this.enabled = enabled;
        if(!enabled)
            stop();
    }

    public void dispose()
    {
        Gdx.app.log(Constants.LOG, "Disposing music manager");
        stop();
    }
}