public class FileCipher {
    public static void main(String[] args) {
        if (args.length == 8) {
            ArgumentParser argumentParser = new ArgumentParser(args);
        } else {
            System.out.println("You have typed the command-line argument incorrectly or it is completely empty!");
        }
    }
}