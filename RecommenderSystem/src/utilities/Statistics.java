package utilities;

public class Statistics {
	private Double[] data;
	private double size = 0;

	public Statistics(Double[] data) {
		this.data = data;
		for (Double a : data){
			if(a!=null){
				size++;
			}
		}
	}

	public double getMean() {
		double sum = 0.0;
		for (Double a : data){
			if(a!=null){
				sum += a;
			}
		}
		return sum / size;
	}

	public double getVariance() {
		double mean = getMean();
		double temp = 0;
		for (Double a : data){
			if(a!=null){
				temp += (a - mean) * (a - mean);
			}
		}
		return temp / (size-1);
	}

	public double getStdDev() {
		return Math.sqrt(getVariance()*(size-1));
	}
}
