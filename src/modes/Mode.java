package modes;

import enums.AlgorithmType;
import enums.OperationType;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;

public abstract class Mode {
    private OperationType operationType;
    private String inputFileName;
    private String outputFileName;
    private AlgorithmType algorithmType;
    private String keyFileName;
    private byte[] plainText;
    private byte[] initializationVector;
    private byte[] key;
    private byte[] nonce;
    private byte[] cipherText;

    protected void readKeyFile() {
        try {
            File keyFile = new File("files/" + getKeyFileName());
            BufferedReader keyFileText = new BufferedReader(new FileReader(keyFile));
            String keyFileString = keyFileText.readLine();

            byte[] fullIV = keyFileString.split(" - ")[0].getBytes(StandardCharsets.UTF_8);
            byte[] IV = getLSB(fullIV, 8);
            setInitializationVector(IV);

            byte[] fullKey = keyFileString.split(" - ")[1].getBytes(StandardCharsets.UTF_8);
            byte[] key = getLSB(fullKey, 8);
            setKey(key);

            byte[] fullNonce = keyFileString.split(" - ")[2].getBytes(StandardCharsets.UTF_8);
            byte[] nonce = getLSB(fullNonce, 4);
            setNonce(nonce);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    protected void readInputFile() {
        try {
            File inputFile = new File("files/" + getInputFileName());
            BufferedReader inputFileText = new BufferedReader(new FileReader(inputFile));
            String inputFileString = inputFileText.readLine();

            if (this.operationType == OperationType.ENCRYPTION) {
                setPlainText(inputFileString.getBytes(StandardCharsets.UTF_8));
            } else {
                setCipherText(inputFileString.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private byte[] getLSB(byte[] fullWord, int blockSize) {
        byte[] word = new byte[blockSize];
        System.arraycopy(fullWord, fullWord.length - blockSize, word, 0, blockSize);
        return word;
    }

    protected byte[] byteXOR(byte[] word1, byte[] word2) {
        int blockSize = word1.length;

        byte[] result = new byte[blockSize];

        for (int i = 0; i < blockSize; i++) {
            result[i] = (byte) (word1[i] ^ word2[i]);
        }

        return result;
    }

    protected byte[] byteAddOne(byte[] number) {
        for (int i = number.length - 1; i > -1; i--) {
            if (number[i] == 1) {
                number[i] = 0;
            } else {
                number[i] = 1;
                return number;
            }
        }

        number[0] = 0;
        return number;
    }

    protected void writeOutputFile(OperationType operationType) {
        try (FileOutputStream outputStream = new FileOutputStream("files/" + getOutputFileName())) {
            if (operationType == OperationType.ENCRYPTION) {
                outputStream.write(getCipherText());
            } else {
                outputStream.write(getPlainText());
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    protected byte[] ECBPart(byte[] ecbInput) {
        byte[] ecbOutput = new byte[ecbInput.length];

        try {
            if (getAlgorithm() == AlgorithmType.DES) {
                Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
                SecretKeySpec keySpec = new SecretKeySpec(getKey(), "DES");
                cipher.init(Cipher.ENCRYPT_MODE, keySpec);

                ecbOutput = cipher.doFinal(ecbInput);
            } else {
                if (getOperationType() == OperationType.ENCRYPTION) {
                    Cipher cipher1 = Cipher.getInstance("DES/ECB/NoPadding");
                    SecretKeySpec keySpec1 = new SecretKeySpec(getKey(), "DES");
                    cipher1.init(Cipher.ENCRYPT_MODE, keySpec1);

                    byte[] ecbOutput1 = cipher1.doFinal(ecbInput);

                    Cipher cipher2 = Cipher.getInstance("DES/ECB/NoPadding");
                    SecretKeySpec keySpec2 = new SecretKeySpec(getKey(), "DES");
                    cipher2.init(Cipher.DECRYPT_MODE, keySpec2);

                    byte[] ecbOutput2 = cipher2.doFinal(ecbOutput1);

                    Cipher cipher3 = Cipher.getInstance("DES/ECB/NoPadding");
                    SecretKeySpec keySpec3 = new SecretKeySpec(getKey(), "DES");
                    cipher3.init(Cipher.ENCRYPT_MODE, keySpec3);

                    ecbOutput = cipher3.doFinal(ecbOutput2);
                } else {
                    Cipher cipher1 = Cipher.getInstance("DES/ECB/NoPadding");
                    SecretKeySpec keySpec1 = new SecretKeySpec(getKey(), "DES");
                    cipher1.init(Cipher.DECRYPT_MODE, keySpec1);

                    byte[] ecbOutput1 = cipher1.doFinal(ecbInput);

                    Cipher cipher2 = Cipher.getInstance("DES/ECB/NoPadding");
                    SecretKeySpec keySpec2 = new SecretKeySpec(getKey(), "DES");
                    cipher2.init(Cipher.ENCRYPT_MODE, keySpec2);

                    byte[] ecbOutput2 = cipher2.doFinal(ecbOutput1);

                    Cipher cipher3 = Cipher.getInstance("DES/ECB/NoPadding");
                    SecretKeySpec keySpec3 = new SecretKeySpec(getKey(), "DES");
                    cipher3.init(Cipher.DECRYPT_MODE, keySpec3);

                    ecbOutput = cipher3.doFinal(ecbOutput2);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return ecbOutput;
    }

    protected abstract void encryption();
    protected abstract void decryption();

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
        return algorithmType;
    }

    public void setAlgorithm(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
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

    public byte[] getCipherText() {
        return cipherText;
    }

    public void setCipherText(byte[] cipherText) {
        this.cipherText = cipherText;
    }
}
