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

public class Confusion{

	//size of array
	int knn;
	int label1;
	int label2;

	//Constuctor for labels
	public Confusion(int label1, int label2, int k){
		this.label1 = label1;
		this.label2 = label2;
		this.knn = k;
	}

	//Mamin function to get accuracy
	public static void main(String[] args) throw IOException{
		int lower = 0;
		int upper = 0;
		if(label1 > label2) {
			lower = label2 - k;
			upper = label1 + k;
		} else {
			lower = label1 - k;
			upper = label2 + k;
		}
		
		Vector [] testList = new Vector[lineNum - trainList.length];
		for( int i = 0; i < testList.length; i ++) {
			int index = trainList.length;
			testList[i] = vectorList[index];
			index ++;
		}
		int [] testLabels = new int[testList.length];
		for( int i = 0; i < testList.length; i++) {
			testLabels[i] = testList[i].array[testList[i].array.length - 1];
		}
		int totalnum = testLabels.length;
		int error = totalnum;

		for( int i = 0; i < totalnum; i++){
			if(testLabels[i] <= upper && testLabels[i] >= lower)
				error --;
		}

		double errorRate = (double)(error / totalnum);
		double errorRateinPercent = errorRate * 100;

		//get range of confusion matrix
		int max = 0;
		int min = testLabels[0];

		for( int i = 0; i < totalnum; i++){
			if(max < testLabels[i])
				max = testLabels[i];
			if(min > testLables[i])
				min = testLabels[i];
		}

		int range = max - min + 1;
		int [] title = new int[range];
		//product the matrix
		for(int i = 0; i < range; i++){
			title[i] = min;
			min = min + 1;
		}

				
		
	
	}

}
