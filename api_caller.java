package api_caller;

import java.net.*;
import java.io.*;
import java.util.Random;
/**
 * 
 * @author Sean Onslow
 *
 */

public class api_caller{
	private int lineCount;
	private String[][] locations;
    public static void main(String[] args) throws Exception {
    	//Edit the file location for your own
    	String csvFile = "C:\\Users\\seano\\workspace\\api_caller\\test_three.csv";
        api_caller api_caller = new api_caller(csvFile);
    }
    
    public int lineCount(String file){
    	String csvFile = file;
    	BufferedReader br = null;
    	String line = "";
    	int counter = 0;
    	try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
            	counter += 1;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    	return counter;
    }
    
    public String[][] readFile(String file){
    	String[][] locations = new String[lineCount][2];
    	BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int counter = 0;
        try {

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] location = line.split(cvsSplitBy);
                locations[counter][0] = location[0];
                locations[counter][1] = location[1];
                counter += 1;
                
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return locations;
    }
    
    public api_caller(String file) throws IOException{
    	lineCount = lineCount(file);
    	locations = readFile(file);
    	call_api();
    }
    
    public void call_api() throws IOException{
    	Random rand = new Random();
    	int  n = rand.nextInt(lineCount) + 1;
    	String location1 = locations[n][0] + "," + locations[n][1];
    	location1 = location1.replaceAll(" ", "%20");
    	n = rand.nextInt(lineCount) + 1;
    	String location2 = locations[n][0] + "," + locations[n][1];
    	location2 = location2.replaceAll(" ", "%20");
    	URL googleApi = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + location1 + "&destinations=" + location2 + "&key=AIzaSyBPenBk9jdGzITL5WXiMzHtKp5u3VKPNlU");
        URLConnection yc = googleApi.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
        String inputLine;
        int counter = 0;
        while ((inputLine = in.readLine()) != null)
        	if(inputLine.contains("text")){
        		if(counter == 1){
        			System.out.println("It will take " + inputLine.substring(28, inputLine.length() - 2) + " to walk from " + location1.replaceAll("%20",  " ") + " to " + location2.replaceAll("%20", " "));
        		}
        		counter += 1;
            }
        in.close();
    }
}