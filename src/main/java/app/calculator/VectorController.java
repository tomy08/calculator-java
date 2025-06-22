package app.calculator;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class VectorController {

    @FXML
    private TextField vectorA;

    @FXML
    private TextField vectorB;

    @FXML
    private TextField escalar;

    @FXML
    private TextField resultado;

    @FXML
    private void handleSuma() {
        try {
            double[] a = parseVector(vectorA.getText());
            double[] b = parseVector(vectorB.getText());
            double[] r = suma(a, b);
            resultado.setText(formatVector(r));
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleResta() {
        try {
            double[] a = parseVector(vectorA.getText());
            double[] b = parseVector(vectorB.getText());
            double[] r = resta(a, b);
            resultado.setText(formatVector(r));
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleEscalarA() {
        try {
            double esc = Double.parseDouble(escalar.getText());
            double[] a = parseVector(vectorA.getText());
            double[] r = escalarPorVector(esc, a);
            resultado.setText(formatVector(r));
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleEscalarB() {
        try {
            double esc = Double.parseDouble(escalar.getText());
            double[] b = parseVector(vectorB.getText());
            double[] r = escalarPorVector(esc, b);
            resultado.setText(formatVector(r));
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleProductoEscalar() {
        try {
            double[] a = parseVector(vectorA.getText());
            double[] b = parseVector(vectorB.getText());
            double r = productoEscalar(a, b);
            resultado.setText(String.valueOf(r));
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleProductoVectorial() {
        try {
            double[] a = parseVector(vectorA.getText());
            double[] b = parseVector(vectorB.getText());
            double[] r = productoVectorial(a, b);
            resultado.setText(formatVector(r));
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }

    // Métodos auxiliares

    private double[] parseVector(String texto) {
        String[] partes = texto.trim().split(",");
        double[] vector = new double[partes.length];
        for (int i = 0; i < partes.length; i++) {
            vector[i] = Double.parseDouble(partes[i].trim());
        }
        return vector;
    }

    private String formatVector(double[] v) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v.length; i++) {
            sb.append(v[i]);
            if (i < v.length - 1)
                sb.append(", ");
        }
        return sb.toString();
    }

    public static double[] suma(double[] a, double[] b) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Dimensiones distintas");
        double[] r = new double[a.length];
        for (int i = 0; i < a.length; i++)
            r[i] = a[i] + b[i];
        return r;
    }

    public static double[] resta(double[] a, double[] b) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Dimensiones distintas");
        double[] r = new double[a.length];
        for (int i = 0; i < a.length; i++)
            r[i] = a[i] - b[i];
        return r;
    }

    public static double[] escalarPorVector(double escalar, double[] v) {
        double[] r = new double[v.length];
        for (int i = 0; i < v.length; i++)
            r[i] = escalar * v[i];
        return r;
    }

    public static double productoEscalar(double[] a, double[] b) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Dimensiones distintas");
        double r = 0;
        for (int i = 0; i < a.length; i++)
            r += a[i] * b[i];
        return r;
    }

    public static double[] productoVectorial(double[] a, double[] b) {
        if (a.length != 3 || b.length != 3)
            throw new IllegalArgumentException("Solo 3D");
        return new double[] {
                a[1] * b[2] - a[2] * b[1],
                a[2] * b[0] - a[0] * b[2],
                a[0] * b[1] - a[1] * b[0]
        };
    }
}
