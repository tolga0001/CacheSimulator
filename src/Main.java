import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        int s, b, E;
        String traceFile;
        if (args.length < 8) {
            System.out.println("incorrect input! ");
            s = 8;
            b = 1;
            E = 25;
            traceFile = "/Users/tolga/IdeaProjects/CacheSimulator2/src/test_large.trace";
            //System.out.println("File len is "+traceFile.length());
        } else {
            String[] arr = new String[8];
            takeInput(args, arr);
            s = 2; //Integer.parseInt(arr[0]);
            b = 3; //Integer.parseInt(arr[1]);
            E = 8; //Integer.parseInt(arr[2]);
            traceFile = arr[3];
        }
        String ramFileName = "RAM.dat";
            Cache cache = new Cache(s, b, E);

        CacheSimulator simulator = new CacheSimulator(cache, traceFile, ramFileName);
        simulator.simulate();
        System.out.println(simulator.toString());
        File output = new File("Cache.txt");

        try {
            FileWriter writer = new FileWriter(output);
            writer.write(cache.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        System.out.println(cache);


    }

    public static void takeInput(String[] testArr, String[] arr) {
        for (int i = 1; i < 8; i = i + 2) {
            switch (testArr[i - 1]) {
                case "-s":
                    arr[0] = testArr[i];
                    break;
                case "-E":
                    arr[1] = testArr[i];
                    break;
                case "-b":
                    arr[2] = testArr[i];
                    break;
                case "-t":
                    arr[3] = testArr[i];
                    break;
            }
        }
    }
}