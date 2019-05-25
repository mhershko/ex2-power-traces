import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ex02_M1 {
    public static void main(String[] args) throws IOException {
        String user = "305706012";
        int number_of_power_traces = 10000;
        String filename = args[0];
        String difficulty = args[1];
        String serverURL = "http://aoi.ise.bgu.ac.il/encrypt?user=" + user + "&difficulty=1" + difficulty;

        //download_power_traces(filename, serverURL, number_of_power_traces);
        Vector<Vector<Double>> leaks_vec = create_leaks_vector(filename, number_of_power_traces);
        Vector<Double> means = calculate_means(leaks_vec);
        Vector<Double> vars = calculate_vars(leaks_vec);

        System.out.println("Mean\tVariance"); // Print once at the start of your program and then
//        for (int i = 0; i < means.length; i++) {
//            System.out.println(String.format("%.2f\t%.2f", means[i], vars[i]));
//        }
    }

    private static Vector<Double> calculate_means(Vector<Vector<Double>> leaks_vec) {
        Vector<Double> means_vec = new Vector<>();
        return means_vec;
    }
    private static Vector<Double> calculate_vars(Vector<Vector<Double>> leaks_vec) {
        Vector<Double> vars_vec = new Vector<>();
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
                Pattern r = Pattern.compile("leakes.*[(.*)]}");
                Matcher m = r.matcher(st);
                if (m.find()) {
                    Vector<Double> trace = new Vector<>();
                    String[] parts = m.group(0).split(",");
                    for (String part : parts) {
                        trace.add(Double.parseDouble(part));
                    }
                    leaks_vec.add(trace);
                }
            }
        }
        br.close();
        return leaks_vec;
    }

//    public static double[] getMeans(String filename) {
//
//    }

//    public static double[] getMeansVariances(String filename){
//
//    }

}
