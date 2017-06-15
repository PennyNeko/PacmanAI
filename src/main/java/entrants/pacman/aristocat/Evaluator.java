package entrants.pacman.aristocat;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

public class Evaluator
{
	private double weights[];
	private double weightSum;
	
	public Evaluator(int size) {
		this.weights = new double[size];
		for(int i = 0; i < size; ++i) {
			weights[i] = 1.0;
		}
		this.weightSum = size;
	}
	
	public Evaluator(double... weights) {
		this.weights = weights;
		this.weightSum = DoubleStream.of(weights).sum();
	}
	
	public Evaluator randomize(double...widths) {
		for(int i = 0; i < weights.length; ++i) {
			weights[i] = ThreadLocalRandom.current().nextDouble(-widths[i], widths[i]);
		}
		return this;
	}
	
	public double evaluate(double... ds) {
		double sum = 0.0;
		for(int i = 0; i < Math.min(ds.length, weights.length); ++i) {
			sum += ds[i] * weights[i];
		}
		return sum / weightSum;
	}
	
	public Evaluator combine(Evaluator eval) {
		double weights[] = new double[this.weights.length];
		int split = ThreadLocalRandom.current().nextInt(weights.length);
		for(int i = 0; i < split; ++i) {
			weights[i] = this.weights[i];
		}
		for(int i = split; i < weights.length; ++i) {
			weights[i] = eval.weights[i - split];
		}
		return new Evaluator(weights);
	}
	
	public void mutate() {
		int index = ThreadLocalRandom.current().nextInt(weights.length);
		weights[index] += ThreadLocalRandom.current().nextGaussian() * weights[index] / 10.0;
	}
	
	@Override
	public String toString() {
		String res = "";
		for(int i = 0; i < weights.length; ++i) {
			res += weights[i] + " ";
		}
		return res;
	}
}
