import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import org.apache.commons.lang3.StringUtils;

import com.opencsv.*;
// Class Which Writes into a final file the results of each Thread.
class Writer {
    File newFile;
    FileWriter outputfile;
    CSVWriter writer;

    public Writer(String nameFile) {
    	// Creates a File object with the specified path.
        this.newFile = new File (nameFile);
        try {
        	// Creates a FileWriter Object with the file, in case of error it throws an exception.
        	// Second Parameter is append boolean. (If it should append at the end 'true', if it should overwrite 'false').
			outputfile = new FileWriter(newFile,true);
			// Create a csv Writer Object.
	    	writer = new CSVWriter(outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    // Method to write on said file.
    // It must be synchronized as only one thread should be writting.
    // Should recieve the Data in an array (divided by zones), the name of the molecule, and year of mesurements.
    public synchronized void writeOnCSV(int[] data, String name, String year) {
    	
    	for(int i = 0; i < data.length; i++) {
    		// Create a temporary string to hold the value of a row in the csv.
    		String[] temp = {name, "zona" + i , year , "" + data[i]};
    		// System.out.println(temp);
    		// Writes temp inside the file.
    		writer.writeNext(temp);
    	}
    }
    public void closeWriter() {
    	try {
    		// Close the csv Writer object.
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}

class ReadDataByLine extends Thread {
    // File to read from
    String file;
    // Class writer to be sync on all readers
    Writer writer;
    // Counter of number of entries in file.
    int counter = 0;
    // Array in which the data for each location is saved.
    int[] sums = new int[10];
    // Name of the molecule
    String name;
    // Year of the data
    String year;

    public ReadDataByLine(String file, Writer writer, String name, String year){
        this.file = file;
        this.writer = writer;
        this.name = name;
        this.year = year;
    }
    public void run() {
    	
        try { 
  
            // Create an object of filereader class 
            // with CSV file as a parameter. 
            FileReader filereader = new FileReader(file); 
  
            // create csvReader object passing 
            // filereader as parameter 
            CSVReader csvReader = new CSVReader(filereader); 
            // Var where each row of the csv will be copied.
            String[] nextRecord; 
            int i;
  
            while ((nextRecord = csvReader.readNext()) != null) { 
            	i=0;
                for (String cell : nextRecord) { 
                    // System.out.print(cell + "\t");
                    
                    // Condition to prevent non important info from the csv to be included on the Data array.
                    if(i >= 2 && i <= 11 && !StringUtils.isEmpty(cell)) {
                    	sums[i-2] += (int) Double.parseDouble(cell);
                    }
                    i++;
                } 
                // System.out.println();
                counter = counter + 1; 
            } 
        } 
        catch (Exception e) { 
            e.printStackTrace(); 
        }
        for(int i = 0; i < 10; i++) {
        	sums[i] /= counter;
        	// System.out.print("Sum [" + i + "] : " + sums[i] + "\t");
        }
        // System.out.println("\nCounter: " + counter);
        
        // Call to writer function.
        writer.writeOnCSV(sums, name, year);
        
        // Message of successful run of thread.
        System.out.println(name + " data was successfully transformed.");
    }
}

public class Parser {

	static String csv_no2_2014_path = "src/Data/GDL-NO2-2014.csv";
	static String csv_co_2014_path = "src/Data/GDL-CO-2014.csv";
	static String csv_o3_2014_path = "src/Data/GDL-O3-2014.csv";
	static String csv_pm10_2014_path = "src/Data/GDL-PM10-2014.csv";
	static String csv_so2_2014_path = "src/Data/GDL-SO2-2014.csv";
	static String csv_write_path = "src/Data/ProcessedData.csv";
	
public static void main(String[] args) { 
		// Declare Writer
		Writer writer= new Writer(csv_write_path);
	    System.out.println("Read Data Line by Line With Header \n"); 
	    // Declare Readers
	    ReadDataByLine reader1 = new ReadDataByLine(csv_no2_2014_path, writer, "NO2", "2014");
	    ReadDataByLine reader2 = new ReadDataByLine(csv_co_2014_path, writer, "CO", "2014");
	    ReadDataByLine reader3 = new ReadDataByLine(csv_o3_2014_path, writer, "O3", "2014");
	    ReadDataByLine reader4 = new ReadDataByLine(csv_pm10_2014_path, writer, "PM10", "2014");
	    ReadDataByLine reader5 = new ReadDataByLine(csv_so2_2014_path, writer, "SO2", "2014");
	    // Start Readers
	    reader1.start();
	    reader2.start();
	    reader3.start();
	    reader4.start();
	    reader5.start();
	    

	    try {
			reader1.join();
		    reader2.join();
		    reader3.join();
		    reader4.join();
		    reader5.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    writer.closeWriter();
	    System.out.println("_______________________________________________"); 
	}

}
/*
Copyright 2019 Huitznahui Bolaños
% This program is free software: you can redistribute it and or modify
%   it under the terms of the GNU General Public License as published by
%    the Free Software Foundation, either version 3 of the License, or
%    (at your option) any later version.
%
%    This program is distributed in the hope that it will be useful,
%    but WITHOUT ANY WARRANTY; without even the implied warranty of
%    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
%    GNU General Public License for more details.
%
%    You should have received a copy of the GNU General Public License
%    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
