package com.rubentxu.juegos.html;

import com.rubentxu.juegos.core.DreamsGame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class DreamsGameHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new DreamsGame();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
