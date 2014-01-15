package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import java.util.HashMap;
import java.util.Map;

public class Profile implements Serializable {
    private int currentLevelId;
    private int credits;
    private Map<Integer, Integer> highScores;
    private int lives;

    public Profile() {
        currentLevelId=
        credits = 0;
        lives=3;
        highScores = new HashMap<Integer, Integer>();
    }

    public int getCurrentLevelId() {
        return currentLevelId;
    }

    public Map<Integer, Integer> getHighScores() {
        return highScores;
    }

    public int getHighScore(
            int levelId) {
        if (highScores == null) return 0;
        Integer highScore = highScores.get(levelId);
        return (highScore == null ? 0 : highScore);
    }

    public boolean notifyScore(
            int levelId,
            int score) {
        if (score > getHighScore(levelId)) {
            highScores.put(levelId, score);
            return true;
        }
        return false;
    }

    public int getCredits() {
        return credits;
    }

    public String getCreditsAsText() {
        return String.valueOf(credits);
    }

    public int getLives() {
        return lives;
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

        currentLevelId = json.readValue("currentLevelId", Integer.class, jsonData);
        credits = json.readValue("credits", Integer.class, jsonData);
        lives = json.readValue("lives", Integer.class, jsonData);

        Map<String, Integer> highScores = json.readValue("highScores", HashMap.class,
                Integer.class, jsonData);
        for (String levelIdAsString : highScores.keySet()) {
            int levelId = Integer.valueOf(levelIdAsString);
            Integer highScore = highScores.get(levelIdAsString);
            this.highScores.put(levelId, highScore);
        }
    }

    @Override
    public void write(Json json) {
        json.writeValue("currentLevelId", currentLevelId);
        json.writeValue("credits", credits);
        json.writeValue("highScores", highScores);
        json.writeValue("lives", highScores);
    }


}