package entrants.mcts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entrants.pacman.aristocat.Evaluator;
import entrants.util.GameUtil;
import pacman.controllers.MASController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MCTSNode {
	private static int GHOST_TIME = 40;
	// TODO: does this need to be thread-safe?
	private static Random rng = new Random();
	
	private Game state;
	private MOVE move;
	private Evaluator eval;
	private List<MCTSNode> childrenVisited;
	private List<MCTSNode> childrenLeft;
	
	public MCTSNode(MOVE move, Evaluator eval) {
		this.state = null;
		this.move = move;
		this.eval = eval;
		this.childrenVisited = new ArrayList<>();
		this.childrenLeft = new ArrayList<>();
	}
	
	public void visit(Game game, MASController ghosts) {
		// Copy and advance the game state with the node's move
		state = game.copy();
		if(move != MOVE.NEUTRAL) {
			do {
				state.advanceGame(move, ghosts.getMove(state.copy(), GHOST_TIME));
			} while(GameUtil.getPossibleMoves(state).contains(move) &&
					!state.isJunction(state.getPacmanCurrentNodeIndex()));
		}
		
		// Initialize the children
		for(MOVE move : GameUtil.getPossibleMoves(this.state)) {
			this.childrenLeft.add(new MCTSNode(move, eval));
		}
	}
	
	public void visitChild(MASController ghosts) {
		// Only if we have any children left to visit
		if(childrenLeft.size() > 0) {
			// For now, RNG it
			// TODO: proper child selection
			int nextIndex = rng.nextInt(childrenLeft.size());
			MCTSNode next = childrenLeft.get(nextIndex);
			next.visit(state, ghosts);
			
			// Update the children lists
			childrenVisited.add(next);
			childrenLeft.remove(nextIndex);
		}
	}
	
	public List<MCTSNode> getChildrenVisited() {
		return childrenVisited;
	}
	
	public List<MCTSNode> getChildrenLeft() {
		return childrenLeft;
	}
	
	public boolean hasChildrenLeft() {
		return !childrenLeft.isEmpty();
	}
	
	public double getValue() {
		//if(childrenVisited.isEmpty()) {
		return eval.evaluate(state.getScore(), state.getPacmanNumberOfLivesRemaining(), state.getTotalTime());
		/*} else {
			int average = 0;
			for(MCTSNode child : childrenVisited) {
				average += child.getValue();
			}
			return (int)(average / (double)childrenVisited.size());
		}*/
	}
	
	public MOVE getMove() {
		return move;
	}
}
