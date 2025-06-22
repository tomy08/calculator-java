package app.calculator;

public class MatrixUtils {
    public static double[][] suma(double[][] a, double[][] b) {
        int n = a.length, m = a[0].length;
        if (b.length != n || b[0].length != m)
            throw new IllegalArgumentException("Dimensiones distintas");
        double[][] r = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                r[i][j] = a[i][j] + b[i][j];
        return r;
    }

    public static double[][] resta(double[][] a, double[][] b) {
        int n = a.length, m = a[0].length;
        if (b.length != n || b[0].length != m)
            throw new IllegalArgumentException("Dimensiones distintas");
        double[][] r = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                r[i][j] = a[i][j] - b[i][j];
        return r;
    }

    public static double[][] escalarPorMatriz(double escalar, double[][] mat) {
        int n = mat.length, m = mat[0].length;
        double[][] r = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                r[i][j] = escalar * mat[i][j];
        return r;
    }

    public static double[][] multiplicar(double[][] a, double[][] b) {
        int n = a.length, m = a[0].length, p = b[0].length;
        if (b.length != m)
            throw new IllegalArgumentException("Dimensiones incompatibles");
        double[][] r = new double[n][p];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < p; j++)
                for (int k = 0; k < m; k++)
                    r[i][j] += a[i][k] * b[k][j];
        return r;
    }

    public static double[][] transpuesta(double[][] mat) {
        int n = mat.length, m = mat[0].length;
        double[][] r = new double[m][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                r[j][i] = mat[i][j];
        return r;
    }

    public static double determinante(double[][] mat) {
        int n = mat.length;
        if (n != mat[0].length)
            throw new IllegalArgumentException("No es cuadrada");
        if (n == 1)
            return mat[0][0];
        if (n == 2)
            return mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0];
        double det = 0;
        for (int k = 0; k < n; k++) {
            double[][] sub = new double[n - 1][n - 1];
            for (int i = 1; i < n; i++)
                for (int j = 0, col = 0; j < n; j++)
                    if (j != k)
                        sub[i - 1][col++] = mat[i][j];
            det += Math.pow(-1, k) * mat[0][k] * determinante(sub);
        }
        return det;
    }

    public static double[][] inversa(double[][] mat) {
        int n = mat.length;
        double det = determinante(mat);
        if (det == 0)
            throw new IllegalArgumentException("No invertible");
        double[][] adj = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                double[][] sub = new double[n - 1][n - 1];
                for (int r = 0, rr = 0; r < n; r++) {
                    if (r == i)
                        continue;
                    for (int c = 0, cc = 0; c < n; c++) {
                        if (c == j)
                            continue;
                        sub[rr][cc++] = mat[r][c];
                    }
                    rr++;
                }
                adj[j][i] = Math.pow(-1, i + j) * determinante(sub);
            }
        return escalarPorMatriz(1.0 / det, adj);
    }

    public static double[][] dividir(double[][] a, double[][] b) {
        return multiplicar(a, inversa(b));
    }
}
