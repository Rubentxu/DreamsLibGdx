package com.rubentxu.juegos.core.controladores;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.rubentxu.juegos.core.modelo.Block;
import com.rubentxu.juegos.core.modelo.Rubentxu;
import com.rubentxu.juegos.core.modelo.World;

import java.util.HashMap;
import java.util.Map;

public class RubentxuController {
    enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }

    private static final long LONG_JUMP_PRESS 	= 150l;
    private static final float ACCELERATION 	= 20f;
    private static final float GRAVITY 			= -15f;
    private static final float MAX_JUMP_SPEED	= 7f;
    private static final float DAMP 			= 0.90f;
    private static final float MAX_VEL 			= 2f;

    private World world;
    private Rubentxu ruben;
    private long	jumpPressedTime;
    private boolean jumpingPressed;
    private boolean grounded = false;

    // This is the rectangle pool used in collision detection
    // Good to avoid instantiation each frame
    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };

    static Map<Keys, Boolean> keys = new HashMap<RubentxuController.Keys, Boolean>();
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.JUMP, false);
        keys.put(Keys.FIRE, false);
    };

    // Blocks that Bob can collide with any given frame
    private Array<Block> collidable = new Array<Block>();

    public RubentxuController(World world) {
        this.world = world;
        this.ruben = world.getRubentxu();
    }

    // ** Key presses and touches **************** //

    public void leftPressed() {
        keys.get(keys.put(Keys.LEFT, true));
    }

    public void rightPressed() {
        keys.get(keys.put(Keys.RIGHT, true));
    }

    public void jumpPressed() {
        keys.get(keys.put(Keys.JUMP, true));
    }

    public void firePressed() {
        keys.get(keys.put(Keys.FIRE, false));
    }

    public void leftReleased() {
        keys.get(keys.put(Keys.LEFT, false));
    }

    public void rightReleased() {
        keys.get(keys.put(Keys.RIGHT, false));
    }

    public void jumpReleased() {
        keys.get(keys.put(Keys.JUMP, false));
        jumpingPressed = false;
    }

    public void fireReleased() {
        keys.get(keys.put(Keys.FIRE, false));
    }

    /** The main update method **/
    public void update(float delta) {

        processInput();


        if (grounded && ruben.getState().equals(Rubentxu.State.JUMPING)) {
            ruben.setState(Rubentxu.State.IDLE);
        }

        // Setting initial vertical acceleration
        ruben.getAcceleration().y = GRAVITY;

        // Convert acceleration to frame time
        ruben.getAcceleration().mul(delta);

        // apply acceleration to change velocity
        ruben.getVelocity().add(ruben.getAcceleration().x, ruben.getAcceleration().y);

        checkCollisionWithBlocks(delta);

        ruben.getVelocity().x *= DAMP;

        // ensure terminal velocity is not exceeded
        if (ruben.getVelocity().x > MAX_VEL) {
            ruben.getVelocity().x = MAX_VEL;
        }
        if (ruben.getVelocity().x < -MAX_VEL) {
            ruben.getVelocity().x = -MAX_VEL;
        }

        // simply updates the state time
        ruben.update(delta);

    }


    private void checkCollisionWithBlocks(float delta) {

        ruben.getVelocity().mul(delta);


        Rectangle rubenRect = rectPool.obtain();

        rubenRect.set(ruben.getBounds().x, ruben.getBounds().y, ruben.getBounds().width, ruben.getBounds().height);


        int startX, endX;
        int startY = (int) ruben.getBounds().y;
        int endY = (int) (ruben.getBounds().y + ruben.getBounds().height);

        if (ruben.getVelocity().x < 0) {
            startX = endX = (int) Math.floor(ruben.getBounds().x + ruben.getVelocity().x);
        } else {
            startX = endX = (int) Math.floor(ruben.getBounds().x + ruben.getBounds().width + ruben.getVelocity().x);
        }


        populateCollidableBlocks(startX, startY, endX, endY);


        rubenRect.x += ruben.getVelocity().x;


        world.getCollisionRects().clear();


        for (Block block : collidable) {
            if (block == null) continue;
            if (rubenRect.overlaps(block.getBounds())) {
                ruben.getVelocity().x = 0;
                world.getCollisionRects().add(block.getBounds());
                break;
            }
        }

        rubenRect.x = ruben.getPosition().x;

        startX = (int) ruben.getBounds().x;
        endX = (int) (ruben.getBounds().x + ruben.getBounds().width);
        if (ruben.getVelocity().y < 0) {
            startY = endY = (int) Math.floor(ruben.getBounds().y + ruben.getVelocity().y);
        } else {
            startY = endY = (int) Math.floor(ruben.getBounds().y + ruben.getBounds().height + ruben.getVelocity().y);
        }

        populateCollidableBlocks(startX, startY, endX, endY);

        rubenRect.y += ruben.getVelocity().y;

        for (Block block : collidable) {
            if (block == null) continue;
            if (rubenRect.overlaps(block.getBounds())) {
                if (ruben.getVelocity().y < 0) {
                    grounded = true;
                }
                ruben.getVelocity().y = 0;
                world.getCollisionRects().add(block.getBounds());
                break;
            }
        }

        rubenRect.y = ruben.getPosition().y;


        ruben.getPosition().add(ruben.getVelocity());
        ruben.getBounds().x = ruben.getPosition().x;
        ruben.getBounds().y = ruben.getPosition().y;


        ruben.getVelocity().mul(1 / delta);

    }


    private void populateCollidableBlocks(int startX, int startY, int endX, int endY) {
        collidable.clear();
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x >= 0 && x < world.getLevel().getWidth() && y >=0 && y < world.getLevel().getHeight()) {
                    collidable.add(world.getLevel().get(x, y));
                }
            }
        }
    }


    private boolean processInput() {
        if (keys.get(Keys.JUMP)) {
            if (!ruben.getState().equals(Rubentxu.State.JUMPING)) {
                jumpingPressed = true;
                jumpPressedTime = System.currentTimeMillis();
                ruben.setState(Rubentxu.State.JUMPING);
                ruben.getVelocity().y = MAX_JUMP_SPEED;
                grounded = false;
            } else {
                if (jumpingPressed && ((System.currentTimeMillis() - jumpPressedTime) >= LONG_JUMP_PRESS)) {
                    jumpingPressed = false;
                } else {
                    if (jumpingPressed) {
                        ruben.getVelocity().y = MAX_JUMP_SPEED;
                    }
                }
            }
        }
        if (keys.get(Keys.LEFT)) {
            // left is pressed
            ruben.setFacingLeft(true);
            if (!ruben.getState().equals(Rubentxu.State.JUMPING)) {
                ruben.setState(Rubentxu.State.WALKING);
            }
            ruben.getAcceleration().x = -ACCELERATION;
        } else if (keys.get(Keys.RIGHT)) {
            // left is pressed
            ruben.setFacingLeft(false);
            if (!ruben.getState().equals(Rubentxu.State.JUMPING)) {
                ruben.setState(Rubentxu.State.WALKING);
            }
            ruben.getAcceleration().x = ACCELERATION;
        } else {
            if (!ruben.getState().equals(Rubentxu.State.JUMPING)) {
                ruben.setState(Rubentxu.State.IDLE);
            }
            ruben.getAcceleration().x = 0;

        }
        return false;
    }
}
