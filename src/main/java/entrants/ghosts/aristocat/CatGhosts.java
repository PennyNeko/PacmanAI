package entrants.ghosts.aristocat;

import java.util.EnumMap;

import pacman.controllers.IndividualGhostController;
import pacman.controllers.MASController;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class CatGhosts extends MASController {
	public CatGhosts(int TICK_THRESHOLD) {
        super(true, new EnumMap<GHOST, IndividualGhostController>(GHOST.class));
        controllers.put(GHOST.BLINKY, new GeneralGhost(GHOST.BLINKY));
        controllers.put(GHOST.INKY, new GeneralGhost(GHOST.INKY));
        controllers.put(GHOST.PINKY, new GeneralGhost(GHOST.PINKY));
        controllers.put(GHOST.SUE, new GeneralGhost(GHOST.SUE));
    }
}
