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

class Vector {
	public double [] array;
}

public class DataRead {

	//An array to hold all the counts of selection
	double [] countArray;

	//size of array
	int size;

	//constructor to initialize the array and random generator.
	public DataRead(int fileSize){
		this.size = fileSize;
		this.countArray = new double[fileSize];

		for(int i = 0; i < fileSize; i++){
			this.countArray[i] = 0.0;
		}

	}	

	//Running the 10, 100, 1000 tests in Main
	public static void main(String[] args) throws IOException{
		String vector = "";
		//Read the input train data file
		String filename = args[0];
		BufferedReader reader = new BufferedReader(new FileReader(args[0]));
		BufferedReader reader1 = new BufferedReader(new FileReader(filename));
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
			l++;
			read =reader1.readLine();
		}

		String[] myDataChars = new String[9];
		int dataLen = myDataChars.length;
		double[] dataChars = new double[dataLen + 2];
		Vector[] vectorList = new Vector[lineNum];
		int vec = 0;
		int linenum = lineNum;

		

		if(filename.equals("abalone.data")){
			while(lineNum >= 1){
				vector = lines[linenum - lineNum];
				int i = 0;
				for (String elem: vector.split(",")){
					myDataChars[i] = elem;
					i++;
				}
				if(myDataChars[0].equals('F')){
					dataChars[0] = 0;
					dataChars[1] = 1;
					dataChars[2] = 0;
				}else if (myDataChars[0].equals('M')){
					dataChars[0] = 0;
					dataChars[1] = 0;
					dataChars[2] = 1;
				}else if (myDataChars[0].equals('I')){
					dataChars[0] = 1;
					dataChars[1] = 0;
					dataChars[2] = 0;
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
				vectorList[vec] = v;
				vec++;
				lineNum --;
			}
		}else {
			while(lineNum >= 1) {
				int i = 0;
				int index = 2;
				dataChars[0] = 0;
				dataChars[1] = 0;
				vector = lines[linenum - lineNum];
				for (String elem: vector.split(",")){
					myDataChars[i] = elem;
					dataChars[index] = Double.parseDouble(elem);
					i++;
				}
				Vector v = new Vector();
				v.array = dataChars;
				vectorList[vec] = v;
				vec ++;
				lineNum --;
			}
		}
			System.out.println("number of vector : " + vec);
			reader.close();

	}
}
