package modes;

import enums.AlgorithmType;
import enums.OperationType;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CTR extends Mode {
    public CTR(
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
        } else {
            decryption();
        }
    }

    @Override
    public void encryption() {
        byte[] cipherText;

        if (getPlainText().length % 8 == 0) {
            cipherText = new byte[getPlainText().length];
        } else {
            cipherText = new byte[getPlainText().length + 8 - getPlainText().length % 8];
        }

        byte[] extendedPlainText = new byte[cipherText.length];
        Arrays.fill(extendedPlainText, (byte) 0);
        System.arraycopy(getPlainText(), 0, extendedPlainText, 0, getPlainText().length);

        byte[] counter = new byte[4];
        Arrays.fill(counter, (byte) 0);

        byte[] ecbInput = new byte[8];
        System.arraycopy(getNonce(), 0, ecbInput, 0, 4);

        for (int i = 0; i < extendedPlainText.length / 8; i++) {
            System.arraycopy(counter, 0, ecbInput, 4, 4);

            byte[] ecbOutput = ECBPart(ecbInput, 1, false);

            byte[] plainTextPart = new byte[8];
            System.arraycopy(extendedPlainText, i * 8, plainTextPart, 0, 8);

            byte[] cipherPart = byteXOR(plainTextPart, ecbOutput);
            System.arraycopy(cipherPart, 0, cipherText, i * 8, 8);

            counter = byteAddOne(counter);
        }

        if (getPlainText().length % 8 != 0) {
            byte[] resultCipherText = new byte[getPlainText().length];
            System.arraycopy(cipherText, 0, resultCipherText, 0, getPlainText().length);
            setCipherText(resultCipherText);
        } else {
            setCipherText(cipherText);
        }

        writeOutputFile(getOperationType());
    }

    @Override
    public void decryption() {
        byte[] plainText;

        if (getCipherText().length % 8 == 0) {
            plainText = new byte[getCipherText().length];
        } else {
            plainText = new byte[getCipherText().length + 8 - getCipherText().length % 8];
        }

        byte[] extendedCipherText = new byte[plainText.length];
        Arrays.fill(extendedCipherText, (byte) 0);

        System.arraycopy(getCipherText(), 0, extendedCipherText, 0, getCipherText().length);

        byte[] counter = new byte[4];
        Arrays.fill(counter, (byte) 0);

        for (int i = 0; i < extendedCipherText.length / 8; i++) {
            byte[] ecbInput = new byte[8];
            System.arraycopy(getNonce(), 0, ecbInput, 0, 4);
            System.arraycopy(counter, 0, ecbInput, 4, 4);

            byte[] ecbOutput = ECBPart(ecbInput, 1, false);

            byte[] cipherTextPart = new byte[8];
            System.arraycopy(extendedCipherText, i * 8, cipherTextPart, 0, 8);

            byte[] plainTextPart = byteXOR(cipherTextPart, ecbOutput);
            System.arraycopy(plainTextPart, 0, plainText, i * 8, 8);

            counter = byteAddOne(counter);
        }

        if (getCipherText().length % 8 != 0) {
            byte[] resultPlainText = new byte[getCipherText().length];
            System.arraycopy(plainText, 0, resultPlainText, 0, getCipherText().length);
            setPlainText(resultPlainText);
        } else {
            setPlainText(plainText);
        }

        writeOutputFile(getOperationType());
    }
}
