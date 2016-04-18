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
import java.util.Comparator;

class Vector {
	public double [] array;
}


class Distance{
	public int vector1;
	public int vector2;
	public double distance;
	Distance(){

	}
	Distance(Distance d){
		vector1 = d.vector1;
		vector2 = d.vector2;
		distance = d.distance;
	}
}
class Knn {
	PriorityQueue<Distance> Q = new PriorityQueue<Distance>(11, 
			new Comparator<Distance>(){
			public int compare(Distance a, Distance b){
			if (a.distance > b.distance) return 1;
			if (a.distance == b.distance) return 0;
			return -1;
			}
			});	
}

public class DataRead {

	//An array to hold all the counts of selection
	double [] countArray;

	//2d array stores origin data
	static Vector[] vectorList;

	//training data
	static Vector[] training;
	static int trainingSize;

	//test data
	static Vector[] test;
	static int testSize;

	//size of array
	static int size;

	//column size
	static int column = 0;

	static double acc = 0.0;

	static double [] [] labels;
	//constructor to initialize the array and random generator.
	public DataRead(int fileSize){
		this.size = fileSize;
		this.countArray = new double[fileSize];

		for(int i = 0; i < fileSize; i++){
			this.countArray[i] = 0.0;
		}

	}

	static public void zScaling(){
		Sampling sample = new Sampling(size);
		sample.randomSelect(0.9);
		DescriptiveStatistics stats = new DescriptiveStatistics();
		double mean[] = new double[column-1];
		double sd[]= new double[column-1];
		trainingSize = (int)Math.round(0.9 * size);
		training = new Vector[trainingSize];
		testSize = size -trainingSize;
		test = new Vector[testSize];
		int k = 0;
		int h = 0;
		for(int i=0; i< size; i++){
			if(sample.countArray[i] != 0.0){
				training[h] = vectorList[i];
				h++;
			}
			else{
				test[k] = vectorList[i];
				k++;
			}
		}
		for(int i = 0; i<column-1; i++){
			for(int j = 0; j < size; j++){
				if(sample.countArray[j] != 0.0){
					stats.addValue(vectorList[j].array[i]);
				}
			}
			mean[i] = stats.getMean();
			sd[i]= stats.getStandardDeviation();

		}

		for(int i = 0; i<trainingSize; i++){
			for(int j = 0; j<column-1; j++){
				training[i].array[j] = (training[i].array[j] - mean[j])/sd[j];
			}
		}
		for(int i = 0; i<testSize; i++){
			for(int j = 0; j<column-1; j++){
				test[i].array[j] = (test[i].array[j] - mean[j])/sd[j];
			}
		}

	}
	static public double distance(int t, int tr){
		double distance = 0.0;
		double temp = 0.0;
		for(int i = 0; i <column-1; i++){
			temp = (test[t].array[i] - training[tr].array[i]);
			temp = temp*temp;
			distance = distance + temp;
		}
		distance = Math.sqrt(distance);
		//System.out.println(distance);
		return distance;
	}	

	static public void formatData(String filename) throws IOException{
		String vector = "";
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		BufferedReader reader1 = new BufferedReader(new FileReader(filename));
		BufferedReader reader2 = new BufferedReader(new FileReader(filename));
		String s = reader2.readLine();
		if(s != null){
			for(int i =0; i<s.length(); i++){
				if( s.charAt(i) == ',')
					column++;
			}
			column++;
			//	System.out.println(column);
			reader2.close();
		}
		//gets the total line number of the data file
		int lineNum = 0;

		while(reader.readLine() != null){
			lineNum ++;
		}

		String[] lines = new String[lineNum];
		String read = reader1.readLine();
		int l = 0;

		while(read != null){
			lines[l] = read;
			//System.out.println(lines[l]);
			l++;
			read =reader1.readLine();
		}

		String[] myDataChars = new String[column];
		int dataLen = myDataChars.length;
		vectorList = new Vector[lineNum];
		int vec = 0;
		int linenum = lineNum;
		size = lineNum;


		if(filename.equals("abalone.data")){
			column = column +2;
			while(lineNum >= 1){
				vector = lines[linenum - lineNum];
				int i = 0;
				double[] dataChars = new double[dataLen + 2];
				for (String elem: vector.split(",")){
					myDataChars[i] = elem;
					i++;
				}
				if(myDataChars[0].equals("F")){
					dataChars[0] = 0.0;
					dataChars[1] = 1.0;
					dataChars[2] = 0.0;
				}else if (myDataChars[0].equals("M")){
					dataChars[0] = 0.0;
					dataChars[1] = 0.0;
					dataChars[2] = 1.0;
				}else if (myDataChars[0].equals("I")){
					dataChars[0] = 1.0;
					dataChars[1] = 0.0;
					dataChars[2] = 0.0;
				}
				i = 3;
				int index = 1;
				while(index < myDataChars.length){
					dataChars[i] = Double.parseDouble(myDataChars[index]);
					i++;
					index++;
				}
				Vector v = new Vector();
				v.array = dataChars;
				vectorList[vec]= v;
				vec++;
				lineNum --;
			}
		}else {
			while(lineNum >= 1) {
				int i = 0;
				double[] dataChars = new double[dataLen];
				vector = lines[linenum - lineNum];
				for (String elem: vector.split(",")){
					dataChars[i] = Double.parseDouble(elem);
					i++;
				}
				Vector v = new Vector();
				v.array = dataChars;
				vectorList[vec] = v;
				vec ++;
				lineNum --;
			}
			/*
			   for(int i = 0; i<size; i++){
			   for(int j = 0; j<dataLen; j++){
			   System.out.print(vectorList[i].array[j] + ",");
			   }
			   System.out.println("");
			   }
			   */
		}

	}

