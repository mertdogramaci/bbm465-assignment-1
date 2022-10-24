package algorithms;

import enums.AlgorithmType;
import enums.OperationType;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class CFB {
    private OperationType operationType;
    private String inputFileName;
    private String outputFileName;
    private AlgorithmType algorithm;
    private String keyFileName;

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
            File inputFile = new File("files/" + this.inputFileName);
            BufferedReader inputText = new BufferedReader(new FileReader(inputFile));
            String inputString = inputText.readLine();
            byte [] plainText = inputString.getBytes(StandardCharsets.UTF_8);

            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            System.out.println();
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }

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
}
