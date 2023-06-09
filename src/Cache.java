public class Cache {
    private final int s;
    private final int b;
    int E;
    int hit_count;
    int miss_count;
    int evictions;
    Block[][] cacheTable;

// Cache Constructor
    public Cache(int s, int b, int E) {
        this.s = s;
        this.b = b;
        this.E = E;
        hit_count = 0;
        miss_count = 0;
        evictions = 0;
        initializeCache();
    }

    public void initializeCache() {
        // the  number of Line for each set is b
        // create two dimensional array to represent the cache table
        int setNumber = (int) Math.pow(2, s);
        int lineNumber = E;
        cacheTable = new Block[setNumber][lineNumber];
    }


    public int getBlockBitSize() {
        return b;

    }

    public int getAddressSize() {
        // variables
        int addressSize = 32;
        return addressSize;
    }

    public int getSetBitSize() {
        return s;
    }


    public int getNumberOfLines() {
        return E;
    }

    public int getHit_count() {
        return hit_count;
    }

    public int getMiss_count() {
        return miss_count;
    }

    public int getEvictions() {
        return evictions;
    }

    public Block[][] getCacheTable() {
        return cacheTable;
    }

    public int getSetSize() {
        return (int) Math.pow(2, s);
    }

    public int getNumberOfBlocks() {
        return (int) Math.pow(2, b);

    }
}

class Block {
    String tag;
    int time;
    char valid;
    String data;

    public Block(String tag, int time, String data) {
        this.tag = tag;
        this.time = time;
        this.data = data;
        valid = '0';
    }

}

