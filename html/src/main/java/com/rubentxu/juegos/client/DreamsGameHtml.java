package com.rubentxu.juegos.client;

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
    public void log(String tag, String message, Throwable exception) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getLogLevel() {
        return 1;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
	public GwtApplicationConfiguration getConfig () {

        GwtApplicationConfiguration config = new GwtApplicationConfiguration(800, 600);
        config.stencil = true;
        return config;
	}
}
