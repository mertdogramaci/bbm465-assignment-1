package algorithms;

import enums.AlgorithmType;
import enums.OperationType;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CFB extends Algorithm {

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
            File keyFile = new File("files/" + getKeyFileName());
            BufferedReader keyFileText = new BufferedReader(new FileReader(keyFile));
            String keyFileString = keyFileText.readLine();

            byte[] fullIV = keyFileString.split(" - ")[0].getBytes(StandardCharsets.UTF_8);
            InitializeIV(fullIV);

            setKey(keyFileString.split(" - ")[1].getBytes(StandardCharsets.UTF_8));
            setNonce(keyFileString.split(" - ")[2].getBytes(StandardCharsets.UTF_8));
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }

        try {
            File inputFile = new File("files/" + getInputFileName());
            BufferedReader inputFileText = new BufferedReader(new FileReader(inputFile));
            String inputFileString = inputFileText.readLine();

            setPlainText(inputFileString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }

        System.out.println("key:\t" + Arrays.toString(getKey()) + "\t\t\t\t\t\tlength = " + getKey().length);
        System.out.println("IV:\t\t" + Arrays.toString(getInitializationVector()) + "\t\tlength = " + getInitializationVector().length);

        if (getOperationType() == OperationType.ENCRYPTION) {
            Encryption();
        } else {
            Decryption();
        }

    }

    @Override
    protected void Encryption() {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(getKey(), "DES");
            IvParameterSpec ivSpec = new IvParameterSpec(getInitializationVector());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            System.out.println(cipher);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    protected void Decryption() {

    }


}
