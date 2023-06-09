

public class Main {
    public static void main(String[] args) {
        if(args.length < 8) {
            System.out.println("incorrect input! " + args.length);
        }
        String[] arr = new String[8];
        takeInput(args, arr);
        int s = Integer.parseInt(arr[0]);
        int b = Integer.parseInt(arr[1]);
        int E = Integer.parseInt(arr[2]);
        String traceFile = arr[3];
        String ramFileName = "RAM.dat";

        Cache cache = new Cache(s, b, E);

        CacheSimulator simulator = new CacheSimulator(cache, traceFile, ramFileName);
        simulator.simulate();
        System.out.println(simulator.toString());


    }

    public static void takeInput(String[] testArr, String[] arr) {
        for(int i = 1; i < 8; i = i+2) {
            switch (testArr[i-1]) {
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