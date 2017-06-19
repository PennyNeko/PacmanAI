package entrants.util;

import java.util.BitSet;

import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.info.GameInfo;
import pacman.game.internal.Ghost;

public class AccumGameState {
	// Tracks the currently remaining pills
	private BitSet activePills;
	private BitSet powerPills;

	// Tracks the pill locations seen by pacman
	private BitSet pillsSeen;
	private BitSet powerPillsSeen;
	
	
	public AccumGameState() {
		this.activePills = new BitSet();
		this.powerPills = new BitSet();
		this.pillsSeen = new BitSet();
		this.powerPillsSeen = new BitSet();
	}
	
	public Game update(Game game) {
		GameInfo info = game.getPopulatedGameInfo();
		info.fixGhosts((ghost) -> new Ghost(
                ghost,
                game.getCurrentMaze().lairNodeIndex,
                -1,
                -1,
                MOVE.NEUTRAL
        ));
		
		// Update the pills
		for(int i = info.getPills().nextSetBit(0); i != -1; i = info.getPills().nextSetBit(i + 1)) {
			activePills.set(i);
			pillsSeen.set(i);
		}
		for(int i = info.getPowerPills().nextSetBit(0); i != -1; i = info.getPowerPills().nextSetBit(i + 1)) {
			powerPills.set(i);
			powerPillsSeen.set(i);
		}
		
		if(game.getPillIndex(game.getPacmanCurrentNodeIndex()) >= 0) {
			activePills.clear(game.getPillIndex(game.getPacmanCurrentNodeIndex()));
		}
		if(game.getPowerPillIndex(game.getPacmanCurrentNodeIndex()) >= 0) {
			powerPills.clear(game.getPowerPillIndex(game.getPacmanCurrentNodeIndex()));
		}
		
		for(int i = activePills.nextSetBit(0); i != -1; i = activePills.nextSetBit(i + 1)) {
			info.setPillAtIndex(i, true);
		}
		for(int i = powerPills.nextSetBit(0); i != -1; i = powerPills.nextSetBit(i + 1)) {
			info.setPowerPillAtIndex(i, true);
		}
		
		return game.getGameFromInfo(info);
	}
	
	public int getPillPositionsSeen() {
		return pillsSeen.cardinality();
	}
	
	public int getPowerPillPositionsSeen() {
		return powerPillsSeen.cardinality();
	}
	
	public AccumGameState copy() {
		AccumGameState state = new AccumGameState();
		state.activePills = (BitSet)this.activePills.clone();
		state.powerPills = (BitSet)this.powerPills.clone();
		state.pillsSeen = (BitSet)this.pillsSeen.clone();
		state.powerPillsSeen = (BitSet)this.powerPillsSeen.clone();
		return state;
	}
}
