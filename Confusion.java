import java.util.*;
import java.math.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.*;
import java.util.PriorityQueue;
import java.util.Comparator;

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
		/*
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
		*/
		double errorRate = errorRate();
		
		// Confusion Matrix
		double [] totalLabels = new double[lineNum];
		for (int i = 0; i < lineNum; i++){
			totalLabels[i] = vectorList[index].array[vectorList[i].array.length - 1];
		}
		
		int max = 0;
		int min = totalLabels[0];

		for(int i = 0; i < lineNum; i++){
			if(max < totalLabels[i])
				max = totalLabels[i];
			if(min > totalLabels[i])
				min = totalLabels[i];
		}
		
		double[] tableLabels = new double[max-min+1];
		int d = min;
		for(int i = 0; i < tableLabels.length; i++){
			tableLabels[i] = d;
			d++;
		}

		System.out.println("----------Confusion Matrix----------");
		int tablel = tableLabels.length + 1;
		double[][] table = new double[max - min + 2][max - min + 2];
		table[0][0] = 0;
		for(int i = 0; i < tablel; i++){
			table[i+1][0] = tableLabels[i];
			table[0][i+1] = tableLabels[i];
		}
		for(int i = 1,j=1; i < table1; i++, j++){
			table[i][j] = 0.0;
		}
		for(int i = 1; i < tablel; i++){
			for(int j = 1; j < tablel; j++){
				for(d = 0; d <= realLabels.length; d++){
					if(realLabels[d] == table[i]){
						if(predictList[d] == realLables[d]){
							table[i][i] += 1;
						}else{
							int p = predictList[d] - min + 1;
							table[i][p] += 1;
						}
					}
				} 				
			}
		}
		int rowitem = 0;
		for(int i = 1; i < tablel; i++){
			for(int j = 1; j < tablel; j++){
				if(table[i][j] != 0.0)
				{
					rowitem ++;
				}
			}
			for(int j = 1; j < tablel; j++){
				if(table[i][j] != 0.0){
					table[i][j] = round(100 * table[i][j]/rowitem);
				}
			}
		}
		for(int i = 0; i < tablel; i++){
			for(int j = 0; j < tablel; j++){
				System.out.print(table[i][j] + "  ");
			}
			System.out.print("\n");
		}
	}

	public double errorRate(){
		double [] testLabels = predictedList;
		int index = trainList.length;
		double [] realLabels = new double[lineNum - index];
		int error = 0;
		for( int i = 0; i < testList.length; i++) {
			realLables[i] = vectorList[index].array[vectorList[index].array.length -1];
			index ++;
			if( testLabels[i] != realLables[i]) {
				error ++;
			}
		}		

		double errorRate = (double)(error / totalnum);
		double errorRateinPercent = errorRate * 100;

		return errorRateinPercent;
	}
		
	

}
