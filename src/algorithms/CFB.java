package algorithms;

import enums.AlgorithmType;
import enums.OperationType;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class CFB {
    private OperationType operationType;
    private String inputFileName;
    private String outputFileName;
    private AlgorithmType algorithm;
    private String keyFileName;
    private byte[] plainText;
    private byte[] initializationVector;
    private byte[] key;
    private byte[] nonce;

    public CFB(
            OperationType operationType,
            String inputFileName,
            String outputFileName,
            AlgorithmType algorithm,
            String keyFileName
    ) {
        setOperationType(operationType);
        setInputFileName(inputFileName);
        setOutputFileName(outputFileName);
        setAlgorithm(algorithm);
        setKeyFileName(keyFileName);

        try {
            File keyFile = new File("files/" + this.keyFileName);
            BufferedReader keyFileText = new BufferedReader(new FileReader(keyFile));
            String keyFileString = keyFileText.readLine();

            setInitializationVector(keyFileString.split(" - ")[0].getBytes(StandardCharsets.UTF_8));
            setKey(keyFileString.split(" - ")[1].getBytes(StandardCharsets.UTF_8));
            setNonce(keyFileString.split(" - ")[2].getBytes(StandardCharsets.UTF_8));
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }

        try {
            File inputFile = new File("files/" + this.inputFileName);
            BufferedReader inputFileText = new BufferedReader(new FileReader(inputFile));
            String inputFileString = inputFileText.readLine();

            setPlainText(inputFileString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }

        if (this.operationType == OperationType.ENCRYPTION) {
            CFBEncryption();
        } else {
            CFBDecryption();
        }

    }

    private void CFBEncryption() {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(this.key, "DES");
            IvParameterSpec ivSpec = new IvParameterSpec(this.initializationVector);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            System.out.println(cipher);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void CFBDecryption() {

    }

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
