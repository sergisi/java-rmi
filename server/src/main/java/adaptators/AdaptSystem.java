package adaptators;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdaptSystem {

    public void println(String line) {
        System.out.println(line);
    }

    public String readLn() {
        return new Scanner(System.in).nextLine();
    }

    public List<String> getContents(String filepath) throws IOException {
        return Files.lines(Paths.get(filepath)).collect(Collectors.toList());
    }

    public PrintWriter getOutputFile(String filepath) throws  IOException {
        return new PrintWriter(filepath);
    }
}

