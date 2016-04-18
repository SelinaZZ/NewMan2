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
		double mean[] = new double[column];
		double sd[]= new double[column];
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
		for(int i = 0; i<column; i++){
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
	//Running the 10, 100, 1000 tests in Main
	public static void main(String[] args) throws IOException{
		String vector = "";
		//Read the input train data file
		String filename = args[0];
		BufferedReader reader = new BufferedReader(new FileReader(args[0]));
		BufferedReader reader1 = new BufferedReader(new FileReader(filename));
		BufferedReader reader2 = new BufferedReader(new FileReader(filename));
		String s = reader2.readLine();
		if(s != null){
			for(int i =0; i<s.length(); i++){
				if( s.charAt(i) == ',')
					column++;
			}
			column++;
			System.out.println(column);
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
		}

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
	//			System.out.println("v1: " + dist.vector1 + ", v2 :" + dist.vector2 + ", dist: " + dist.distance);
			}
		}
		for(int i = 0; i<5; i++){
		Distance re = result[0].Q.poll();
		System.out.println(re.distance);
}
/*		Iterator <Distance> it = result[0].Q.iterator();
		while(it.hasNext()){
			System.out.println(it.next().distance);
		}
*/	}
}
