/**
 * Copyright 2013 Robin Stumm (serverkorken@googlemail.com, http://dermetfan.bplaced.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rubentxu.juegos.core.utils.dermetfan.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * An {@link AnimatedSprite} holds an {@link Animation} and sets the {@link Texture} of its super type {@link Sprite} to the correct one according to the information in the {@link Animation}.
 * Usage:
 * <pre>Animation animation = new Animation(1 / 3f, new TextureRegion(frame1), new TextureRegion(frame2), new TextureRegion(frame3));
animation.setPlayMode(Animation.LOOP);
animatedSprite = new AnimatedSprite(animation);</pre>
 * Draw it using any of the {@link Sprite Sprite's} draw methods:
<pre>animatedSprite.draw(batch);</pre> 
 * 
 * @author dermetfan
 */
public class AnimatedSprite extends Sprite {

	/** the {@link Animation} to display */
	private Animation animation;

	/** the current time of the {@link Animation} */
	private float time;

	/** if the animation is playing */
	private boolean playing = true;

	/** if the animation should be updated every time it's drawn */
	private boolean autoUpdate = true;

	/** if the size of the previous frame should be kept by the following frames */
	private boolean keepSize;

	/** if a frame should be centered in its previous one's center if it's smaller */
	private boolean centerFrames;

	/**
	 * creates a new {@link AnimatedSprite} with the given {@link Animation}
	 * @param animation the {@link #animation} to use
	 */
	public AnimatedSprite(Animation animation) {
		this(animation, false);
	}

	/**
	 * creates a new {@link AnimatedSprite} with the given {@link Animation}
	 * @param animation the {@link #animation} to use
	 * @param keepSize the {@link #keepSize} to use
	 */
	public AnimatedSprite(Animation animation, boolean keepSize) {
		super(animation.getKeyFrame(0));
		this.animation = animation;
		this.keepSize = keepSize;
	}

	/** updates the {@link AnimatedSprite} with the delta time fetched from {@link Graphics#getDeltaTime()  Gdx.graphics.getDeltaTime()} */
	public void update() {
		update(Gdx.graphics.getDeltaTime());
	}

	/** updates the {@link AnimatedSprite} with the given delta time */
	public void update(float delta) {
		if(playing) {
			setRegion(animation.getKeyFrame(time += delta));
			if(!keepSize)
				setSize(getRegionWidth(), getRegionHeight());
		}
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		if(centerFrames && !keepSize) {
			float x = getX(), y = getY(), width = getWidth(), height = getHeight(), originX = getOriginX(), originY = getOriginY();

			if(autoUpdate)
				update();

			float differenceX = width - getRegionWidth(), differenceY = height - getRegionHeight();
			setOrigin(originX - differenceX / 2, originY - differenceY / 2);
			setBounds(x + differenceX / 2, y + differenceY / 2, width - differenceX, height - differenceY);

			super.draw(spriteBatch);

			setOrigin(originX, originY);
			setBounds(x, y, width, height);
			return;
		}

		if(autoUpdate)
			update();

		super.draw(spriteBatch);
	}

	/** sets {@link #playing} to true */
	public void play() {
		playing = true;
	}

	/** sets {@link #playing} to false */
	public void pause() {
		playing = false;
	}

	/** pauses and sets the {@link #time} to 0 */
	public void stop() {
		playing = false;
		time = 0;
	}

	/** @param time the {@link #time} to go to */
	public void setTime(float time) {
		this.time = time;
	}

	/** @return the current {@link #time} */
	public float getTime() {
		return time;
	}

	/** @return the {@link #animation} */
	public Animation getAnimation() {
		return animation;
	}

	/** @param animation the {@link #animation} to set */
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	/** @return if this {@link AnimatedSprite} is playing */
	public boolean isPlaying() {
		return playing;
	}

	/** @param playing if the {@link AnimatedSprite} should be playing */
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	/** @return if the {@link #animation} has finished playing */
	public boolean isAnimationFinished() {
		return animation.isAnimationFinished(time);
	}

	/** @return the {@link #autoUpdate} */
	public boolean isAutoUpdate() {
		return autoUpdate;
	}

	/** @param autoUpdate the {@link #autoUpdate} to set */
	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	/** @return the {{@link #keepSize} */
	public boolean isKeepSize() {
		return keepSize;
	}

	/** @param keepSize the {@link #keepSize} to set */
	public void setKeepSize(boolean keepSize) {
		this.keepSize = keepSize;
	}

	/** @return the {@link #centerFrames} */
	public boolean isCenterFrames() {
		return centerFrames;
	}

	/** @param centerFrames the {@link #centerFrames} to set */
	public void setCenterFrames(boolean centerFrames) {
		this.centerFrames = centerFrames;
	}

}
