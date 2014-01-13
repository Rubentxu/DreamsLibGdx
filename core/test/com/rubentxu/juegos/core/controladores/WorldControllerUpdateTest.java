package com.rubentxu.juegos.core.controladores;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.rubentxu.juegos.core.managers.world.PlatformManager;
import com.rubentxu.juegos.core.managers.world.RubentxuManager;

public class WorldControllerUpdateTest {

	WorldController worldController;
	RubentxuManagerMock rubentxuMock;
	PlatformManagerMock platformMock;

	@Before
	public void setUp() throws Exception {
		rubentxuMock = new RubentxuManagerMock();
		platformMock = new PlatformManagerMock();
		worldController = new WorldController(world);

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

	class RubentxuManagerMock extends RubentxuManager {

		public RubentxuManagerMock() {
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
