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


import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.*;

import java.util.*;

import static com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DUtils.*;

/**
 * A {@link Box2DSprite} is a {@link Sprite} with additional drawing information and the abililty to draw itself on a given {@link Body} or {@link Fixture}.
 * It is supposed to be put in the user data of {@link Fixture Fixtures} or {@link Body Bodies}. The Fixture's user data is recommend though to make use of caching which will increase performance!
 * 
 * @author dermetfan
 */
public class Box2DSprite extends Sprite {

	/** the z index for sorted drawing */
	private float z;

	/** if the width and height should be adjusted to those of the {@link Body} or {@link Fixture} this {@link Box2DSprite} is attached to (true by default) */
	private boolean adjustWidth = true, adjustHeight = true;

	/** if the origin of this {@link Box2DSprite} should be used when it's drawn (false by default) */
	private boolean useOriginX, useOriginY;

	/** a user data object replacing the user data that this {@link Box2DSprite} replaces if it's set as user data */
	private Object userData;

	/** @see Sprite#Sprite() */
	public Box2DSprite() {
		super();
	}

	/** @see Sprite#Sprite(Texture, int, int) */
	public Box2DSprite(Texture texture, int srcWidth, int srcHeight) {
		super(texture, srcWidth, srcHeight);
	}

	/** @see Sprite#Sprite(Texture, int, int, int, int) */
	public Box2DSprite(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
		super(texture, srcX, srcY, srcWidth, srcHeight);
	}

	/** @see Sprite#Sprite(TextureRegion, int, int, int, int) */
	public Box2DSprite(TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight) {
		super(region, srcX, srcY, srcWidth, srcHeight);
	}

	/** @see Sprite#Sprite(Texture) */
	public Box2DSprite(Texture texture) {
		super(texture);
	}

	/** @see Sprite#Sprite(TextureRegion) */
	public Box2DSprite(TextureRegion region) {
		super(region);
	}

	/** @see Sprite#Sprite(Sprite) */
	public Box2DSprite(Sprite sprite) {
		super(sprite);
	}

	/** a temporary map to store the bodies / fixtures which user data Box2DSprites are in in {@link #draw(SpriteBatch, World, boolean)}*/
	private static ObjectMap<Box2DSprite, Object> tmpZBox2DSpriteMap = new ObjectMap<Box2DSprite, Object>(0);

	/** a {@link Comparator} used to sort {@link Box2DSprite Box2DSprites} by their {@link Box2DSprite#z z index} in {@link #draw(SpriteBatch, World)} */
	private static Comparator<Box2DSprite> zComparator = new Comparator<Box2DSprite>() {

		@Override
		public int compare(Box2DSprite s1, Box2DSprite s2) {
			return s1.z - s2.z > 0 ? 1 : -1;
		}

	};

	/** temporary variable used in {@link #draw(SpriteBatch, World)} */
	private static Array<Body> tmpBodies = new Array<Body>(0);

	/** @see #draw(SpriteBatch, World, boolean) */
	public static void draw(SpriteBatch batch, World world) {
		draw(batch, world, false);
	}

	/** draws all the {@link Box2DSprite Box2DSprites} on the {@link Body} or {@link Fixture} that hold them in their user data in the given {@link World} */
	public static void draw(SpriteBatch batch, World world, boolean sortByZ) {
		world.getBodies(tmpBodies);

		if(sortByZ) {
			for(Body body : tmpBodies) {
				if(body.getUserData() instanceof Box2DSprite)
					tmpZBox2DSpriteMap.put((Box2DSprite) body.getUserData(), body);
				for(Fixture fixture : body.getFixtureList())
					if(fixture.getUserData() instanceof Box2DSprite)
						tmpZBox2DSpriteMap.put((Box2DSprite) fixture.getUserData(), fixture);
			}

			Array<Box2DSprite> keys = tmpZBox2DSpriteMap.keys().toArray();
			keys.sort(zComparator);

			for(Box2DSprite key : keys) {
				Object value = tmpZBox2DSpriteMap.get(key);
				if(value instanceof Body)
					key.draw(batch, (Body) value);
				else
					key.draw(batch, (Fixture) value);
			}
			tmpZBox2DSpriteMap.clear();
		} else {
			for(Body body : tmpBodies) {
				if(body.getUserData() instanceof Box2DSprite)
					((Box2DSprite) body.getUserData()).draw(batch, body);
				for(Fixture fixture : body.getFixtureList())
					if(fixture.getUserData() instanceof Box2DSprite)
						((Box2DSprite) fixture.getUserData()).draw(batch, fixture);
			}
		}
	}

