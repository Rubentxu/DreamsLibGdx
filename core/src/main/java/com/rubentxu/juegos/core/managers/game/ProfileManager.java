package com.rubentxu.juegos.core.managers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.modelo.Profile;

public class ProfileManager {


    private Profile profile;

    public Profile retrieveProfile() {

        FileHandle profileDataFile = Gdx.files.local(Constants.PROFILE_DATA_FILE);
        Gdx.app.log(Constants.LOG, "Retrieving profile from: " + profileDataFile.path());

        if (profile != null) return profile;
        Json json = new Json();

        if (profileDataFile.exists()) {
            try {
                String profileAsText = profileDataFile.readString().trim();

                if (profileAsText.matches("^[A-Za-z0-9/+=]+$")) {
                    Gdx.app.log(Constants.LOG, "Persisted profile is base64 encoded");
                    profileAsText = Base64Coder.decodeString(profileAsText);
                }

                profile = json.fromJson(Profile.class, profileAsText);

            } catch (Exception e) {
                Gdx.app.error(Constants.LOG, "Unable to parse existing profile data file", e);
                profile = new Profile();
                persist(profile);
            }
        } else {
            profile = new Profile();
            persist(profile);
        }

        return profile;
    }

    protected void persist(Profile profile) {

        FileHandle profileDataFile = Gdx.files.local(Constants.PROFILE_DATA_FILE);
        Gdx.app.log(Constants.LOG, "Persisting profile in: " + profileDataFile.path());
        Json json = new Json();
        String profileAsText = json.toJson(profile);

        if (!DreamsGame.DEBUG) {
            profileAsText = Base64Coder.encodeString(profileAsText);
        }
        profileDataFile.writeString(profileAsText, false);
    }

    public void persist() {
        if (profile != null) {
            persist(profile);
        }
    }
}