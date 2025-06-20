package app.calculator;

import Jama.Matrix;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class BasicController {

    @FXML
    private TextField pantalla;

    private double ans = 0;
    private int pos;
    private String input;

    @FXML
    private void handleIgual() {
        try {
            String expr = pantalla.getText().trim();

            // === OPERACIONES CON MATRICES ===
            if (expr.startsWith("INV")) {
                Matrix m = parseMatrix(expr.substring(3));
                pantalla.setText(matrixToString(m.inverse()));
                return;
            } else if (expr.startsWith("DET")) {
                Matrix m = parseMatrix(expr.substring(3));
                pantalla.setText(String.valueOf(m.det()));
                return;
            } else if (expr.startsWith("T")) {
                Matrix m = parseMatrix(expr.substring(1));
                pantalla.setText(matrixToString(m.transpose()));
                return;
            } else if (expr.contains("+") && expr.contains("{")) {
                String[] partes = expr.split("\\+");
                Matrix m1 = parseMatrix(partes[0]);
                Matrix m2 = parseMatrix(partes[1]);
                pantalla.setText(matrixToString(m1.plus(m2)));
                return;
            } else if (expr.contains("-") && expr.contains("{")) {
                String[] partes = expr.split("-");
                Matrix m1 = parseMatrix(partes[0]);
                Matrix m2 = parseMatrix(partes[1]);
                pantalla.setText(matrixToString(m1.minus(m2)));
                return;
            } else if (expr.contains("*") && expr.contains("{")) {
                String[] partes = expr.split("\\*");
                if (!partes[0].contains("{")) {
                    double escalar = Double.parseDouble(partes[0]);
                    Matrix m = parseMatrix(partes[1]);
                    pantalla.setText(matrixToString(m.times(escalar)));
                } else {
                    Matrix m1 = parseMatrix(partes[0]);
                    Matrix m2 = parseMatrix(partes[1]);
                    pantalla.setText(matrixToString(m1.times(m2)));
                }
                return;
            } else if (expr.contains("/") && expr.contains("{")) {
                String[] partes = expr.split("/");
                Matrix m1 = parseMatrix(partes[0]);
                Matrix m2 = parseMatrix(partes[1]);
                pantalla.setText(matrixToString(m1.times(m2.inverse())));
                return;
            }

            // === OPERACIÓN ARITMÉTICA NORMAL ===
            expr = reemplazarRaices(expr);
            double resultado = evaluar(expr);
            ans = resultado;
            pantalla.setText(String.valueOf(resultado));
            pantalla.positionCaret(pantalla.getText().length());

        } catch (Exception e) {
            pantalla.setText("Error");
            e.printStackTrace();
        }
    }

    // === PASO 2: Conversión de texto a matriz ===
    private Matrix parseMatrix(String texto) {
        texto = texto.replaceAll("[{}]", ""); // Quitar llaves
        String[] filas = texto.split(";");
        double[][] valores = new double[filas.length][];
        for (int i = 0; i < filas.length; i++) {
            String[] nums = filas[i].split(",");
            valores[i] = new double[nums.length];
            for (int j = 0; j < nums.length; j++) {
                valores[i][j] = Double.parseDouble(nums[j]);
            }
        }
        return new Matrix(valores);
    }

    // === PASO 2: Conversión de matriz a texto ===
    private String matrixToString(Matrix m) {
        StringBuilder sb = new StringBuilder("{{");
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                sb.append(m.get(i, j));
                if (j < m.getColumnDimension() - 1) sb.append(",");
            }
            if (i < m.getRowDimension() - 1) sb.append(";"); // Separador de filas
        }
        sb.append("}}");
        return sb.toString();
    }

    // === Reemplaza √ por Math.sqrt(...) ===
    private String reemplazarRaices(String expr) {
        while (expr.contains("√(")) {
            int i = expr.indexOf("√(");
            int j = encontrarCierre(expr, i + 2);
            String inner = expr.substring(i + 2, j);
            double val = Math.sqrt(evaluar(inner));
            expr = expr.substring(0, i) + val + expr.substring(j + 1);
        }
        return expr;
    }

    private int encontrarCierre(String s, int i) {
        int cont = 1;
        for (int j = i; j < s.length(); j++) {
            if (s.charAt(j) == '(') cont++;
            else if (s.charAt(j) == ')') cont--;
            if (cont == 0) return j;
        }
        return -1;
    }

    // === Evaluador aritmético simple ===
    private double evaluar(String expr) {
        return new Object() {
            int pos = -1, ch;

            void sig() {
                ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
            }

            boolean comer(int c) {
                while (ch == ' ') sig();
                if (ch == c) {
                    sig();
                    return true;
                }
                return false;
            }

            double parse() {
                sig();
                double x = parseExp();
                if (pos < expr.length()) throw new RuntimeException("Inesperado: " + (char) ch);
                return x;
            }

            double parseExp() {
                double x = parseTerm();
                for (;;) {
                    if (comer('+')) x += parseTerm();
                    else if (comer('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (comer('*')) x *= parseFactor();
                    else if (comer('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (comer('+')) return parseFactor();
                if (comer('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (comer('(')) {
                    x = parseExp();
                    comer(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') sig();
                    x = Double.parseDouble(expr.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Número esperado");
                }

                return x;
            }
        }.parse();
    }
}
