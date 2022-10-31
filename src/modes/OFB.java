package modes;

import enums.AlgorithmType;
import enums.OperationType;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class OFB extends Mode {

    public OFB(
            OperationType operationType,
            String inputFileName,
            String outputFileName,
            AlgorithmType algorithmType,
            String keyFileName
    ) {
        setOperationType(operationType);
        setInputFileName(inputFileName);
        setOutputFileName(outputFileName);
        setAlgorithmType(algorithmType);
        setKeyFileName(keyFileName);

        readKeyFile();

        readInputFile();

        if (getOperationType() == OperationType.ENCRYPTION) {
            encryption();
//            decryption();
        } else {
            decryption();
        }

    }

    @Override
    protected void encryption() {
        byte[] cipherText;
        System.out.println(Arrays.toString(getPlainText()));
        System.out.println(getPlainText().length);

        if (getPlainText().length % 8 == 0) {
            cipherText = new byte[getPlainText().length];
        } else {
            cipherText = new byte[getPlainText().length + 8 - getPlainText().length % 8];
        }

        byte[] extendedPlainText = new byte[cipherText.length];
        Arrays.fill(extendedPlainText, (byte) 0);

        System.arraycopy(getPlainText(), 0, extendedPlainText, 0, getPlainText().length);

        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(getKey(), "DES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] ecbInput = getInitializationVector();

            for (int i = 0; i < extendedPlainText.length / 8; i++) {
                byte[] plainTextPart = new byte[8];
                System.arraycopy(extendedPlainText, i * 8, plainTextPart, 0, 8);

                byte[] ecbOutput = cipher.doFinal(ecbInput);

                byte[] cipherPart = byteXOR(ecbOutput,plainTextPart);
                System.arraycopy(cipherPart, 0, cipherText, i * 8, 8);

                ecbInput = ecbOutput;
            }

//            if (getPlainText().length % 8 != 0) {
//                byte[] resultCipherText = new byte[getPlainText().length];
//                System.arraycopy(cipherText, 0, resultCipherText, 0, getPlainText().length);
//                setCipherText(resultCipherText);
//            } else {
//                setCipherText(cipherText);
//            }
            setCipherText(cipherText);//sona eklenenleri silmeden direkt ciphertexte set etcez decryptte sorun oluyor yoksa
            System.out.println(Arrays.toString(getCipherText()));
            System.out.println(getCipherText().length);
            System.out.println(Arrays.toString(cipherText));
            System.out.println(cipherText.length);
            System.out.println(new String(cipherText, StandardCharsets.UTF_8));
            System.out.println(new String(getCipherText(), StandardCharsets.UTF_8));

            writeOutputFile(getOperationType());
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }

    @Override
    protected void decryption() {
        System.out.println(Arrays.toString(getCipherText()));
        System.out.println(new String(getCipherText(), StandardCharsets.UTF_8));
        System.out.println(getCipherText().length);
        byte[] plainText;

        if (getCipherText().length % 8 == 0) {
            plainText = new byte[getCipherText().length];
        } else {
            plainText = new byte[getCipherText().length + 8 - getCipherText().length % 8];
        }

        byte[] extendedCipherText = new byte[plainText.length];
        Arrays.fill(extendedCipherText, (byte) 0);

        System.arraycopy(getCipherText(), 0, extendedCipherText, 0, getCipherText().length);

        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(getKey(), "DES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] ecbInput = getInitializationVector();

            for (int i = 0; i < extendedCipherText.length / 8; i++) {
                byte[] cipherTextPart = new byte[8];
                System.arraycopy(extendedCipherText, i * 8, cipherTextPart, 0, 8);
                byte[] ecbOutput = cipher.doFinal(ecbInput);
                byte[] plainTextPart = byteXOR(ecbOutput,cipherTextPart);

                System.arraycopy(plainTextPart, 0, plainText, i * 8, 8);

                ecbInput = ecbOutput;
            }

            if (getCipherText().length % 8 != 0) {
                byte[] resultPlainText = new byte[getCipherText().length];
                System.arraycopy(plainText, 0, resultPlainText, 0, getCipherText().length);
                setPlainText(resultPlainText);
            } else {
                setPlainText(plainText);
            }
            System.out.println(Arrays.toString(plainText));
            System.out.println(plainText.length);
            System.out.println(Arrays.toString(getPlainText()));
            System.out.println(getPlainText().length);

            System.out.println(new String(plainText, StandardCharsets.UTF_8));
            System.out.println(new String(getPlainText(), StandardCharsets.UTF_8));

            writeOutputFile(getOperationType());
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }
}
