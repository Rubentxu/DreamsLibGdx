package com.rubentxu.juegos.core.modelo;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

import java.util.List;

public class Profile implements Serializable {
    private int credits;
    private int lives;
    private List<Level> levels;

    public Profile() {
        credits = 0;
        lives = 3;
    }

    public Profile(List<Level> levels) {
        credits = 0;
        lives = 3;
        this.levels = levels;
    }

    public int getHighScore(int levelId) {
        return levels.get(levelId).getHighScore();
    }

    public boolean notifyScore(int levelId, int score) {
        if (score > getHighScore(levelId)) {
            levels.get(levelId).setHighScore(score);
            return true;
        }
        return false;
    }

    public int getCredits() {
        return credits;
    }

    public void addCredits(int credits) {
        this.credits += credits;
    }

    public String getCreditsAsText() {
        return String.valueOf(credits);
    }

    public int getLives() {
        return lives;
    }

    public boolean removeLive() {
        return --lives <= 0;
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

        levels = json.readValue("levels", List.class, jsonData);
        credits = json.readValue("credits", Integer.class, jsonData);
        lives = json.readValue("lives", Integer.class, jsonData);
    }

    @Override
    public void write(Json json) {
        json.writeValue("levels", levels);
        json.writeValue("credits", credits);
        json.writeValue("lives", lives);
    }

    public CharSequence getLivesAsText() {
        return String.valueOf(lives);
    }

    public List<Level> getLevels() {
        return levels;
    }
}