package entrants.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pacman.game.Game;
import pacman.game.Constants.MOVE;

public class GameUtil {
	public static List<MOVE> getPossibleMoves(Game game) {
		return new ArrayList<>(Arrays.asList(game.getPossibleMoves(game.getPacmanCurrentNodeIndex())));
	}
}
