import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PolynomialSecret {
    
    public static int decode(String v, int b) {
        return Integer.parseInt(v, b);
    }

    public static double[] solve(double[][] A, double[] B) {
        int n = B.length;
        for (int i = 0; i < n; i++) {
            int max = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(A[j][i]) > Math.abs(A[max][i])) max = j;
            }
            double[] temp = A[i]; A[i] = A[max]; A[max] = temp;
            double t = B[i]; B[i] = B[max]; B[max] = t;

            for (int j = i + 1; j < n; j++) {
                double f = A[j][i] / A[i][i];
                B[j] -= f * B[i];
                for (int k = i; k < n; k++) {
                    A[j][k] -= f * A[i][k];
                }
            }
        }

        double[] X = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = B[i];
            for (int j = i + 1; j < n; j++) {
                sum -= A[i][j] * X[j];
            }
            X[i] = sum / A[i][i];
        }
        return X;
    }

    public static void main(String[] args) {
        try {
            String content = new String(Files.readAllBytes(Paths.get("input.json")));
            JSONObject obj = new JSONObject(content);

            JSONObject keys = obj.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");

            List<Integer> xs = new ArrayList<>();
            List<Integer> ys = new ArrayList<>();

            for (String key : obj.keySet()) {
                if (key.equals("keys")) continue;
                JSONObject p = obj.getJSONObject(key);
                int b = Integer.parseInt(p.getString("base"));
                String v = p.getString("value");
                int x = Integer.parseInt(key);
                int y = decode(v, b);
                xs.add(x);
                ys.add(y);
            }

            double[][] A = new double[k][k];
            double[] B = new double[k];
            for (int i = 0; i < k; i++) {
                int x = xs.get(i);
                int y = ys.get(i);
                for (int j = 0; j < k; j++) {
                    A[i][j] = Math.pow(x, k - j - 1);
                }
                B[i] = y;
            }

            double[] coeff = solve(A, B);
            double c = coeff[coeff.length - 1];
            System.out.println("Secret (c): " + c);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}