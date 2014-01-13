package com.rubentxu.juegos.core.managers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferencesManager {

    private static final String PREF_VOLUME = "volume";
    private static final String PREF_MUSIC_ENABLED = "music.enabled";
    private static final String PREF_SOUND_ENABLED = "sound.enabled";
    private static final String PREF_TOUCHPAD_ENABLED = "touchpad.enabled";
    private static final String PREFS_NAME = "DreamsGame";

    public PreferencesManager() {
    }

    protected Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public boolean isSoundEnabled() {
        return getPrefs().getBoolean(PREF_SOUND_ENABLED, true);
    }

    public void setSoundEnabled(
            boolean soundEffectsEnabled) {
        getPrefs().putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
        getPrefs().flush();
    }

    public boolean isMusicEnabled() {
        return getPrefs().getBoolean(PREF_MUSIC_ENABLED, true);
    }

    public void setMusicEnabled(
            boolean musicEnabled) {
        getPrefs().putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
        getPrefs().flush();
    }

    public boolean isTouchPadEnabled() {
        return getPrefs().getBoolean(PREF_TOUCHPAD_ENABLED, true);
    }

    public void setTouchPadEnabled(
            boolean touchPadEnabled) {
        getPrefs().putBoolean(PREF_TOUCHPAD_ENABLED, touchPadEnabled);
        getPrefs().flush();
    }

    public float getVolume() {
        return getPrefs().getFloat(PREF_VOLUME, 0.5f);
    }

    public void setVolume(
            float volume) {
        getPrefs().putFloat(PREF_VOLUME, volume);
        getPrefs().flush();
    }
}