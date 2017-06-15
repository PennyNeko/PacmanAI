package entrants.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GeneticAlgorithm
{
	
	private static int factorial(int n, int m) {
		int res = 1;
		for(int i = m; i <= n; ++i) {
			res *= i;
		}
		return res;
	}
	
	private List<Evaluable<Double>> population;
	private final int PARENT_SIZE;
	private final double MUTATION_CHANCE;
	
	public GeneticAlgorithm(List<Evaluable<Double>> population, int parentSize, double mutationChance) {
		this.population = population;
		this.PARENT_SIZE = parentSize;
		this.MUTATION_CHANCE = mutationChance;
	}
	
	public void performIteration() {
		List<Evaluable<Double>> parents = selectParents(PARENT_SIZE);
		this.population = this.mutatePopulation(
				selectNewPopulation(
						parents, createChildren(parents)
					));
	}
	
	public Evaluable<Double> getBestIndividual() {
		List<Double> fitnesses = GeneticAlgorithm.evaluatePopulation(population);
		int best = 0;
		for(int i = 1; i < fitnesses.size(); ++i) {
			if(fitnesses.get(i) > fitnesses.get(best)) {
				best = i;
			}
		}
		
		return population.get(best);
	}
	
	private static List<Double> evaluatePopulation(List<Evaluable<Double>> pop) {
		List<Double> fitness = new ArrayList<>(pop.size());
		
		for(Evaluable<Double> individual : pop) {
			fitness.add(individual.evaluate());
		}
		
		return fitness;
	}
	
	private List<Evaluable<Double>> selectParents(int parentSize) {
		return rouletteWheelSelection(this.population, parentSize);
	}
	
	private List<Evaluable<Double>> createChildren(List<Evaluable<Double>> parents) {
		List<Evaluable<Double>> children = new ArrayList<>(factorial(parents.size() - 2, parents.size() - 2));
		
		for(Evaluable<Double> parent1 : parents) {
			for(Evaluable<Double> parent2 : parents) {
				children.add(parent1.createOffspring(parent2));
			}
		}
		
		return children;
	}
	
	private List<Evaluable<Double>> selectNewPopulation(List<Evaluable<Double>> parents, List<Evaluable<Double>> children) {
		//List<Evaluable<Double>> pop = new ArrayList<>(POPULATION_SIZE);
		return rouletteWheelSelection(children, population.size());
	}
	
	private List<Evaluable<Double>> mutatePopulation(List<Evaluable<Double>> pop) {
		for(Evaluable<Double> individual : pop) {
			if(ThreadLocalRandom.current().nextDouble() < MUTATION_CHANCE) {
				individual.mutate();
			}
		}
		return pop;
	}
	
	private static List<Evaluable<Double>> rouletteWheelSelection(List<Evaluable<Double>> individuals, int n) {
		n = Math.min(individuals.size(), Math.max(0, n));
		
		List<Evaluable<Double>> selection = new ArrayList<>(n);
		List<Double> fitnesses = GeneticAlgorithm.evaluatePopulation(individuals);
		
		double totalFitness = 0;
		for(Double fitness : fitnesses) {
			totalFitness += fitness;
		}
		
		for(int i = 0; i < n; ++i) {
			double prob = ThreadLocalRandom.current().nextDouble(totalFitness);
			
			for(int j = 0; j < fitnesses.size(); ++j) {
				prob -= fitnesses.get(j);
				if(prob <= 0) {
					selection.add(individuals.get(j));
					break;
				}
			}
		}
		
		return selection;
	}
}
