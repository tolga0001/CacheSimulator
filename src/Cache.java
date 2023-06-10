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
        int setNumber = getNumberOfSets();
        int lineNumber = E;
        cacheTable = new Block[setNumber][lineNumber];
        for (int i = 0; i < setNumber; i++) {
            for (int j = 0; j < lineNumber; j++) {
                cacheTable[i][j] = new Block("", 0, "");
            }
        }
    }

    private int getNumberOfSets() {
        return (int) Math.pow(2, s);
    }


    public int getBlockBitSize() {
        return b;

    }

    public int getAddressSize() {
        // variables
        return 32;
    }

    public int getSetBitSize() {
        return s;
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

    public int getNumberOfBlocks() {
        return  E;

    }

    public int getBlockSize() {
        return (int) Math.pow(2, b);
    }

    @Override
    public String toString() {
        Block block;
        StringBuilder cacheInfo = new StringBuilder();
        for (int setId = 0; setId < getNumberOfSets(); setId++) {
            for (int blockId = 0; blockId < getNumberOfBlocks(); blockId++) {
                block = cacheTable[setId][blockId];
                cacheInfo.append(block.toString());
            }

        }
        return String.valueOf(cacheInfo);

    }
}

class Block {
    String tag;
    int time;
    char valid;
    String data;
    boolean isMiss;

    public Block(String tag, int time, String data) {
        this.tag = tag;
        this.time = time;
        this.data = data;
        valid = '0';
        isMiss = true;
    }

    // @Override
    public String toString() {
        return "Cache Tag: " + tag + " time: " + time + " " + "v: " + valid + " " + "data: " + data + "\n";
    }

}

