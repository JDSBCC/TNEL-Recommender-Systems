package utilities;

public class Statistics {
	private double[] data;
	private double size = 0;

	public Statistics(double[] data) {
		this.data = data;
		for (double a : data){
			if(a!=-1){
				size++;
			}
		}
	}

	public double getMean() {
		double sum = 0.0;
		for (double a : data){
			if(a!=-1){
				sum += a;
			}
		}
		return sum / size;
	}

	public double getVariance() {
		double mean = getMean();
		double temp = 0;
		for (double a : data){
			if(a!=-1){
				temp += (a - mean) * (a - mean);
			}
		}
		return temp / (size-1);
	}

	public double getStdDev() {
		return Math.sqrt(getVariance()*(size-1));
	}
}
