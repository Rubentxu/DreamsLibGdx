package com.rubentxu.juegos.core.managers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.rubentxu.juegos.core.BaseGame;
import com.rubentxu.juegos.core.DreamsGame;
import com.rubentxu.juegos.core.constantes.Constants;
import com.rubentxu.juegos.core.constantes.GameState;
import com.rubentxu.juegos.core.managers.StateObserver;
import com.rubentxu.juegos.core.modelo.Hero;
import com.rubentxu.juegos.core.modelo.Item;
import com.rubentxu.juegos.core.modelo.Profile;
import com.rubentxu.juegos.core.modelo.base.Box2DPhysicsObject;
import com.rubentxu.juegos.core.modelo.base.State;

public class ProfileManager implements StateObserver {


    private Profile profile;

    public Profile retrieveProfile() {
        profile = null;
        FileHandle profileDataFile = Gdx.files.local(Constants.PROFILE_DATA_FILE);
        Gdx.app.log(Constants.LOG, "Retrieving profile from: " + profileDataFile.path());

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
                FileHandle initProfileDataFile = Gdx.files.internal(Constants.INIT_PROFILE_DATA_FILE);
                Gdx.app.error(Constants.LOG, "Retrieving profile from: " + initProfileDataFile.path());
                profile = json.fromJson(Profile.class, initProfileDataFile.readString().trim());
                persist(profile);
            }
        } else {
            FileHandle initProfileDataFile = Gdx.files.internal(Constants.INIT_PROFILE_DATA_FILE);
            Gdx.app.log(Constants.LOG, "Retrieving profile from: " + initProfileDataFile.path());
            profile = json.fromJson(Profile.class, initProfileDataFile.readString().trim());
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

    public void resetToDefaultProfile() {
        FileHandle profileDataFile = Gdx.files.local(Constants.PROFILE_DATA_FILE);
        if (profileDataFile.exists()) profileDataFile.delete();
    }

    public Profile getProfile(){
        if(profile== null) return retrieveProfile();
        return profile;
    }

    @Override
    public void onNotify(State state, Box2DPhysicsObject entity) {

        if (entity instanceof Item) {
            switch (((Item) entity).getType()) {
                case COIN:
                    profile.addCoinsAquired(((Item) entity).getValue());
                    break;
                case POWERUP:
                    break;
                case KEY:
                    break;
            }
        }
    }

    @Override
    public void onNotifyStateTimeLimit(State state, Box2DPhysicsObject entity, float time) {
        Gdx.app.debug(Constants.LOG, "NotifyStateTimeLimit ProfileManager....");
        if (state.equals(Box2DPhysicsObject.BaseState.HURT) && entity instanceof Hero) {
            Hero hero = (Hero) entity;
            Gdx.app.log(Constants.LOG, "---------------------------------------------------------------------PIERDES VIDA???");
            if (profile.removeLive()) BaseGame.setGameState(GameState.GAME_OVER);
            hero.setState(Hero.StateHero.IDLE);
        }
    }
}