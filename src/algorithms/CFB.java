package algorithms;

import enums.AlgorithmType;
import enums.OperationType;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
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

        if (getOperationType() == OperationType.ENCRYPTION) {
            encryption();
            decryption();
        } else {
            decryption();
        }

    }

    @Override
    protected void encryption() {
        byte[] cipherText;

        if (getPlainText().length % 8 == 0) {
             cipherText = new byte[getPlainText().length];
        } else {
            cipherText = new byte[getPlainText().length + 8 - getPlainText().length % 8];
        }

        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(getKey(), "DES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] ecbInput = getInitializationVector();

            for (int i = 0; i < getPlainText().length / 8; i++) {
                byte[] ecbOutput = cipher.doFinal(ecbInput);

                byte[] plainTextPart = new byte[8];
                System.arraycopy(getPlainText(), i * 8, plainTextPart, 0, 8);

                byte[] cipherPart = byteXOR(plainTextPart, ecbOutput);
                System.arraycopy(cipherPart, 0, cipherText, i * 8, 8);

                ecbInput = cipherPart;
            }

            if (getPlainText().length % 8 != 0) {
                byte[] ecbOutput = cipher.doFinal(ecbInput);

                int length = getPlainText().length % 8;

                byte[] plainTextPart = new byte[8];
                System.arraycopy(getPlainText(), getPlainText().length / 8 * 8, plainTextPart, 0, length);

                byte[] cipherPart = byteXOR(plainTextPart, ecbOutput);
                System.arraycopy(cipherPart, 0, cipherText, getPlainText().length / 8 * 8, length);
            }

            // TODO: will be deleted
            setCipherText(cipherText);

            System.out.println(Arrays.toString(getCipherText()));
            System.out.println(getCipherText().length);
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }

    @Override
    protected void decryption() {
        byte[] plainText;

        if (getCipherText().length % 8 == 0) {
            plainText = new byte[getCipherText().length];
        } else {
            plainText = new byte[getCipherText().length + 8 - getCipherText().length % 8];
        }

        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(getKey(), "DES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] ecbInput = getInitializationVector();

            for (int i = 0; i < getCipherText().length / 8; i++) {
                byte[] ecbOutput = cipher.doFinal(ecbInput);

                byte[] cipherTextPart = new byte[8];
                System.arraycopy(getCipherText(), i * 8, cipherTextPart, 0, 8);

                byte[] plainTextPart = byteXOR(cipherTextPart, ecbOutput);
                System.arraycopy(plainTextPart, 0, plainText, i * 8, 8);

                ecbInput = cipherTextPart;
            }

            if (getCipherText().length % 8 != 0) {
                byte[] ecbOutput = cipher.doFinal(ecbInput);

                int length = getCipherText().length % 8;

                byte[] cipherTextPart = new byte[8];
                System.arraycopy(getCipherText(), getCipherText().length / 8 * 8, cipherTextPart, 0, length);

                byte[] cipherPart = byteXOR(cipherTextPart, ecbOutput);
                System.arraycopy(cipherPart, 0, plainText, getCipherText().length / 8 * 8, length);
            }

            System.out.println(Arrays.toString(plainText));
            System.out.println(plainText.length);

            System.out.println(new String(plainText, StandardCharsets.UTF_8));
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }
}
