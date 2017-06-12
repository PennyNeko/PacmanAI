package entrants.pacman.aristocat;

import java.util.List;

import entrants.mcts.MCTSTree;
import entrants.util.GameUtil;
import pacman.Executor;
import pacman.controllers.PacmanController;
import pacman.controllers.examples.po.POCommGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getMove() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., entrants.pacman.username).
 */
public class MyPacMan extends PacmanController {
	
	public static void main(String[] args) {
        Executor po = new Executor(true, true, true);
        po.setDaemon(true);
        po.runGame(new MyPacMan(), new POCommGhosts(50), true, 40);
    }
	
    public MOVE getMove(Game game, long timeDue) {
    	if(game.isJunction(game.getPacmanCurrentNodeIndex())) {
        	MCTSTree tree = new MCTSTree(game);
        	tree.simulate(20);
        	
            return tree.getBestMove();
    	} else {
    		List<MOVE> moves = GameUtil.getPossibleMoves(game);
    		if(moves.contains(game.getPacmanLastMoveMade())) {
    			return game.getPacmanLastMoveMade();
    		} else {
    			for(MOVE move : moves) {
    				if(move != game.getPacmanLastMoveMade().opposite()) {
    					return move;
    				}
    			}
    			
    			return game.getPacmanLastMoveMade().opposite();
    		}
    	}
    }
}