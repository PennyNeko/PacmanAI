package entrants.genetic;

/**
 * Interface to mark a class as a GA individual.
 * @author Florian Bethe
 *
 * @param <T> Type of evaluation result
 */
public interface GAIndividual<T>
{
	/**
	 * Evaluates the individual's fitness.
	 * @return Fitness value
	 */
	public T evaluate();
	
	/**
	 * Creates a new offspring with the given parent.
	 * @param parent Other parent
	 * @return New offspring
	 */
	public GAIndividual<T> createOffspring(GAIndividual<T> parent);
	
	/**
	 * Mutates the individual.
	 */
	void mutate();
}
