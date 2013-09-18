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

package com.rubentxu.juegos.core.utils.dermetfan.math;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;

/**
 * provides noise algorithms
 * @author dermetfan
 */
public abstract class Noise {

	/** the seed used by {@link #random} */
	private static long seed = -1;

	/** if {@link #seed} should be used (false by default) */
	private static boolean seedEnabled;

	/** the {@link Random} used to generate pseudo-random values */
	private static Random random = new Random();

	/**
	 * randomizes a given float array using the midpoint-displacement algorithm
	 * @param values the float array to randomize
	 * @param range the range used for random values
	 * @param smoothness the smoothness of the transitions
	 * @return the randomized float array
	 */
	public static float[] midpointDisplacement(float[] values, float range, float smoothness) {
		for(int i = 0; i < values.length; i++, range /= smoothness)
			values[i] = (values[(i - 1 + values.length) % values.length] + values[(i + 1) % values.length]) / 2 + (random.nextFloat() * range * 2 - range);
		return values;
	}

	/**
	 * generates a height map using the midpoint-displacement algorithm
	 * @param n level of detail
	 * @param scaleX scale of the x axis
	 * @param scaleY scale of the y axis
	 * @param range the range used for random values
	 * @param smoothness the smoothness of the transitions
	 * @param initializeRandomly if init should be ignored to use random values instead
	 * @param init the value used to initialize the grid
	 * @return a height map generated using the midpoint-displacement algorithm
	 */
	public static float[][] midpointDisplacement(int n, int scaleX, int scaleY, float range, float smoothness, boolean initializeRandomly, float init) {
		int x, y, power = (int) Math.pow(2, n), width = scaleX * power + 1, height = scaleY * power + 1, step;
		float[][] map = new float[width][height];
		boolean sy, sx;

		for(x = 0; x < width; x += power)
			for(y = 0; y < height; y += power)
				map[x][y] = initializeRandomly ? MathUtils.random(range * 2) : init;

		for(step = power / 2; step > 0; step /= 2, range /= smoothness) {
			sx = false;
			for(x = 0; x < width; x += step, sx = !sx) {
				sy = false;
				for(y = 0; y < height; y += step, sy = !sy)
					if(sx || sy)
						if(sx && sy)
							map[x][y] = (map[x - step][y - step] + map[x + step][y - step] + map[x - step][y + step] + map[x + step][y + step]) / 4 + MathUtils.random(-range, range);
						else if(sx)
							map[x][y] = (map[x - step][y] + map[x + step][y]) / 2 + MathUtils.random(-range, range);
						else
							map[x][y] = (map[x][y - step] + map[x][y + step]) / 2 + MathUtils.random(-range, range);
			}
		}
		return map;
	}
	
	/**
	 * generates a height map using the diamond-square algorithm
	 * @param n level of detail
	 * @param scaleX scale of the x axis
	 * @param scaleY scale of the y axis
	 * @param range the range used for random values
	 * @param smoothness the smoothness of the transitions
	 * @param wrapX if the map should wrap on the x axis
	 * @param wrapY if the map should wrap on the y axis
	 * @param initializeRandomly if init should be ignored to use random values instead
	 * @param init the value used to initialize the grid
	 * @return a height map generated using the diamond-square algorithm
	 */
	public static float[][] diamondSquare(int n, int scaleX, int scaleY, float smoothness, float range, boolean wrapX, boolean wrapY, boolean initializeRandomly, float init) {
		int power = (int) Math.pow(2, n), width = scaleX * power + 1, height = scaleY * power + 1, x, y;
		float map[][] = new float[width][height], avg;

		// seed the grid
		for(x = 0; x < width; x += power)
			for(y = 0; y < height; y += power)
				map[x][y] = initializeRandomly ? random.nextFloat() * range * 2 - range : init;

		for(power /= 2; power > 0; power /= 2, range /= smoothness) {
			// square step
			for(x = power; x < width; x += power * 2)
				for(y = power; y < height; y += power * 2)
					map[x][y] = (map[x - power][y - power] + map[x - power][y + power] + map[x + power][y + power] + map[x + power][y - power]) / 4 + (random.nextFloat() * range * 2 - range);

			// diamond step
			for(x = 0; x < width - (wrapX ? 1 : 0); x += power)
				for(y = power * (1 - x / power % 2); y < height - (wrapY ? 1 : 0); y += power * 2) {
					map[x][y] = (avg = (map[(x - power + width - 1) % (width - 1)][y] + map[(x + power) % (width - 1)][y] + map[x][(y - power + height - 1) % (height - 1)] + map[x][(y + power) % (height - 1)]) / 4) + (random.nextFloat() * range * 2 - range);

					if(wrapX && x == 0)
						map[width - 1][y] = avg;
					if(wrapY && y == 0)
						map[x][height - 1] = avg;
				}
		}

		return map;
	}

	/** @param seedEnabled if {@link #seed} should be used */
	public static void setSeedEnabled(boolean seedEnabled) {
		if(Noise.seedEnabled = seedEnabled)
			random.setSeed(seed);
		else
			random = new Random();
	}

	/** @return the {@link #seedEnabled} */
	public static boolean isSeedEnabled() {
		return seedEnabled;
	}

	/** @return the {@link #seed} */
	public static long getSeed() {
		return seed;
	}

	/** @param seed the {@link #seed} to set */
	public static void setSeed(long seed) {
		random.setSeed(Noise.seed = seed);
	}

	/** @return the {@link #random} */
	public static Random getRandom() {
		return random;
	}

}
