package entrants.ghosts.aristocat;

import java.util.EnumMap;

import pacman.controllers.IndividualGhostController;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GeneralGhost extends IndividualGhostController {
	
	public static void main(String[] args) {
		
	}
	
	EnumMap<Constants.GHOST, IndividualGhostController> ghosts;
	
	public GeneralGhost(Constants.GHOST ghost) {
		super(ghost);
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		// TODO Auto-generated method stub
		return null;
	}
}
