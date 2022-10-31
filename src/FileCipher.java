import modes.*;
import enums.*;

public class FileCipher {
    private OperationType operationType;
    private AlgorithmType algorithm;
    private String inputFileName;
    private String outputFileName;
    private String keyFileName;

    public FileCipher(String[] args) {
        // if-else block for operation type (means encryption or decryption) argument
        if (args[1].split("")[1].equals("e")) {
            setOperationType(OperationType.ENCRYPTION);
        } else if (args[1].split("")[1].equals("d")) {
            setOperationType(OperationType.DECRYPTION);
        } else {
            // TODO: exception da throw edilebilir belki ?
            System.out.println("You have typed the encryption/decryption argument wrongly! " +
                    "It should be in '-e' or '-d' format!");
            System.exit(0);
        }

        setInputFileName(args[3]);
        setOutputFileName(args[5]);
        setKeyFileName(args[8]);

        // if-else block for algorithm argument
        if (args[6].equals("DES")) {
            setAlgorithm(AlgorithmType.DES);
        } else if (args[6].equals("3DES")) {
            setAlgorithm(AlgorithmType.TRIPLEDES);
        } else {
            System.out.println("You have typed the algorithm argument wrongly! It should be 'DES' or '3DES'!");
            System.exit(0);
        }

        // if-else block for mode argument
        if (args[7].equals("CBC")) {
            CBC cbc = new CBC(
                    this.operationType,
                    this.inputFileName,
                    this.outputFileName,
                    this.algorithm,
                    this.keyFileName
            );
        } else if (args[7].equals("CFB")) {
            CFB cfb = new CFB(
                    this.operationType,
                    this.inputFileName,
                    this.outputFileName,
                    this.algorithm,
                    this.keyFileName
            );
        } else if (args[7].equals("OFB")) {
            OFB ofb = new OFB(
                    this.operationType,
                    this.inputFileName,
                    this.outputFileName,
                    this.algorithm,
                    this.keyFileName
            );
        } else if (args[7].equals("CTR")) {
            CTR ctr = new CTR(
                    this.operationType,
                    this.inputFileName,
                    this.outputFileName,
                    this.algorithm,
                    this.keyFileName
            );
        } else {
            System.out.println("You have typed the mode argument wrongly! It should be 'CBC', 'CFB', 'OFB', or 'CTR'!");
            System.exit(0);
        }
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public AlgorithmType getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(AlgorithmType algorithm) {
        this.algorithm = algorithm;
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
