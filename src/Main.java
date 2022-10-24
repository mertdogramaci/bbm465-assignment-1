public class Main {
    public static void main(String[] args) {
        if (args.length == 9) {
            FileCipher fileCipher = new FileCipher(args);
        } else {
            System.out.println("You have typed the command-line argument as incomplete or it is completely empty!");
        }
    }
}