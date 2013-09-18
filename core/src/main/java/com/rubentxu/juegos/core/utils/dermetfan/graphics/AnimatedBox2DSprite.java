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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * A {@link Box2DSprite} using an {@link AnimatedSprite} for animation
 * @author dermetfan
 */
public class AnimatedBox2DSprite extends Box2DSprite {

	/** the {@link AnimatedSprite} used for animation */
	private AnimatedSprite animatedSprite;

	/**
	 * creates a new {@link AnimatedBox2DSprite} with the given {@link AnimatedSprite}
	 * @param animatedSprite the {@link AnimatedSprite} to use 
	 */
	public AnimatedBox2DSprite(AnimatedSprite animatedSprite) {
		super(animatedSprite);
		this.animatedSprite = animatedSprite;
	}

	@Override
	public void draw(SpriteBatch batch, Fixture fixture) {
		if(animatedSprite.isAutoUpdate())
			update();
		super.draw(batch, fixture);
	}

	@Override
	public void draw(SpriteBatch batch, Body body) {
		if(animatedSprite.isAutoUpdate())
			update();
		super.draw(batch, body);
	}

	/** updates with Graphics#getDeltaTime() */
	public void update() {
		update(Gdx.graphics.getDeltaTime());
	}

	/** @param delta the delta time to update with {@link Graphics#getDeltaTime()  Gdx.graphics.getDeltaTime()} */
	public void update(float delta) {
		animatedSprite.update(delta);
		setRegion(animatedSprite);
	}

	/** @return the {@link AnimatedSprite} */
	public AnimatedSprite getAnimatedSprite() {
		return animatedSprite;
	}

	/** @param animatedSprite the {@link AnimatedSprite} to set */
	public void setAnimatedSprite(AnimatedSprite animatedSprite) {
		this.animatedSprite = animatedSprite;
	}

	/** @see AnimatedSprite#play() */
	public void play() {
		animatedSprite.play();
	}

	/** @see AnimatedSprite#pause() */
	public void pause() {
		animatedSprite.pause();
	}

	/** @see AnimatedSprite#stop() */
	public void stop() {
		animatedSprite.stop();
	}

	/** @see AnimatedSprite#setTime(float) */
	public void setTime(float time) {
		animatedSprite.setTime(time);
	}

	/** @see AnimatedSprite#getTime() */
	public float getTime() {
		return animatedSprite.getTime();
	}

	/** @see AnimatedSprite#getAnimation() */
	public Animation getAnimation() {
		return animatedSprite.getAnimation();
	}

	/** @see AnimatedSprite#setAnimation(Animation) */
	public void setAnimation(Animation animation) {
		animatedSprite.setAnimation(animation);
	}

	/** @see AnimatedSprite#isPlaying() */
	public boolean isPlaying() {
		return animatedSprite.isPlaying();
	}

	/** @see AnimatedSprite#setPlaying(boolean) */
	public void setPlaying(boolean playing) {
		animatedSprite.setPlaying(playing);
	}

	/** @see AnimatedSprite#isAutoUpdate() */
	public boolean isAutoUpdate() {
		return animatedSprite.isAutoUpdate();
	}

	/** @see AnimatedSprite#setAutoUpdate(boolean) */
	public void setAutoUpdate(boolean autoUpdate) {
		animatedSprite.setAutoUpdate(autoUpdate);
	}

}
