package modes;

import enums.AlgorithmType;
import enums.OperationType;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public abstract class Mode {
    private OperationType operationType;
    private String inputFileName;
    private String outputFileName;
    private AlgorithmType algorithmType;
    private String keyFileName;
    private byte[] plainText;
    private byte[] initializationVector;
    private byte[] key;
    private byte[] fullKey;
    private byte[] nonce;
    private byte[] cipherText;

    private static final byte[] ARBITRARY_KEY = {0, 1, 2, 3, 4, 5, 6, 7};

    protected void readKeyFile() {
        try {
            File keyFile = new File(getKeyFileName());
            BufferedReader keyFileText = new BufferedReader(new FileReader(keyFile));
            String keyFileString = keyFileText.readLine();

            byte[] fullIV = keyFileString.split(" - ")[0].getBytes(StandardCharsets.UTF_8);
            byte[] IV = getLSB(fullIV, 8);
            setInitializationVector(IV);

            fullKey = keyFileString.split(" - ")[1].getBytes(StandardCharsets.UTF_8);
            byte[] key = getLSB(fullKey, 8);
            setKey(key);

            byte[] fullNonce = keyFileString.split(" - ")[2].getBytes(StandardCharsets.UTF_8);
            byte[] nonce = getLSB(fullNonce, 4);
            setNonce(nonce);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    protected void readInputFile() {
        try {
            if (this.operationType == OperationType.ENCRYPTION) {
                setPlainText(Files.readAllBytes(Paths.get(getInputFileName())));
            } else {
                setCipherText(Files.readAllBytes(Paths.get(getInputFileName())));
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private byte[] getLSB(byte[] fullWord, int blockSize) {
        byte[] word = new byte[blockSize];

        if (fullWord.length < blockSize) {
            Arrays.fill(word, (byte) 0);
            System.arraycopy(fullWord, 0, word, fullWord.length, fullWord.length);
        } else {
            System.arraycopy(fullWord, fullWord.length - blockSize, word, 0, blockSize);
        }

        return word;
    }

    private byte[] getMSB(byte[] fullWord, int blockSize) {
        byte[] word = new byte[blockSize];

        if (fullWord.length < blockSize) {
            Arrays.fill(word, (byte) 0);
            System.arraycopy(fullWord, 0, word, 0, fullWord.length);
        } else {
            System.arraycopy(fullWord, 0, word, 0, blockSize);
        }

        return word;
    }

    protected byte[] byteXOR(byte[] word1, byte[] word2) {
        int blockSize = word1.length;

        byte[] result = new byte[blockSize];

        for (int i = 0; i < blockSize; i++) {
            result[i] = (byte) (word1[i] ^ word2[i]);
        }

        return result;
    }

    protected byte[] byteAddOne(byte[] number) {
        for (int i = number.length - 1; i > -1; i--) {
            if (number[i] == 1) {
                number[i] = 0;
            } else {
                number[i] = 1;
                return number;
            }
        }

        number[0] = 0;
        return number;
    }

    protected void writeOutputFile(OperationType operationType) {
        try (FileOutputStream outputStream = new FileOutputStream(getOutputFileName())) {
            if (operationType == OperationType.ENCRYPTION) {
                outputStream.write(getCipherText());
            } else {
                outputStream.write(getPlainText());
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    protected byte[] ECBPart(byte[] ecbInput,int opMode, boolean isReversed) {
        byte[] ecbOutput = new byte[ecbInput.length];

        try {
            if (getAlgorithmType() == AlgorithmType.DES) {
                Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
                SecretKeySpec keySpec = new SecretKeySpec(getKey(), "DES");
                cipher.init(opMode, keySpec);

                ecbOutput = cipher.doFinal(ecbInput);
            } else {
                byte[] key1 = new byte[8];
                byte[] key2 = new byte[8];

                if (fullKey.length < 16) {
                    int key1Size = fullKey.length / 2;
                    int key2Size = fullKey.length / 2 + fullKey.length % 2;

                    byte[] key1Part = getLSB(fullKey, key1Size);
                    byte[] key2Part = getMSB(fullKey, key2Size);

                    System.arraycopy(key1Part, 0, key1, 0, key1Size);
                    System.arraycopy(ARBITRARY_KEY, 0, key1, key1Size, fullKey.length - key1Size);

                    System.arraycopy(key2Part, 0, key2, 0, key2Size);
                    System.arraycopy(ARBITRARY_KEY, 0, key2, key2Size, fullKey.length - key2Size);
                } else {
                    byte[] doubleKey = getLSB(fullKey, 16);

                    key1 = getLSB(doubleKey, 8);
                    key2 = getMSB(doubleKey, 8);
                }
                Cipher cipher1 = Cipher.getInstance("DES/ECB/NoPadding");
                SecretKeySpec keySpec1 = new SecretKeySpec(key1, "DES");
                cipher1.init(opMode, keySpec1);

                byte[] ecbOutput1 = cipher1.doFinal(ecbInput);

                Cipher cipher2 = Cipher.getInstance("DES/ECB/NoPadding");
                SecretKeySpec keySpec2 = new SecretKeySpec(key2, "DES");
                cipher2.init(getReverseOpMode(opMode,isReversed), keySpec2);

                byte[] ecbOutput2 = cipher2.doFinal(ecbOutput1);

                Cipher cipher3 = Cipher.getInstance("DES/ECB/NoPadding");
                SecretKeySpec keySpec3 = new SecretKeySpec(key1, "DES");
                cipher3.init(opMode, keySpec3);

                ecbOutput = cipher3.doFinal(ecbOutput2);

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return ecbOutput;
    }

    protected int getReverseOpMode(int opMode, boolean isReversed){
        if(isReversed){
            if (opMode == 1) return 2;
            else return 1;
        }
        return opMode;
    }

    protected byte[] deletePadding(byte[] decryptedArray){
        int i = decryptedArray.length-1;
        while(decryptedArray[i] == 0){
            i--;
        }
        byte[] outputByte = new byte[i+1];
        System.arraycopy(decryptedArray,0,outputByte,0,i+1);
        return outputByte;
    }

    protected abstract void encryption();
    protected abstract void decryption();

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }

    public String getKeyFileName() {
        return keyFileName;
    }

    public void setKeyFileName(String keyFileName) {
        this.keyFileName = keyFileName;
    }

    public byte[] getPlainText() {
        return plainText;
    }

    public void setPlainText(byte[] plainText) {
        this.plainText = plainText;
    }

    public byte[] getInitializationVector() {
        return initializationVector;
    }

    public void setInitializationVector(byte[] initializationVector) {
        this.initializationVector = initializationVector;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public void setNonce(byte[] nonce) {
        this.nonce = nonce;
    }

    public byte[] getCipherText() {
        return cipherText;
    }

    public void setCipherText(byte[] cipherText) {
        this.cipherText = cipherText;
    }

    public byte[] getFullKey() {
        return fullKey;
    }

    public void setFullKey(byte[] fullKey) {
        this.fullKey = fullKey;
    }
}
