package entrants.pacman.aristocat;

import java.util.ArrayList;
import java.util.List;

import entrants.genetic.Evaluable;
import entrants.genetic.GeneticAlgorithm;
import entrants.mcts.MCTSTree;
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
public class CatMan extends PacmanController implements Evaluable<Double> {
	
	private Evaluator eval;
	
	public static void main(String[] args) {
        /*Executor po = new Executor(true, true, true);
        po.setDaemon(true);
        po.runGame(new CatMan(new Evaluator(1, 500, -1)), new POCommGhosts(50), true, 40);*/
        
		final int POPULATION_SIZE = 1000;
		final int PARENT_SIZE = 100;
		final double MUTATION_RATE = 1.0;
		final double INITIAL_WIDTHS[] = {
				10.0, 2000.0, 50.0
		};
		
        List<Evaluable<Double>> initialPopulation = new ArrayList<>(POPULATION_SIZE);
        for(int i = 0; i < POPULATION_SIZE; ++i) {
        	initialPopulation.add(new CatMan(new Evaluator().randomize(INITIAL_WIDTHS)));
        }
        
        GeneticAlgorithm genetics = new GeneticAlgorithm(initialPopulation, PARENT_SIZE, MUTATION_RATE);
        for(int i = 0; i < 10; ++i) {
        	genetics.performIteration();
        	System.out.println("Best individual: " + ((CatMan)genetics.getBestIndividual()).eval);
        }
    }
	
	public CatMan(Evaluator eval) {
		this.eval = eval;
	}
	
    public MOVE getMove(Game game, long timeDue) {
    	//if(game.isJunction(game.getPacmanCurrentNodeIndex())) {
        	MCTSTree tree = new MCTSTree(game, eval);
        	tree.simulate(20);
        	
            return tree.getBestMove();
    	/*} else {
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
    	}*/
    }

	@Override
	public Double evaluate()
	{
		return new Double(1);
	}

	@Override
	public Evaluable<Double> createOffspring(Evaluable<Double> parent)
	{
		// TODO: buggy!
		return new CatMan(eval.combine(((CatMan) parent).eval));
	}

	@Override
	public void mutate(double chance)
	{
		eval.mutate();
	}
}