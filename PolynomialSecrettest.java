import java.util.*;

public class PolynomialSecrettest {

    // Function to decode y from base
    public static int decode(String value, int base) {
        return Integer.parseInt(value, base);
    }

    // Solve system of linear equations using Gaussian elimination
    public static double[] gaussianElimination(double[][] A, double[] B) {
        int n = B.length;
        for (int i = 0; i < n; i++) {
            // Partial pivot
            int max = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(A[j][i]) > Math.abs(A[max][i])) max = j;
            }
            double[] temp = A[i]; A[i] = A[max]; A[max] = temp;
            double t = B[i]; B[i] = B[max]; B[max] = t;

            // Eliminate below
            for (int j = i + 1; j < n; j++) {
                double factor = A[j][i] / A[i][i];
                B[j] -= factor * B[i];
                for (int k = i; k < n; k++) {
                    A[j][k] -= factor * A[i][k];
                }
            }
        }

        // Back substitution
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
        Scanner sc = new Scanner(System.in);

        // Read n and k
        System.out.print("Enter n (total points): ");
        int n = sc.nextInt();
        System.out.print("Enter k (threshold): ");
        int k = sc.nextInt();

        List<Integer> xs = new ArrayList<>();
        List<Integer> ys = new ArrayList<>();

        // Read n points
        System.out.println("Enter the points as: x base value");
        for (int i = 0; i < n; i++) {
            int x = sc.nextInt();
            int base = sc.nextInt();
            String value = sc.next();
            int y = decode(value, base);
            xs.add(x);
            ys.add(y);
        }

        // Take first k points
        double[][] A = new double[k][k];
        double[] B = new double[k];
        for (int i = 0; i < k; i++) {
            int x = xs.get(i);
            int y = ys.get(i);
            for (int j = 0; j < k; j++) {
                A[i][j] = Math.pow(x, k - j - 1); // descending powers
            }
            B[i] = y;
        }

        // Solve system
        double[] coeff = gaussianElimination(A, B);

        // Secret = last coefficient (c)
        double c = coeff[coeff.length - 1];
        System.out.println("Secret (c): " + Math.round(c));

        sc.close();
    }
}
