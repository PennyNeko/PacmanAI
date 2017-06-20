package entrants.pacman.aristocat;

import java.util.ArrayList;
import java.util.List;

import entrants.genetic.GAIndividual;
import entrants.genetic.GeneticAlgorithm;
import entrants.pacman.aristocat.mcts.MCTSTree;
import entrants.util.AccumGameState;
import entrants.util.Evaluator;
import pacman.Executor;
import pacman.controllers.MASController;
import pacman.controllers.PacmanController;
import pacman.controllers.examples.po.POCommGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getMove() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., entrants.pacman.username).
 */
public class CatMan extends PacmanController implements GAIndividual<Double> {
	
	private Evaluator eval;
	private AccumGameState state;
	
	public static void main(String[] args) {
		final int POPULATION_SIZE = 10;
		final int PARENT_SIZE = 5;
		final double MUTATION_RATE = 1.0;
		final double INITIAL_WIDTHS[] = {
				10.0, 200.0, 10.0, 5.0, 70.0, 100.0
		};
		
        List<GAIndividual<Double>> initialPopulation = new ArrayList<>(POPULATION_SIZE);
        for(int i = 0; i < POPULATION_SIZE; ++i) {
        	initialPopulation.add(new CatMan(new Evaluator(INITIAL_WIDTHS.length).randomize(INITIAL_WIDTHS)));
        }
        
        GeneticAlgorithm genetics = new GeneticAlgorithm(initialPopulation, PARENT_SIZE, MUTATION_RATE);
        for(int i = 0; i < 10; ++i) {
        	genetics.performIteration();
        	CatMan best = (CatMan)genetics.getBestIndividualTotal();
        	System.out.println("Best individual: " + best.eval);
        }
    }
	
	public CatMan(Evaluator eval) {
		this.eval = eval;
		this.state = new AccumGameState();
	}
	
    public MOVE getMove(Game game, long timeDue) {
    	//if(game.isJunction(game.getPacmanCurrentNodeIndex())) {
    	game = state.update(game);
    	
        MCTSTree tree = new MCTSTree(game, state, eval);
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
		System.out.println("Evaluating");
		Executor po = new Executor(true, true, true);
        po.setDaemon(true);
        //return po.runGameTimedSpeedOptimised(new CatMan(eval),
        //		new POCommGhosts(50), true, false, "").getAverage();
        return po.runExperiment(new CatMan(eval), new POCommGhosts(10), 1, "")[0].getAverage();
	}

	@Override
	public GAIndividual<Double> createOffspring(GAIndividual<Double> parent)
	{
		return new CatMan(eval.combine(((CatMan) parent).eval));
	}

	@Override
	public void mutate()
	{
		eval.mutate();
	}
}