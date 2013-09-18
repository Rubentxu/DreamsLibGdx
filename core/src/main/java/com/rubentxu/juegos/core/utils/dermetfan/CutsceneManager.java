package com.rubentxu.juegos.core.utils.dermetfan;

import com.badlogic.gdx.utils.Array;

/**
 * Manages {@link Cutscene Cutscenes}
 * @author dermetfan
 */
public class CutsceneManager {

	/** the currently active {@link Cutscene Cutscenes} */
	private Array<Cutscene> cutscenes = new Array<Cutscene>();

	/** adds a {@link Cutscene} to the {@link #cutscenes} so it will be {@link #update(float) updated} and calls {@link Cutscene#init() init()} on it*/
	public void start(Cutscene cutscene) {
		cutscenes.add(cutscene);
		cutscene.init();
	}

	/** updates the {@link #cutscenes} */
	public void update(float delta) {
		for(Cutscene cutscene : cutscenes)
			if(cutscene.update(delta))
				end(cutscene);
	}

	/** removes a {@link Cutscene} from the {@link #cutscenes} and calls {@link Cutscene#end() end()} on it */
	public void end(Cutscene cutscene) {
		cutscenes.removeValue(cutscene, true);
		cutscene.end();
	}

	/** @return the {@link #cutscenes} */
	public Array<Cutscene> getCutscenes() {
		return cutscenes;
	}

	/** @param cutscenes the {@link #cutscenes} to set */
	public void setCutscenes(Array<Cutscene> cutscenes) {
		this.cutscenes = cutscenes;
	}

	public static interface Cutscene {

		/** called by {@link CutsceneManager#start(Cutscene)} */
		public void init();

		/**
		 * called by {@link CutsceneManager#update(float)}
		 * @param delta the time passes since last update
		 * @return if the cutscene is finished
		 */
		public boolean update(float delta);

		/** called by {@link CutsceneManager#end(Cutscene)} */
		public void end();

	}

}
