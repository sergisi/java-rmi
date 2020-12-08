package adaptators;

import java.util.Scanner;

public class AdaptSystem {

    public void printLn(String line) {
        System.out.println(line);
    }

    public String readLn() {
        return new Scanner(System.in).nextLine();
    }
}
