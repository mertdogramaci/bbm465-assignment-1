import modes.*;
import enums.*;

import java.io.*;

public class FileCipher {
    private OperationType operationType;
    private AlgorithmType algorithmType;
    private String inputFileName;
    private String outputFileName;
    private String keyFileName;

    public static void main(String[] args) {
        if (args.length == 8) {

            FileCipher fileCipher = new FileCipher(args);
        } else {
            System.out.println(" You have typed the command-line argument as incomplete or it is completely empty!");
        }
    }

    public FileCipher(String[] args) {
        // if-else block for operation type (means encryption or decryption) argument
        if (args[0].split("")[1].equals("e")) {
            setOperationType(OperationType.ENCRYPTION);
        } else if (args[0].split("")[1].equals("d")) {
            setOperationType(OperationType.DECRYPTION);
        } else {
            // TODO: exception da throw edilebilir belki ?
            System.out.println("You have typed the encryption/decryption argument wrongly! " +
                    "It should be in '-e' or '-d' format!");
            System.exit(0);
        }

        setInputFileName(args[2]);
        setOutputFileName(args[4]);
        setKeyFileName(args[7]);

        // if-else block for algorithm argument
        if (args[5].equals("DES")) {
            setAlgorithmType(AlgorithmType.DES);
        } else if (args[5].equals("3DES")) {
            setAlgorithmType(AlgorithmType.TRIPLEDES);
        } else {
            System.out.println("You have typed the algorithm argument wrongly! It should be 'DES' or '3DES'!");
            System.exit(0);
        }

        long startTime = System.currentTimeMillis();

        // if-else block for mode argument
        if (args[6].equals("CBC")) {
            CBC cbc = new CBC(
                    this.operationType,
                    this.inputFileName,
                    this.outputFileName,
                    this.algorithmType,
                    this.keyFileName
            );
        } else if (args[6].equals("CFB")) {
            CFB cfb = new CFB(
                    this.operationType,
                    this.inputFileName,
                    this.outputFileName,
                    this.algorithmType,
                    this.keyFileName
            );
        } else if (args[6].equals("OFB")) {
            OFB ofb = new OFB(
                    this.operationType,
                    this.inputFileName,
                    this.outputFileName,
                    this.algorithmType,
                    this.keyFileName
            );
        } else if (args[6].equals("CTR")) {
            CTR ctr = new CTR(
                    this.operationType,
                    this.inputFileName,
                    this.outputFileName,
                    this.algorithmType,
                    this.keyFileName
            );
        } else {
            System.out.println("You have typed the mode argument wrongly! It should be 'CBC', 'CFB', 'OFB', or 'CTR'!");
            System.exit(0);
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        String logOutput = getInputFileName() + "\s" + getOutputFileName() + "\s";

        if (getOperationType() == OperationType.ENCRYPTION) {
            logOutput += "enc\s";
        } else {
            logOutput += "dec\s";
        }

        logOutput += getAlgorithmType() + "\s" + args[6] + "\s" + executionTime + "\n";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("run.log", true));
            writer.write(logOutput);
            writer.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
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

    public String getKeyFileName() {
        return keyFileName;
    }

    public void setKeyFileName(String keyFileName) {
        this.keyFileName = keyFileName;
    }
}
