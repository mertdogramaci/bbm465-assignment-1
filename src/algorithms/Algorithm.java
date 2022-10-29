package algorithms;

import enums.AlgorithmType;
import enums.OperationType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public abstract class Algorithm {
    private OperationType operationType;
    private String inputFileName;
    private String outputFileName;
    private AlgorithmType algorithm;
    private String keyFileName;
    private byte[] plainText;
    private byte[] initializationVector;
    private byte[] key;
    private byte[] nonce;
    private byte[] cipherText;

    protected void readKeyFile(int blockSize) {
        try {
            File keyFile = new File("files/" + getKeyFileName());
            BufferedReader keyFileText = new BufferedReader(new FileReader(keyFile));
            String keyFileString = keyFileText.readLine();

            byte[] fullIV = keyFileString.split(" - ")[0].getBytes(StandardCharsets.UTF_8);
            byte[] IV = getLSB(fullIV, 8);
            setInitializationVector(IV);

            byte[] fullKey = keyFileString.split(" - ")[1].getBytes(StandardCharsets.UTF_8);
            byte[] key = getLSB(fullKey, 8);
            setKey(key);

            setNonce(keyFileString.split(" - ")[2].getBytes(StandardCharsets.UTF_8));
        } catch (IOException ioException) {
            System.out.println(ioException.toString());
        }
    }

    protected void readInputFile() {
        try {
            File inputFile = new File("files/" + getInputFileName());
            BufferedReader inputFileText = new BufferedReader(new FileReader(inputFile));
            String inputFileString = inputFileText.readLine();

            setPlainText(inputFileString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ioException) {
            System.out.println(ioException.toString());
        }
    }

    protected byte[] getLSB(byte[] fullWord, int blockSize) {
        byte[] word = new byte[blockSize];
        System.arraycopy(fullWord, fullWord.length - blockSize, word, 0, blockSize);
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

    protected void writeOutputFile(OperationType operationType) {
        try (FileOutputStream outputStream = new FileOutputStream("files/" + getOutputFileName())) {
            if (operationType == OperationType.ENCRYPTION) {
                outputStream.write(getCipherText());
            } else {
                outputStream.write(getPlainText());
            }
        } catch (IOException ioException) {
            System.out.println(ioException.toString());
        }
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

    public AlgorithmType getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(AlgorithmType algorithm) {
        this.algorithm = algorithm;
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
}
