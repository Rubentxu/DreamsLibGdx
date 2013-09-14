package com.rubentxu.juegos.core.servicios;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public abstract class Assets {

	public static final AssetManager manager = new AssetManager();

	public static Texture ball, someImage, japanischeFlagge, bruteWithTcng, drop, luigiFront, luigiSide, particle, player, animationTest, tank, tankCannon;

	static {
		manager.load("imagenes/test/ball.png", Texture.class);

		manager.load("imagenes/test/japanischeFlagge.jpg", Texture.class);
		/*manager.load("imagenes/test/bruteWithTcng.png", Texture.class);
		manager.load("imagenes/test/drop.png", Texture.class);
		manager.load("imagenes/test/luigi front.png", Texture.class);
		manager.load("imagenes/test/luigi side.png", Texture.class);
		manager.load("imagenes/test/particle.png", Texture.class);
		manager.load("imagenes/test/player.png", Texture.class);
		manager.load("imagenes/test/animationTest.png", Texture.class);
		manager.load("imagenes/test/tank.png", Texture.class);
		manager.load("imagenes/test/tankCannon.png", Texture.class);*/
	}

	public static boolean update() {
		if(manager.update()) {
			set();
			return true;
		}
		return false;
	}

	public static void set() {
		ball = manager.get("imagenes/test/ball.png", Texture.class);

		japanischeFlagge = manager.get("imagenes/test/japanischeFlagge.jpg", Texture.class);
		/*bruteWithTcng = manager.get("imagenes/test/bruteWithTcng.png", Texture.class);
		drop = manager.get("imagenes/test/drop.png", Texture.class);
		luigiFront = manager.get("imagenes/test/luigi front.png", Texture.class);
		luigiSide = manager.get("imagenes/test/luigi side.png", Texture.class);
		particle = manager.get("imagenes/test/particle.png", Texture.class);
		player = manager.get("imagenes/test/player.png", Texture.class);
		animationTest = manager.get("imagenes/test/animationTest.png", Texture.class);
		tank = manager.get("imagenes/test/tank.png", Texture.class);
		tankCannon = manager.get("imagenes/test/tankCannon.png", Texture.class);*/
	}

	public static void dispose() {
		ball.dispose();

		japanischeFlagge.dispose();
		/*bruteWithTcng.dispose();
		drop.dispose();
		luigiFront.dispose();
		luigiSide.dispose();
		particle.dispose();
		player.dispose();
		animationTest.dispose();
		tank.dispose();
		tankCannon.dispose();*/
	}

}
