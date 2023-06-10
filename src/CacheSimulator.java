import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CacheSimulator {

    // variables
    Cache cache;
    String TraceFileName;
    String RamFileName;
    DataInputStream input;
    ArrayList<String> ramDatas;
    ArrayList<String> outputs;

// argument constructor
    public CacheSimulator(Cache cache, String traceFileName, String ramFileName) {
        this.cache = cache;
        TraceFileName = traceFileName;
        RamFileName = ramFileName;
        ramDatas = new ArrayList<>();
        outputs = new ArrayList<>();
        readRam();
    }

    // default constructor
    public CacheSimulator() {

    }

 // it is  used to read the ram file
    public void readRam() {
        DataInputStream input;
        try {
            input = new DataInputStream(new FileInputStream(RamFileName));
            while (input.available() > 0) {
                byte data = input.readByte();
                String hexValue = String.format("%02x", data);
                ramDatas.add(hexValue);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // after finishing operations change the binary file
    public void updateRam() {
        FileOutputStream output;
        try {
            output = new FileOutputStream(RamFileName);
            byte[] newData = new byte[ramDatas.size()];
            int i = 0;
            for (String hexa : ramDatas) {
                byte value = convertHexaToDecimal(hexa);
                newData[i] = value;
                i++;
            }
            output.write(newData);
            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   // its used to write the  Ram content to txt file
    public void writeContentOfRam(String fileName) {
        File output = new File(fileName);
        FileWriter writer;
        try {
            writer = new FileWriter(output);
            for (String content:ramDatas) {
                writer.write(content+"\n");
            }
            writer.close();



        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private byte convertHexaToDecimal(String hex) {
        return (byte) Integer.parseInt(hex, 16);


    }

    public String[] SplitInstruction(String currentLine) {
        String[] splitted = currentLine.split(" ");
        splitted[1] = splitted[1].replace(",", "");
        splitted[2] = splitted[2].replace(",", "");
        return splitted;

    }

    public void applyInstruction(String[] instruction) {
        String operationType = instruction[0];
        String operationAddress;
        int size;
        String data;

        switch (operationType) {
            case "L" -> {
                operationAddress = instruction[1];
                size = Integer.parseInt(instruction[2]);
                outputs.add(operationType + " " + operationAddress + "," + " " + size + "\n");
                // TODO: 8.06.2023  fonksiyon tamamlanacak
                applyLoadOperation(operationAddress, size);

            }
            case "S" -> {
                operationAddress = instruction[1];
                size = Integer.parseInt(instruction[2]);
                data = instruction[3];
                outputs.add(operationType + " " + operationAddress + "," + " " + size + ", " + data + "\n");
                // TODO: 8.06.2023  fonksiyon tamamlanacak
                applyStoreOperation(operationAddress, size, data);

            }
            case "M" -> {
                operationAddress = instruction[1];
                size = Integer.parseInt(instruction[2]);
                data = instruction[3];
                outputs.add(operationType + " " + operationAddress + "," + " " + size + ", " + data + "\n");
                // TODO: 8.06.2023 fonksiyon tamamlanacak
                applyDataModifyOperation(operationAddress, size, data);
            }
            default -> System.out.println("ONLY L S AND M ARE ALLOWED");
        }


    }

    private void applyDataModifyOperation(String operationAddress, int size, String data) {
        applyLoadOperation(operationAddress, size);
        applyStoreOperation(operationAddress, size, data);
    }

    private void applyStoreOperation(String operationAddress, int size, String data) {
        // go to the operation address, store the information which is given in the ram
        // use this address value to access to the ram
        int address = getDecimalAdress(operationAddress);
        String binaryAddress = convertBinary(operationAddress);
        int j = 0;
        String byteData;
        for (int i = address; i < address + size; i++) {
            byteData = data.substring(j, j + 2);
            ramDatas.set(address, byteData);
            j += 2;
        }
        String tag = getTag(binaryAddress);
        int setIndex = getSetId(binaryAddress);
        int lineNumber = getLine(setIndex, tag);
        Block block = cache.cacheTable[setIndex][lineNumber];
        if (isEmptyBlock(block)) {
            outputs.add("  Miss\n Store in RAM\n");
            return;
        }

        String oldData = block.data;
        char[] old_data = oldData.toCharArray();
        char newCh;
        int cachIndex = address % cache.getBlockSize(); // find the correct position in cache which index we will start changing data?
        j = 0;
        for (int i = cachIndex; i < cachIndex + size * 2; i++) {
            newCh = data.charAt(j);
            old_data[i] = newCh;
            j++;
        }
        block.data = String.valueOf(old_data);
        if (block.isMiss) {
            outputs.add("  Miss\n Store in cache and RAM\n");
        } else {
            outputs.add("  Hit\n  Store in cache and RAM\n");
        }


    }

    private void applyLoadOperation(String operationAddress, int size) {
        // read the memory adress starting from operation address with the given size
        //the index of RamData array can be used to find the  place of the content of that address

        int address = getDecimalAdress(operationAddress);
        String binaryAddress = convertBinary(operationAddress);
        String currentTag = getTag(binaryAddress);
        String currentData = readData(address, size);
        int setNumber = getSetId(binaryAddress);
        int lineNumber = getLine(setNumber, currentTag);

        // write the information into cache
        Block[][] cacheTable = cache.cacheTable;
        Block block = cacheTable[setNumber][lineNumber];
        block.tag = currentTag;
        block.valid = '1';
        block.data = currentData;
        block.time++;
        // print the result of the block
        if (block.isMiss) {
            outputs.add("  Miss\n  Place in set " + setNumber + "\n");
        } else {
            outputs.add("  Hit\n  Place in set " + setNumber + "\n");
        }


    }


    private int getLine(int setNumber, String tag) {
        // if all blocks are empty on the set place the first one
        Block firstBlock = cache.cacheTable[setNumber][0];
        if (isEmptyBlock(firstBlock)) {
            cache.miss_count++;
            return 0; // first block
        }
        // if there is a block which the tag is matched return its index
        Block currentBlock;
        for (int blockNo = 0; blockNo < cache.getNumberOfBlocks(); blockNo++) {
            currentBlock = cache.cacheTable[setNumber][blockNo];
            if (currentBlock.tag.equals(tag)) {
                cache.hit_count++;
                currentBlock.isMiss = false;
                return blockNo;
            }
        }
        // if there is no tag matched with any block find the empty block
        for (int blockNo = 0; blockNo < cache.getNumberOfBlocks(); blockNo++) {
            currentBlock = cache.cacheTable[setNumber][blockNo];
            if (isEmptyBlock(currentBlock)) {
                cache.miss_count++;
                return blockNo;
            }
        }
        // if there is no empty block that means its full use the FIFO to find the block which time is longer
        int maxTime = firstBlock.time;
        int longestTimeBlockIndex = 0;
        for (int blockNo = 1; blockNo < cache.getNumberOfBlocks(); blockNo++) {
            currentBlock = cache.cacheTable[setNumber][blockNo];
            if (currentBlock.time > maxTime) {
                maxTime = currentBlock.time;
                longestTimeBlockIndex = blockNo;
            }
            cache.miss_count++;
            cache.evictions++;

        }
        return longestTimeBlockIndex;

    }

    private boolean isEmptyBlock(Block block) {
        // if first block is empty that means there is set is empty
        return block.tag.equals("");
    }

    private String readData(int index, int size) {

        if (size > cache.getBlockSize()) {
            return getPartialData(index, size);
        }
        StringBuilder fullBlockData = new StringBuilder();
        // if enough capasity occurs take all the data
        int blockSize = cache.getBlockSize();
        int groupNumber = index / blockSize;
        int startingIndex = groupNumber * blockSize;
        for (int i = startingIndex; i < startingIndex + cache.getBlockSize(); i++) {
            fullBlockData.append(ramDatas.get(i));

        }
        return String.valueOf(fullBlockData);

    }

    private String getPartialData(int index, int size) {
        StringBuilder partialData = new StringBuilder();
        for (int i = index; i < index + size; i++) {
            partialData.append(ramDatas.get(i));
        }
        return String.valueOf(partialData);
    }

    private String getTag(String operationAddress) {
        // find the size of tag;
        int tag_size = cache.getAddressSize() - cache.getSetBitSize() - cache.getBlockBitSize();
        return operationAddress.substring(0, tag_size);

    }

    private int getDecimalAdress(String operationAddress) {
        int address = 0;
        int decimalCh;
        int digitValue = 1;
        char ch;
        int lastIndex = operationAddress.length() - 1;
        for (int i = lastIndex; i >= 0; i--) {
            ch = operationAddress.charAt(i);
            decimalCh = getDecimalValue(ch);
            address += decimalCh * digitValue;
            digitValue *= 16;
        }
        return address;
    }

    private int getDecimalValue(char ch) {
        if (ch >= '0' && ch <= '9') {
            return ch - '0';
        }
        return ch - 'a' + 10;

    }

    private int getSetId(String operationAddress) {
        // hexa number
        int startingIndex = cache.getAddressSize() - cache.getBlockBitSize() - cache.getSetBitSize();
        int last = startingIndex + cache.getSetBitSize() - 1;
        char ch;
        int idValue = 0;
        int digitValue = 1;
        int decimalvalue;
        for (int i = last; i >= startingIndex; i--) {
            ch = operationAddress.charAt(i);
            decimalvalue = ch - '0';
            idValue += decimalvalue * digitValue;
            digitValue *= 2;
        }
        return idValue;
    }

    private String convertBinary(String operationAddress) {
        // address always 32 bit
        StringBuilder str = new StringBuilder();
        char hexaDigit;
        String currentBinary;
        for (int i = 0; i < operationAddress.length(); i++) {
            hexaDigit = operationAddress.charAt(i);
            currentBinary = getBinary(hexaDigit);
            str.append(currentBinary);
        }
        return String.valueOf(str);

    }

    private String getBinary(char hexaDigit) {
        return switch (hexaDigit) {
            case '0' -> "0000";
            case '1' -> "0001";
            case '2' -> "0010";
            case '3' -> "0011";
            case '4' -> "0100";
            case '5' -> "0101";
            case '6' -> "0110";
            case '7' -> "0111";
            case '8' -> "1000";
            case '9' -> "1001";
            case 'a' -> "1010";
            case 'b' -> "1011";
            case 'c' -> "1100";
            case 'd' -> "1101";
            case 'e' -> "1110";
            case 'f' -> "1111";
            default -> "";
        };
    }


    public void simulate() {
        File file = new File(TraceFileName);
        try {
            Scanner scanner = new Scanner(file);
            String line;
            String[] instruction;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                instruction = SplitInstruction(line);
                applyInstruction(instruction);

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String toString() {
        String messages = convertString();
        return "\thits:" + cache.getHit_count() + " misses:" + cache.getMiss_count() + " evictions:" + cache.getEvictions() + "\n\n" + messages;
    }

    private String convertString() {
        StringBuilder str = new StringBuilder();
        for (String message : outputs) {
            str.append(message);
        }
        return String.valueOf(str);
    }


}
