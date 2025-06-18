package app.calculator;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import Jama.Matrix;

public class BasicController {

    @FXML
    private TextField pantalla;

    private double ans = 0;
    private int pos;
    private String input;

    @FXML
    private void initialize() {
        pantalla.setOnKeyTyped(event -> {
            if (event.getCharacter().equals(",")) {
                event.consume();
                pantalla.appendText(".");
            }
        });
    }

    @FXML
    private void handleBoton(ActionEvent event) {
        String textoBoton = ((javafx.scene.control.Button) event.getSource()).getText();
        pantalla.appendText(textoBoton);
    }

    @FXML
    private void handleClear() {
        pantalla.clear();
    }

    @FXML
    private void handleIgual() {
        String expr = pantalla.getText().replace(",", ".").trim();

        try {
            if (expr.contains("INV")) {
                String matrizStr = extraerMatriz(expr);
                double[][] matriz = parsearMatriz(matrizStr);
                double[][] inversa = invertirMatriz(matriz);
                pantalla.setText(matrizToString(inversa));
                return;

            } else if (expr.contains("DET")) {
                String matrizStr = extraerMatriz(expr);
                double[][] matriz = parsearMatriz(matrizStr);
                double determinante = calcularDeterminante(matriz);
                pantalla.setText(String.valueOf(determinante));
                return;
            }

            // Evaluación matemática normal (usá tu parser si tenés uno)
            double resultado = eval(expr);
            pantalla.setText(String.valueOf(resultado));

        } catch (Exception e) {
            pantalla.setText("ERROR");
            e.printStackTrace();
        }
    }

    private String extraerMatriz(String expr) {
        int start = expr.indexOf("{");
        int end = expr.lastIndexOf("}");
        if (start == -1 || end == -1 || end <= start) throw new RuntimeException("Matriz mal formada");
        return expr.substring(start, end + 1);
    }

    private double[][] parsearMatriz(String texto) {
        texto = texto.replace("{", "").replace("}", "").trim();
        String[] filas = texto.split(";");
        double[][] matriz = new double[filas.length][];
        for (int i = 0; i < filas.length; i++) {
            String[] nums = filas[i].trim().split("\\s+");
            matriz[i] = new double[nums.length];
            for (int j = 0; j < nums.length; j++) {
                matriz[i][j] = Double.parseDouble(nums[j]);
            }
        }
        return matriz;
    }

    private double[][] invertirMatriz(double[][] matriz) {
        int n = matriz.length;
        if (n != matriz[0].length) throw new RuntimeException("Matriz no cuadrada");
        Matrix m = new Matrix(matriz);
        Matrix inv = m.inverse();
        return inv.getArray();
    }

    private double calcularDeterminante(double[][] matriz) {
        Matrix m = new Matrix(matriz);
        return m.det();
    }

    private String matrizToString(double[][] matriz) {
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                sb.append(String.format("%.2f", matriz[i][j]));
                if (j < matriz[i].length - 1) sb.append(" ");
            }
            if (i < matriz.length - 1) sb.append("; ");
        }
        sb.append("}");
        return sb.toString();
    }


    private double eval(String expr) {
        // Evaluación muy básica (solo suma/resta) – reemplazalo con un parser si tenés
        return new javax.script.ScriptEngineManager()
                .getEngineByName("JavaScript")
                .eval(expr) instanceof Number
                ? ((Number) new javax.script.ScriptEngineManager()
                .getEngineByName("JavaScript")
                .eval(expr)).doubleValue()
                : 0;
    }
}
