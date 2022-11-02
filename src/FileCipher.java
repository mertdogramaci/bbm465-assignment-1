public class FileCipher {
    public static void main(String[] args) {
        if (args.length == 8) {

            FirstLayer firstLayer = new FirstLayer(args);
        } else {
            System.out.println(" You have typed the command-line argument as incomplete or it is completely empty!");
        }
    }
}
