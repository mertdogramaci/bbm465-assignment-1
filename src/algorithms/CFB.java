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

        readKeyFile(64);

        readInputFile();

        System.out.println("key:\t" + Arrays.toString(getKey()) + "\t\t\t\t\t\tlength = " + getKey().length);
        System.out.println("IV:\t\t" + Arrays.toString(getInitializationVector()) + "\t\tlength = " + getInitializationVector().length);

        if (getOperationType() == OperationType.ENCRYPTION) {
            encryption();
        } else {
            decryption();
        }

    }

    @Override
    protected void encryption() {
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
    protected void decryption() {

    }


}
