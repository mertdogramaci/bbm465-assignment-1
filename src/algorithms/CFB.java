package algorithms;

import enums.AlgorithmType;
import enums.OperationType;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

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
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] deneme = cipher.doFinal(getInitializationVector());
            byte[] result = byteXOR(deneme, getPlainText());
            System.out.println(new String(result, StandardCharsets.UTF_8));
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }

    @Override
    protected void decryption() {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(getKey(), "DES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] deneme = cipher.doFinal(getInitializationVector());
            byte[] result = byteXOR(deneme, getPlainText());
            System.out.println(new String(result, StandardCharsets.UTF_8));
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }


}
