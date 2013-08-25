package com.rubentxu.juegos.core.modelo;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class World {
    /** Our player controlled hero **/
    Rubentxu ruben;
    /** A world has a level through which Rubentxu needs to go through **/
    Level level;

    /** The collision boxes **/
    Array<Rectangle> collisionRects = new Array<Rectangle>();

    // Getters -----------

    public Array<Rectangle> getCollisionRects() {
        return collisionRects;
    }
    public Rubentxu getRubentxu() {
        return ruben;
    }
    public Level getLevel() {
        return level;
    }
    /** Return only the blocks that need to be drawn **/
    public List<Block> getDrawableBlocks(int width, int height) {
        int x = (int)ruben.getPosition().x - width;
        int y = (int)ruben.getPosition().y - height;
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        int x2 = x + 2 * width;
        int y2 = y + 2 * height;
        if (x2 > level.getWidth()) {
            x2 = level.getWidth() - 1;
        }
        if (y2 > level.getHeight()) {
            y2 = level.getHeight() - 1;
        }

        List<Block> blocks = new ArrayList<Block>();
        Block block;
        for (int col = x; col <= x2; col++) {
            for (int row = y; row <= y2; row++) {
                block = level.getBlocks()[col][row];
                if (block != null) {
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

    // --------------------
    public World() {
        createDemoWorld();
    }

    private void createDemoWorld() {
        ruben = new Rubentxu(new Vector2(7, 2));
        level = new Level();
    }

}
