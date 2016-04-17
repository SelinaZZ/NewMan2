import java.util.*;
import java.math.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.*;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.StatUtils;
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.ChartUtilities; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Sampling {

	//An array to hold all the counts of selection
	double [] countArray;

	//size of array
	int size;

	//random generator
	Random rand;

	//constructor to initialize the array and random generator.
	public Sampling(int fileSize){
		this.size = fileSize;
		this.countArray = new double[fileSize];

		for(int i = 0; i < fileSize; i++){
			this.countArray[i] = 0;
		}

		this.rand = new Random(4);
	}

	//Sampling without replacement algorithm is written here
	public void randomSelect() {

		int Nr = size;
		int Nn = (int)Math.round(0.1 * Nr);
		double nextProb = 0.1;
		double nextPoint;

		int i = 0;
		while(i < size){

			nextPoint = rand.nextDouble();
			if(nextPoint < nextProb){
				countArray[i]++;
				Nr--;
				Nn--;
				nextProb = (double)Nn / Nr;
			}
			else{
				Nr--;
				nextProb = (double)Nn / Nr;
			}

			i++;

		}
	}

	//Running the 10, 100, 1000 tests in Main
	public static void main(String[] args) throws IOException{

		//Read the input train data file
		BufferedReader reader = new BufferedReader(new FileReader(args[0]));

		//gets the total line number of the data file
		int lineNum = 0;
		while(reader.readLine() != null){
			lineNum++;
		}
		reader.close();

		//number of runs for tests
		int runTimes[] = {10, 100, 1000, 10000, 100000};
		int length = runTimes.length;
		
		DefaultCategoryDataset chart = new DefaultCategoryDataset();

		//Run tests in the loop
		for(int j = 0; j < length; j++){
			
			//create new sampling object for each test
			Sampling sampling = new Sampling(lineNum);

			//run the algorithm 10, 100 times .....100000 times
			for(int i =0; i<runTimes[j]; i++)
				sampling.randomSelect();
			
			DescriptiveStatistics stats = new DescriptiveStatistics(sampling.countArray);
			
			//report of the result
			System.out.println( "Number of runs: " + runTimes[j] + 
					", Normalized Mean: " + stats.getMean()/runTimes[j] + 
					", Normalized SD: " + stats.getStandardDeviation()/runTimes[j]);
			System.out.println("");
			
			//adding the result after each test, later used to create the chart
			double normalizedMean = stats.getMean()/runTimes[j];	
			chart.addValue((Number)normalizedMean, "mean", runTimes[j]);

		}
		
		//Creating a chart
		JFreeChart LineChart = ChartFactory.createLineChart(
				"Mean VS Number of Runs", "Number of Runs", "Mean",
				chart, PlotOrientation.VERTICAL, true, true, false);
		int width = 640;
		int height = 480;
		File line_chart = new File( "chart.jpeg");
		ChartUtilities.saveChartAsJPEG(line_chart, LineChart, width, height);
	}


}