	//Confusion Matrix
	public void confusion(){
		double[] totalLabels = new double[size];
		for (int i = 0; i < size; i++){
			totalLabels[i] = vectorList[i].array[vectorList[i].array.length - 1];
		}

		double max = 0;
		double min = totalLabels[0];

		for(int i = 0; i < size; i++){
			if(max < totalLabels[i])
				max = totalLabels[i];
			if(min > totalLabels[i])
				min = totalLabels[i];
		}

		double[] tableLabels = new double[max-min+1];
		int d = min;
		for (int i = 0; i < tableLabels.length; i++){
			tableLabels[i] = d;
			d++;
		}

		System.out.println("Confusion Matrix row : test labels");
		System.out.println("Confusion Matrix col : predicted labels");
		System.out.println("--------------Confusion Matrix--------------");

		int len = tableLabels.length + 1;
		double [][] table = new double[len][len];

		for(int i = 0; i < len; i++){
			table[0][i] = test[i].array[test[i].array.length - 1];
			table[i][0] = test[i].array[test[i].array.length - 1];
		}

		for(int i = 1; i < len; i++){
			table[i][i] = 0.0;
		}

		for(int i = 0; i < len; i++){
			for(int d = 0; d <= testSize; d++){
				if(test[d].array[test[d].array.length - 1] == table[i]){
					int p = pList[d] - min + 1;
					table[i][p] += 1;
				}
			}
		}

		int rowitem = 0;
		for(int i = 1; i < len; i++){
			for(int j = 1; j < len; j++){
				if(table[i][i] != 0){
					rowitem++;
				}
			}
			for(int j = 1; j < len; j++){
				if(table[i][i] != 0){
					table[i][i] = (100 * table[i][i]/rowitem);
				}
			}	
		}

		for(int i = 0; i < len; i++){
			for(int j = 0; j < len; j++){
				System.out.print(table[i][i] + "   ");
			}
			System.out.println("");
		}

		System.out.println("Accuracy is : " + acc + "%");

	}


	//Running the 10, 100, 1000 tests in Main
	public static void main(String[] args) throws IOException{
		//Read the input train data file
		String filename = args[0];
		formatData(filename);
		zScaling();

		Knn []result = new Knn[testSize];
		for(int i = 0; i< testSize; i++){
			result[i] = new Knn();
			for(int j = 0; j <trainingSize; j++){
				Distance dist = new Distance();
				dist.vector1 = i;
				dist.vector2 = j;
				dist.distance = distance(i,j);
				result[i].Q.add(dist);
			}
		}

		Distance [][] knnResult= new Distance[testSize][9];
		for(int j = 0; j < testSize; j++){
			for(int i = 0; i<9; i++){
				knnResult[j][i]= new Distance(result[j].Q.poll());
			}
		}

		int[] ks = {1,3,5,7,9};
		int length = ks.length;
		double [][]errorRate = new double [testSize][length];
		for(int h = 0; h <testSize; h++){
			for(int i = 0; i<length; i++){
				int k = ks[i]; 
				double tempError = 0.0;
				int v1 = h;
				int [] counts = new int[k];
				for(int j = 0; j<k; j++){
					int v2 = knnResult[h][j].vector2;
					for(int u = 0; u < k; u++){
						int tempV = knnResult[h][u].vector2;
						if(training[v2].array[column-1] == training[tempV].array[column-1])
							counts[j]++;
					}



				}
				int maxIndex = 0;
				for (int e = 1; e < counts.length; e++){
					int newnumber = counts[i];
					if ((newnumber > counts[maxIndex])){
						maxIndex = e;
					}
				}
				/*
				   int numRes = 0;
				   for(int y = 0; y<counts.length; y++){
				   if(counts[maxIndex] == counts[y])
				   numRes++;
				   }

				   int[] randomPick = new int[numRes+1];
				   int ind = 0;
				   randomPick[ind] = maxIndex;
				   for(int t = 0; t<counts.length; t++){
				   if(counts[maxIndex] == counts[t]){
				   ind++;
				   randomPick[ind] = t;
				   }

				   }
				   int randomIndex = new Random(99).nextInt(randomPick.length);
				   */
				int vec2 = knnResult[h][maxIndex].vector2;
				if(test[v1].array[column-1] != training[vec2].array[column-1]){
					tempError = 1.0;
				}
				else{
					tempError = 0.0;
				}
				errorRate[h][i] = tempError;
				labels[h][i] = training[vec2].array[column-1];
			}
		}
		double[] avgError = new double [5];
		for(int j = 0; j<5; j++){
			double temp = 0.0;
			for(int i = 0; i<testSize; i++){
				temp = temp + errorRate[i][j];
			}
			avgError[j] = temp/testSize;
		}
		int minInd = 0;
		for (int e = 1; e < avgError.length; e++){
			double newnumber = avgError[e];
			if ((newnumber < avgError[minInd])){
				minInd = e;
			}
		}
		int optimalK = ks[minInd];
		double temp = 0.0;
		for(int i = 0; i < testSize; i++){
			temp = temp + errorRate[i][minInd];
		}
		 acc = temp / testSize;
		
		System.out.println(optimalK + "  " + acc);


	}
}
