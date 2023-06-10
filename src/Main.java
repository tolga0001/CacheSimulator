import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        int s, b, E;
        String traceFile;
        if (args.length != 8) {
            System.out.println("incorrect input! ");
        } else {
            String[] arr = new String[8];
            takeInput(args, arr);
            s = Integer.parseInt(arr[0]);
            b = Integer.parseInt(arr[1]);
            E = Integer.parseInt(arr[2]);
            traceFile = arr[3];

            String ramFileName = "RAM.dat";

            // creating cache object
            Cache cache = new Cache(s, b, E);
            // create simulator according to the given cache tracefile and ramFileName
            CacheSimulator simulator = new CacheSimulator(cache, traceFile, ramFileName);

            // write the first initial of ram to the txt file
            simulator.writeContentOfRam("initialRam.txt");
            // start simulating
            simulator.simulate();
            // write the total hit miss and eviction values to the console
            System.out.println(simulator.toString());

            // write the contents of the cache to the txt file after executing operations
            File output = new File("Cache.txt");
            try {
                FileWriter writer = new FileWriter(output);
                writer.write(cache.toString());
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // update the binary file;
            simulator.updateRam();

            // write the last content of Ram to the txt file
            simulator.writeContentOfRam("FinalRam.txt");



        }
    }

    public static void takeInput(String[] testArr, String[] arr) {
        for (int i = 1; i < 8; i = i + 2) {
            switch (testArr[i - 1]) {
                case "-s" -> arr[0] = testArr[i];
                case "-E" -> arr[1] = testArr[i];
                case "-b" -> arr[2] = testArr[i];
                case "-t" -> arr[3] = testArr[i];
            }
        }
    }
}