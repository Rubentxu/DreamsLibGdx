package com.rubentxu.juegos.core.constantes;


public class Constants {

    public static final float WORLD_HEIGHT = 16.875f; // 1080 / 64 =16.875 px
    public static final float WORLD_WIDTH = 30f;     //  1920 / 64 = 30f px

    public static final float VIRTUAL_HEIGHT = 1080f; // 16.875 x 64 =1080 px
    public static final float VIRTUAL_WIDTH = 1920f;     //  30 x 64 = 1920 px
    // Box2D config
    public static final float RUNNING_FRAME_DURATION = 0.02f;
    public static final int VELOCITY_ITERATIONS = 10;
    public static final int POSITION_ITERATIONS = 8;
    // Game attributes
    public static final String VERSION = "0.3 Alpha";
    public static final String LOG = "Hero Dreams";
    // Preferences
    public static final String PREFS_NAME = "DreamsGame";
    public static final String PREF_VOLUME_SOUND = "volume.music";
    public static final String PREF_VOLUME_MUSIC = "volume.sound";
    public static final String PREF_MUSIC_ENABLED = "music.enabled";
    public static final String PREF_SOUND_ENABLED = "sound.enabled";
    public static final String PREF_TOUCHPAD_ENABLED = "touchpad.enabled";
    // Profile
    public static final String PROFILE_DATA_FILE = "data/profile.game";
    public static final String INIT_PROFILE_DATA_FILE = "data/initProfile.game";

    //Name elements GUI STATS
    public static final String LABEL_SCORE= "Puntuacion";
    public static final String SCORE= "Score";
    public static final String IMAGE_LIVES= "ImageLives";
    public static final String LIVES= "Lives";

    // Name World Events
    public static final int COLLET_COIN = 1;
}
