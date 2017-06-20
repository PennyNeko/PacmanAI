package entrants.ghosts.aristocat.mcts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entrants.util.AccumGameState;
import entrants.util.Evaluator;
import pacman.controllers.MASController;
import pacman.controllers.PacmanController;
import pacman.controllers.examples.Legacy2TheReckoning;
import pacman.controllers.examples.RandomNonRevPacMan;
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
	private AccumGameState state;
	private PacmanController pacman;
	private Evaluator eval;
	private MCTSNode root;
	
	public MCTSTree(Game game, AccumGameState state, Evaluator eval) {
		GameInfo info = game.getPopulatedGameInfo();
		this.game = game.getGameFromInfo(info);
		this.state = state;
		this.pacman = new RandomNonRevPacMan();
		this.eval = eval;
		this.root = null;
	}
	
	public void simulate(long timeAvailable) {
		long startTime = System.currentTimeMillis();
		
		// Initialize the tree with a neutral root
		root = new MCTSNode(MOVE.NEUTRAL, eval);
		root.visit(game, state, pacman);
		
		while(root.hasChildrenLeft()) {
			root.visitChild(pacman);
		}
		
		MCTSNode next = root;
		//for(int i = 0; i < 100; ++i) {
		while((System.currentTimeMillis() - startTime) < (timeAvailable - TIME_BUFFER)) {
			next = getNextNode(next);
			next.visitChild(ghosts);
		}
	}
	
	public MOVE getBestMove() {
		List<MOVE> best = new ArrayList<>();
		double bestValue = Double.MIN_VALUE;

		//System.out.print("Values: ");
		for(MCTSNode curr : root.getChildrenVisited()) {
			double currValue = curr.getValue();
			//System.out.print(curr.getMove() + " " + currValue + ", ");
			if(currValue == bestValue) {
				best.add(curr.getMove());
			} else if(currValue > bestValue) {
				best.clear();
				bestValue = currValue;
				best.add(curr.getMove());
			}
		}
		//System.out.println();
		
		if(best.isEmpty()) {
			return MOVE.NEUTRAL;
		} else if(best.size() > 1) {
			// Check if one of them is our previous move
			for(MOVE move : best) {
				if(move.equals(game.getPacmanLastMoveMade())) {
					return move;
				}
			}
			
			// RNG it
			return best.get(rng.nextInt(best.size()));
			//return best.get(0);
		} else {
			return best.get(0);
		}
	}
	
	private MCTSNode getNextNode(MCTSNode last) {
		/*while(!last.getChildrenVisited().isEmpty()) {
			if(rng.nextInt(last.getChildrenLeft().size() + last.getChildrenVisited().size())
					< last.getChildrenLeft().size()) {
				return last;
			} else {
				last = last.getChildrenVisited().get(rng.nextInt(last.getChildrenVisited().size())); 
			}
		}*/
		// For now do depth first search
		while(!last.getChildrenVisited().isEmpty()) {
			last = last.getChildrenVisited().get(rng.nextInt(last.getChildrenVisited().size()));
		}
		
		return last;
	}
}
