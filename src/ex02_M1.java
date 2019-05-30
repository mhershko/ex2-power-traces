import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

public class ex02_M1 {
    public static void main(String[] args) throws IOException {
        String user = "30563390";
        int number_of_power_traces = 10;
        String difficulty ="1";
        findStatics s= new findStatics(argsargs,difficulty,user,number_of_power_traces);
    }

    public static class findStatics{ 
    
    	public findStatics(String[] args,String difficulty, String user,int number_of_power_traces) throws IOException {		
          String filename =args[0] ;
          String serverURL = "http://aoi.ise.bgu.ac.il/encrypt?user=" + user + "&difficulty="+ difficulty;
    	  
          //Create file
          File file = new File(filename);
          file.createNewFile();
        
          download_power_traces(filename, serverURL, number_of_power_traces);
          Vector<Vector<Double>> leaks_vec = create_leaks_vector(filename, number_of_power_traces);
          
          Vector<Double> means =  calculate_means(leaks_vec);
          Vector<Double> vars =   calculate_vars(leaks_vec,means);
  
          System.out.println("Mean\tVariance"); // Print once at the start of your program and then
          for (int i = 0; i < means.size(); i++) {
              System.out.println(String.format("%.2f\t%.2f", means.get(i), vars.get(i)));
          }
    	}
    
    private static Vector<Double> calculate_means(Vector<Vector<Double>> leaks_vec) {
        Vector<Double> means_vec = new Vector<>();
        for(int i=0;i<leaks_vec.get(0).size();i++) {
        	Double mean=0.0;
        	for(int j=0;j<leaks_vec.size();j++) {
        		mean+=leaks_vec.get(j).get(i);
        	}
        	mean/=leaks_vec.size();
        	means_vec.add(mean);
        }
        return means_vec;
    }
    private static Vector<Double> calculate_vars(Vector<Vector<Double>> leaks_vec,Vector<Double> means) {
        Vector<Double> vars_vec = new Vector<>();
        for(int i=0;i<leaks_vec.get(0).size();i++) {
        	Double var=0.0;
        	for(int j=0;j<leaks_vec.size();j++) {
        		var+=Math.pow(leaks_vec.get(j).get(i)-means.get(i), 2);
        	}
        	var/=leaks_vec.size();
        	//double std = Math.sqrt(var);
        	vars_vec.add(var);
        }
        return vars_vec;
    }

    public static void download_power_traces (String filename, String serverURL, int number_of_power_traces) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (int i = 0; i < number_of_power_traces; i++) {
            URL oracle = new URL(serverURL);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            writer.write(in.readLine()+"\n");
            in.close();
        }
        writer.close();
    }
    public static Vector<Vector<Double>> create_leaks_vector(String filename, int number_of_power_traces) throws IOException {
    	Vector<Vector<Double>> leaks_vec = new Vector<>();
    	BufferedReader br = new BufferedReader(new FileReader(filename));
    	for (int i = 0; i < number_of_power_traces; i++){
    		String st;
    		while ((st = br.readLine()) != null) {
    		//	System.out.println(st);
    			String[] parseString=st.split("\\[");
    			parseString=parseString[1].split("\\]");
    			String[] traceValues=parseString[0].split(",");

    			Vector<Double> trace = new Vector<>();
    			for(int j=0;j<traceValues.length;j++) {
    				trace.add(Double.valueOf(traceValues[j]) );                   
    			}
    			leaks_vec.add(trace);
    		}
    	}
    	br.close();
    	return leaks_vec;
    }
    }
}
