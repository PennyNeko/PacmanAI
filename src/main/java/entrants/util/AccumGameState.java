package entrants.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import pacman.game.Game;
import pacman.game.info.GameInfo;

public class AccumGameState {
	private Set<Integer> activePills;
	private Set<Integer> powerPills;
	
	public AccumGameState() {
		this.activePills = new HashSet<>();
		this.powerPills = new HashSet<>();
	}
	
	public void update(Game game) {
		GameInfo info = game.getPopulatedGameInfo();
		
		// TODO: Update the pills
		//activePills.addAll(Arrays.asList(game.getPillIndices()));
		//powerPills.addAll(Arrays.asList(game.getPowerPillIndices()));
	}
}
