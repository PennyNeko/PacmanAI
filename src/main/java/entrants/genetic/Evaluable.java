package entrants.genetic;

public interface Evaluable<T>
{
	public T evaluate();
	public Evaluable<T> createOffspring(Evaluable<T> parent);
	void mutate();
}
