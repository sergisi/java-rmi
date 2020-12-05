package adaptators;

import java.util.Scanner;

public class AdaptSystem {

    public void println(String line) {
        System.out.println(line);
    }

    public void exit(int statusCode) {
        System.exit(statusCode);
    }

    public String readLn() {
        return new Scanner(System.in).nextLine();
    }
}
