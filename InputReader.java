//Ida Andersson idan1222

import java.util.Scanner;
import java.util.ArrayList;
import java.io.InputStream;

public class InputReader {

    private static ArrayList<InputStream> existingStreams = new ArrayList<>();
    private Scanner input;

    public InputReader() {
        this(System.in);
    }

    public InputReader(InputStream inputStream) {
        if (existingStreams.contains(inputStream)) {
            throw new IllegalStateException("ERROR");
        } else {
            existingStreams.add(inputStream);
            input = new Scanner(inputStream, "UTF-8");
        }
    }

    public int readInt(String prompt) {
        System.out.print(prompt + "?>");
        int i = input.nextInt();
        input.nextLine();
        return i;
    }

    public double readDouble(String prompt) {
        System.out.print(prompt + "?>");
        double d = input.nextDouble();
        input.nextLine();
        return d;
    }

    public String readLine(String prompt) {
        System.out.print(prompt + "?>");
        String s = input.nextLine();
        return s;
    }

}
