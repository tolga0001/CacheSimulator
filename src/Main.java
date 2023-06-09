

public class Main {
    public static void main(String[] args) {
        int s, b, E;
        String traceFile;
        if (args.length < 8) {
            System.out.println("incorrect input! ");
            s = 2;
            b = 3;
            E = 8;
            traceFile = "test.txt";
        } else {
            String[] arr = new String[8];
            takeInput(args, arr);
            s =2; //Integer.parseInt(arr[0]);
            b =3; //Integer.parseInt(arr[1]);
            E =8; //Integer.parseInt(arr[2]);
            traceFile = arr[3];
        }
        String ramFileName = "RAM.dat";

        Cache cache = new Cache(s, b, E);

        CacheSimulator simulator = new CacheSimulator(cache, traceFile, ramFileName);
        simulator.simulate();
        System.out.println(simulator.toString());


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