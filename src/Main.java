

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        int s = 2;//Integer.parseInt(args[0]);
        int b =3;// Integer.parseInt(args[1]);
        int E =8; // Integer.parseInt(args[2]);
        String traceFile = "test.trace"; //args[3];
        String ramFileName = "RAM.dat";

        Cache cache = new Cache(s, b, E);

        CacheSimulator simulator = new CacheSimulator(cache, traceFile, ramFileName);
        simulator.simulate();


    }
}