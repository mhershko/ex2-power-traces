import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ex02_M1 {

    static int[] AesSbox = {
            0x63,0x7c,0x77,0x7b,0xf2,0x6b,0x6f,0xc5,0x30,0x01,0x67,0x2b,0xfe,0xd7,0xab,0x76,
            0xca,0x82,0xc9,0x7d,0xfa,0x59,0x47,0xf0,0xad,0xd4,0xa2,0xaf,0x9c,0xa4,0x72,0xc0,
            0xb7,0xfd,0x93,0x26,0x36,0x3f,0xf7,0xcc,0x34,0xa5,0xe5,0xf1,0x71,0xd8,0x31,0x15,
            0x04,0xc7,0x23,0xc3,0x18,0x96,0x05,0x9a,0x07,0x12,0x80,0xe2,0xeb,0x27,0xb2,0x75,
            0x09,0x83,0x2c,0x1a,0x1b,0x6e,0x5a,0xa0,0x52,0x3b,0xd6,0xb3,0x29,0xe3,0x2f,0x84,
            0x53,0xd1,0x00,0xed,0x20,0xfc,0xb1,0x5b,0x6a,0xcb,0xbe,0x39,0x4a,0x4c,0x58,0xcf,
            0xd0,0xef,0xaa,0xfb,0x43,0x4d,0x33,0x85,0x45,0xf9,0x02,0x7f,0x50,0x3c,0x9f,0xa8,
            0x51,0xa3,0x40,0x8f,0x92,0x9d,0x38,0xf5,0xbc,0xb6,0xda,0x21,0x10,0xff,0xf3,0xd2,
            0xcd,0x0c,0x13,0xec,0x5f,0x97,0x44,0x17,0xc4,0xa7,0x7e,0x3d,0x64,0x5d,0x19,0x73,
            0x60,0x81,0x4f,0xdc,0x22,0x2a,0x90,0x88,0x46,0xee,0xb8,0x14,0xde,0x5e,0x0b,0xdb,
            0xe0,0x32,0x3a,0x0a,0x49,0x06,0x24,0x5c,0xc2,0xd3,0xac,0x62,0x91,0x95,0xe4,0x79,
            0xe7,0xc8,0x37,0x6d,0x8d,0xd5,0x4e,0xa9,0x6c,0x56,0xf4,0xea,0x65,0x7a,0xae,0x08,
            0xba,0x78,0x25,0x2e,0x1c,0xa6,0xb4,0xc6,0xe8,0xdd,0x74,0x1f,0x4b,0xbd,0x8b,0x8a,
            0x70,0x3e,0xb5,0x66,0x48,0x03,0xf6,0x0e,0x61,0x35,0x57,0xb9,0x86,0xc1,0x1d,0x9e,
            0xe1,0xf8,0x98,0x11,0x69,0xd9,0x8e,0x94,0x9b,0x1e,0x87,0xe9,0xce,0x55,0x28,0xdf,
            0x8c,0xa1,0x89,0x0d,0xbf,0xe6,0x42,0x68,0x41,0x99,0x2d,0x0f,0xb0,0x54,0xbb,0x16
    };

    public static void main(String[] args) throws IOException {
        String user = "305706012";
        int number_of_power_traces = 10;
        String difficulty ="1";
        findStatics s= new findStatics(args,difficulty,user,number_of_power_traces);
        findKey("power_traces.txt");


    }

    public static Map<Integer, Vector<Integer>> findKey(String filename) throws IOException {
        Map<Integer, Vector<Integer>> hammingWeights = null;
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String st;
        while ((st = br.readLine()) != null) {
            String[] parseString=st.split("\",\"leaks\":");
            parseString=parseString[0].split("\"plaintext\":\"");
            String plaintext = parseString[1];
            System.out.println(plaintext);

            Vector<Integer> bytes = new Vector<>();
            for (int i = 0; i < plaintext.length(); i+=2){
                System.out.println("***** Byte #" + i + " *****");
                String s1 = Character.toString(plaintext.charAt(i));
                String s2 = Character.toString(plaintext.charAt(i+1));
                bytes.add(Integer.parseInt(s1.concat(s2), 16));
            }
            hammingWeights =  calculate_hamming_weight(bytes);
            //Byte correct_byte = find_correct_byte(hammingWeights);
        }
        return hammingWeights;
    }

    // TODO - This method gets a map with byte as a possible part of the correct key, a vector of hamming weights and the leaks vector and return the correct byte
    private static Byte find_correct_byte(Map<Byte, Vector<Integer>> hammingWeights) //add here the leaks vector) {
    {
        Byte b = null;
        return b;
    }
    // This method gets a vector of 16 bytes and returns the hamming weights of all the bytes with all possible keys
    public static Map<Integer, Vector<Integer>> calculate_hamming_weight(Vector<Integer> bytes) {
        Map<Integer, Vector<Integer>> hammingWeights = new HashMap<>();
        int b = 0x00;
        while (b < 0xFF) {
            Vector<Integer> hammingweights_per_byte = new Vector<>();
            for (int i = 0; i < bytes.size(); i++) {
                System.out.println("Input byte = " + bytes.get(i).toString() + ", Hyp Key = " + b);
                int xor_val = bytes.get(i)^b;
                int high = xor_val >> 4;
                int low = xor_val&0x0F;
                int res = AesSbox[low+1 + 0xF*high];
                System.out.println("Result = " + res);
                int hamming_weight = res&0xFF;
                hammingweights_per_byte.add(hamming_weight);
            }
            hammingWeights.put(b, hammingweights_per_byte);
            b+=1;
        }
        return hammingWeights;
    }

    public static class findStatics{ 
    
    	public findStatics(String[] args,String difficulty, String user,int number_of_power_traces) throws IOException {

          String filename = args[0] ;
          String serverURL = "http://aoi.ise.bgu.ac.il/encrypt?user=" + user + "&difficulty="+ difficulty;
    	  
          //Create file
          File file = new File(filename);
          file.createNewFile();
        
          //download_power_traces(filename, serverURL, number_of_power_traces);
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