	/** cached position {@link Vector2} used in {@link #draw(SpriteBatch, Fixture)} for performance */
	private final Vector2 tmpPosition = new Vector2();

	/** draws this {@link Box2DSprite} on the given {@link Fixture} */
	public void draw(SpriteBatch batch, Fixture fixture) {
		batch.setColor(getColor());
		float width = width(fixture), height = height(fixture);
		tmpPosition.set(position(fixture));
		batch.draw(this, tmpPosition.x - width / 2 + getX(), tmpPosition.y - height / 2 + getY(), isUseOriginX() ? getOriginX() : width / 2, isUseOriginY() ? getOriginY() : height / 2, isAdjustWidth() ? width : getWidth(), isAdjustHeight() ? height : getHeight(), getScaleX(), getScaleY(), fixture.getBody().getAngle() * MathUtils.radiansToDegrees + getRotation());
	}

	/** cached center {@link Vector2} used in {@link #draw(SpriteBatch, Body)} for performance */
	private final Vector2 tmpCenter = new Vector2();

	/** draws this {@link Box2DSprite} on the given {@link Body} */
	public void draw(SpriteBatch batch, Body body) {
		batch.setColor(getColor());
		float width = width(body), height = height(body);
		tmpCenter.set(minX(body) + width / 2, minY(body) + height / 2);
		tmpPosition.set(body.getWorldPoint(tmpCenter));
		batch.draw(this, tmpPosition.x - width / 2 + getX(), tmpPosition.y - height / 2 + getY(), isUseOriginX() ? getOriginX() : width / 2, isUseOriginY() ? getOriginY() : height / 2, isAdjustWidth() ? width : getWidth(), isAdjustHeight() ? height : getHeight(), getScaleX(), getScaleY(), body.getAngle() * MathUtils.radiansToDegrees + getRotation());
	}

	/** @return the {@link #z} */
	public float getZ() {
		return z;
	}

	/** @param z the {@link #z} to set */
	public void setZ(float z) {
		this.z = z;
	}

	/** @return if the width should be adjusted to those of the {@link Fixture} this {@link Box2DSprite} is attached to */
	public boolean isAdjustWidth() {
		return adjustWidth;
	}

	/** @param adjustWidth if the width should be adjusted to that of the {@link Body} or {@link Fixture} this {@link Box2DSprite} is attached to */
	public void setAdjustWidth(boolean adjustWidth) {
		this.adjustWidth = adjustWidth;
	}

	/** @return if the height should be adjusted to that of the {@link Body} or {@link Fixture} this {@link Box2DSprite} is attached to */
	public boolean isAdjustHeight() {
		return adjustHeight;
	}

	/** @param adjustHeight if the height should be adjusted to that of the {@link Body} or {@link Fixture} this {@link Box2DSprite} is attached to */
	public void setAdjustHeight(boolean adjustHeight) {
		this.adjustHeight = adjustHeight;
	}

	/** @return the if the x origin of this {@link Box2DSprite} should be used when it's being drawn */
	public boolean isUseOriginX() {
		return useOriginX;
	}

	/** @param useOriginX if the x origin of this {@link Box2DSprite} should be used when it's being drawn */
	public void setUseOriginX(boolean useOriginX) {
		this.useOriginX = useOriginX;
	}

	/** @return if the y origin of this {@link Box2DSprite} should be used when it's being drawn */
	public boolean isUseOriginY() {
		return useOriginY;
	}

	/** @param useOriginY if the y origin of this {@link Box2DSprite} should be used when it's being drawn */
	public void setUseOriginY(boolean useOriginY) {
		this.useOriginY = useOriginY;
	}

	/** @see Sprite#setSize(float, float) */
	public void setWidth(float width) {
		setSize(width, getHeight());
	}

	/** @see Sprite#setSize(float, float) */
	public void setHeight(float height) {
		setSize(getWidth(), height);
	}

	/** @return the userData */
	public Object getUserData() {
		return userData;
	}

	/** @param userData the userData to set */
	public void setUserData(Object userData) {
		this.userData = userData;
	}

	/** @return the {@link #zComparator} */
	public static Comparator<Box2DSprite> getZComparator() {
		return zComparator;
	}

	/** @param zComparator the {@link #zComparator} to set */
	public static void setZComparator(Comparator<Box2DSprite> zComparator) {
		Box2DSprite.zComparator = zComparator;
	}

}
