import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Equation {
    public static void main(String[] args) throws Exception {
        // Step 1: Read JSON file
        String jsonData = new String(Files.readAllBytes(Paths.get("roots.json")));

        // Step 2: Parse JSON
        JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();

        // Extract keys
        JsonObject keys = jsonObject.getAsJsonObject("keys");
        int n = keys.get("n").getAsInt();
        int k = keys.get("k").getAsInt();

        System.out.println("n = " + n + ", k = " + k);

        // Step 3: Extract roots
        List<Integer> xs = new ArrayList<>();
        List<Integer> ys = new ArrayList<>();

        for (String rootKey : jsonObject.keySet()) {
            if (rootKey.equals("keys")) continue;

            int x = Integer.parseInt(rootKey);
            JsonObject rootObj = jsonObject.getAsJsonObject(rootKey);

            int base = Integer.parseInt(rootObj.get("base").getAsString());
            String value = rootObj.get("value").getAsString();

            int y = Integer.parseInt(value, base); // decode base-n
            xs.add(x);
            ys.add(y);

            System.out.println("Decoded root: x=" + x + ", y=" + y);
        }

        // Step 4: Pick only k roots (first k)
        List<Integer> xsSelected = xs.subList(0, k);
        List<Integer> ysSelected = ys.subList(0, k);

        // Step 5: Lagrange interpolation at x=0 (constant term)
        double c = lagrangeAtZero(xsSelected, ysSelected);
        System.out.println("\nConstant term (c) = " + c);
    }

    static double lagrangeAtZero(List<Integer> xs, List<Integer> ys) {
        int n = xs.size();
        double result = 0.0;

        for (int i = 0; i < n; i++) {
            double term = ys.get(i);
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    term *= (0 - xs.get(j)) / (double)(xs.get(i) - xs.get(j));
                }
            }
            result += term;
        }
        return result;
    }
}
