package entrants.mcts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacman.controllers.MASController;
import pacman.controllers.examples.po.POCommGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.info.GameInfo;
import pacman.game.internal.Ghost;

public class MCTSTree {
	//private static int INTERATIONS_PER_NODE = 1;
	private static Random rng = new Random();
	private static long TIME_BUFFER = 5;
	private Game game;
	private MASController ghosts;
	private MCTSNode root;
	
	public MCTSTree(Game game) {
		GameInfo info = game.getPopulatedGameInfo();
		info.fixGhosts((ghost) -> new Ghost(
                ghost,
                game.getCurrentMaze().lairNodeIndex,
                -1,
                -1,
                MOVE.NEUTRAL
        ));
		this.game = game.getGameFromInfo(info);
		this.ghosts = new POCommGhosts(50);
		this.root = null;
	}
	
	public void simulate(long timeAvailable) {
		long startTime = System.currentTimeMillis();
		
		// Initialize the tree with a neutral root
		root = new MCTSNode(MOVE.NEUTRAL);
		root.visit(game, ghosts);
		
		while(root.hasChildrenLeft()) {
			root.visitChild(ghosts);
		}
		
		MCTSNode next = root;
		for(int i = 0; i < 50; ++i) {
			next = getNextNode(next);
			next.visitChild(ghosts);
		}
	}
	
	public MOVE getBestMove() {
		List<MOVE> best = new ArrayList<>();
		int bestValue = Integer.MIN_VALUE;
		
		for(MCTSNode curr : root.getChildrenVisited()) {
			int currValue = curr.getValue();
			if(curr.getMove() == MOVE.UP) {
				//System.out.println(currValue + " " + bestValue);
			}
			if(currValue == bestValue) {
				best.add(curr.getMove());
			} else if(currValue > bestValue) {
				best.clear();
				bestValue = currValue;
				best.add(curr.getMove());
			}
		}
		
		if(best.size() > 1) {
			// Check if one of them is our previous move
			for(MOVE move : best) {
				if(move.equals(game.getPacmanLastMoveMade())) {
					return move;
				}
			}
			
			// RNG it
			return best.get(rng.nextInt(best.size()));
		} else {
			return best.get(0);
		}
	}
	
	private MCTSNode getNextNode(MCTSNode last) {
		
		while(!last.getChildrenVisited().isEmpty()) {
			if(rng.nextInt(last.getChildrenLeft().size() + last.getChildrenVisited().size())
					< last.getChildrenLeft().size()) {
				return last;
			} else {
				last = last.getChildrenVisited().get(rng.nextInt(last.getChildrenVisited().size())); 
			}
		}
		// For now do depth first search
		/*while(!last.getChildrenVisited().isEmpty()) {
			last = last.getChildrenVisited().get(rng.nextInt(last.getChildrenVisited().size()));
		}*/
		
		return last;
	}
}
