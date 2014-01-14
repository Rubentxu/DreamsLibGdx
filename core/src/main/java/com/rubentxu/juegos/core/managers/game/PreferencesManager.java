package com.rubentxu.juegos.core.managers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.rubentxu.juegos.core.constantes.Constants;

public class PreferencesManager {

    protected Preferences getPrefs() {
        return Gdx.app.getPreferences(Constants.PREFS_NAME);
    }

    public boolean isSoundEnabled() {
        return getPrefs().getBoolean(Constants.PREF_SOUND_ENABLED, true);
    }

    public void setSoundEnabled(
            boolean soundEffectsEnabled) {
        getPrefs().putBoolean(Constants.PREF_SOUND_ENABLED, soundEffectsEnabled);
        getPrefs().flush();
    }

    public boolean isMusicEnabled() {
        return getPrefs().getBoolean(Constants.PREF_MUSIC_ENABLED, true);
    }

    public void setMusicEnabled(
            boolean musicEnabled) {
        getPrefs().putBoolean(Constants.PREF_MUSIC_ENABLED, musicEnabled);
        getPrefs().flush();
    }

    public boolean isTouchPadEnabled() {
        return getPrefs().getBoolean(Constants.PREF_TOUCHPAD_ENABLED, true);
    }

    public void setTouchPadEnabled(
            boolean touchPadEnabled) {
        getPrefs().putBoolean(Constants.PREF_TOUCHPAD_ENABLED, touchPadEnabled);
        getPrefs().flush();
    }

    public float getVolume() {
        return getPrefs().getFloat(Constants.PREF_VOLUME, 0.5f);
    }

    public void setVolume(
            float volume) {
        getPrefs().putFloat(Constants.PREF_VOLUME, volume);
        getPrefs().flush();
    }
}