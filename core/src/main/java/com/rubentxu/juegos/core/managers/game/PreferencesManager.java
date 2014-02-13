package com.rubentxu.juegos.core.managers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.rubentxu.juegos.core.constantes.Constants;

public class PreferencesManager {

    private Preferences preferences;
    public static final PreferencesManager instance = new PreferencesManager();
    public boolean sound=false;
    public boolean music=false;
    public float volSound=0.5f;
    public float volMusic=0.5f;
    public boolean touchPadEnabled=true;

    private PreferencesManager() {
        preferences= Gdx.app.getPreferences(Constants.PREFS_NAME);
        load();
    }

    public void load () {
        sound= preferences.getBoolean(Constants.PREF_SOUND_ENABLED, true);
        music= preferences.getBoolean(Constants.PREF_MUSIC_ENABLED, true);
        volSound = MathUtils.clamp(preferences.getFloat(Constants.PREF_VOLUME_SOUND, 0.5f),
                0.0f, 1.0f);
        volMusic = MathUtils.clamp(preferences.getFloat(Constants.PREF_VOLUME_MUSIC, 0.5f),
                0.0f, 1.0f);
        touchPadEnabled= preferences.getBoolean(Constants.PREF_TOUCHPAD_ENABLED, true);
    }
    public void save () {
        preferences.putBoolean(Constants.PREF_SOUND_ENABLED, sound);
        preferences.putBoolean(Constants.PREF_MUSIC_ENABLED, music);
        preferences.putFloat(Constants.PREF_VOLUME_SOUND, volSound);
        preferences.putFloat(Constants.PREF_VOLUME_MUSIC, volMusic);
        preferences.putBoolean(Constants.PREF_TOUCHPAD_ENABLED, touchPadEnabled);
        preferences.flush();
        load();
    }
}