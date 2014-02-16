package com.rubentxu.juegos.core.controladores;

import com.rubentxu.juegos.core.managers.world.HeroManager;
import com.rubentxu.juegos.core.managers.world.PlatformManager;
import com.rubentxu.juegos.core.modelo.World;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WorldControllerUpdateTest {

	WorldController worldController;
	HeroManagerMock rubentxuMock;
	PlatformManagerMock platformMock;

	@Before
	public void setUp() throws Exception {
		rubentxuMock = new HeroManagerMock();
		platformMock = new PlatformManagerMock();
		worldController = new WorldController(game, new World());

        worldController.setRubenManager(rubentxuMock);
        worldController.setPlatformManager(platformMock);
	}

	class PlatformManagerMock extends PlatformManager {
		float delta = -1f;

		@Override
		public void update(float delta) {
			this.delta = delta;
		}

		public boolean updateWasCalled() {
			return this.delta != -1f;
		}

	}

	class HeroManagerMock extends HeroManager {

		public HeroManagerMock() {
			super(null);
		}

		float delta = -1f;

		@Override
		public void update(float delta) {
			this.delta = delta;
		}

		public boolean updateWasCalled() {
			return this.delta != -1f;
		}
	}

	@Test
	public void testRubentxuAndPlatformManagersAreCalledWhenUpdateWorld() {
		this.worldController.update(0f);

		assertTrue(this.rubentxuMock.updateWasCalled());
		assertTrue(this.platformMock.updateWasCalled());
	}

	@Test
	public void testRubentxuAndPlatformManagersGetsDeltaWhenUpdateWorld() {
		this.worldController.update(0.1f);

		assertEquals(0.1f, this.rubentxuMock.delta, 0f);
		assertEquals(0.1f, this.platformMock.delta, 0f);
	}

}
