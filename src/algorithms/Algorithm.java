package algorithms;

import enums.AlgorithmType;
import enums.OperationType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

    protected void readKeyFile(int blockSize) {
        try {
            File keyFile = new File("files/" + getKeyFileName());
            BufferedReader keyFileText = new BufferedReader(new FileReader(keyFile));
            String keyFileString = keyFileText.readLine();

            byte[] fullIV = keyFileString.split(" - ")[0].getBytes(StandardCharsets.UTF_8);
            initializeIV(fullIV);

            setKey(keyFileString.split(" - ")[1].getBytes(StandardCharsets.UTF_8));
            setNonce(keyFileString.split(" - ")[2].getBytes(StandardCharsets.UTF_8));
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
    }

    protected void readInputFile() {
        try {
            File inputFile = new File("files/" + getInputFileName());
            BufferedReader inputFileText = new BufferedReader(new FileReader(inputFile));
            String inputFileString = inputFileText.readLine();

            setPlainText(inputFileString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
    }

    protected void initializeIV(byte[] fullIV) {
        byte[] IV = new byte[16];
        System.arraycopy(fullIV, fullIV.length - 16, IV, 0, 16);
        setInitializationVector(IV);
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
}
