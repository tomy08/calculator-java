package app.calculator;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MatrixController {

    @FXML
    private TextArea matrizA;
    @FXML
    private TextArea matrizB;
    @FXML
    private TextField escalar;
    @FXML
    private TextArea resultado;

    @FXML
    private void volverAlMenu(ActionEvent event) throws IOException {
        loadView(event, "MainMenu.fxml", "Volver al Menú Principal");
    }

    private void loadView(ActionEvent event, String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }

    // Parsea una matriz desde un string tipo "1,2;3,4"
    private double[][] parseMatrix(String input) {
        String[] rows = input.trim().split(";");
        int n = rows.length;
        int m = rows[0].split(",").length;
        double[][] matrix = new double[n][m];
        for (int i = 0; i < n; i++) {
            String[] cols = rows[i].split(",");
            if (cols.length != m)
                throw new IllegalArgumentException("Matriz mal formada");
            for (int j = 0; j < m; j++) {
                matrix[i][j] = Double.parseDouble(cols[j]);
            }
        }
        return matrix;
    }

    // Convierte matriz a string para mostrar
    private String matrixToString(double[][] mat) {
        StringBuilder sb = new StringBuilder();
        for (double[] row : mat) {
            for (double val : row) {
                sb.append(val).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private double getEscalar() {
        if (escalar.getText() == null || escalar.getText().isEmpty())
            return 1.0;
        return Double.parseDouble(escalar.getText());
    }

    @FXML
    private void handleSuma() {
        try {
            double[][] a = parseMatrix(matrizA.getText());
            double[][] b = parseMatrix(matrizB.getText());
            double[][] r = MatrixUtils.suma(a, b);
            resultado.setText(matrixToString(r));
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleResta() {
        try {
            double[][] a = parseMatrix(matrizA.getText());
            double[][] b = parseMatrix(matrizB.getText());
            double[][] r = MatrixUtils.resta(a, b);
            resultado.setText(matrixToString(r));
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleEscalarA() {
        try {
            double[][] a = parseMatrix(matrizA.getText());
            double esc = getEscalar();
            double[][] r = MatrixUtils.escalarPorMatriz(esc, a);
            resultado.setText(matrixToString(r));
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleEscalarB() {
        try {
            double[][] b = parseMatrix(matrizB.getText());
            double esc = getEscalar();
            double[][] r = MatrixUtils.escalarPorMatriz(esc, b);
            resultado.setText(matrixToString(r));
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleMultiplicar() {
        try {
            double[][] a = parseMatrix(matrizA.getText());
            double[][] b = parseMatrix(matrizB.getText());
            double[][] r = MatrixUtils.multiplicar(a, b);
            resultado.setText(matrixToString(r));
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleDividir() {
        try {
            double[][] a = parseMatrix(matrizA.getText());
            double[][] b = parseMatrix(matrizB.getText());
            double[][] r = MatrixUtils.dividir(a, b);
            resultado.setText(matrixToString(r));
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeterminanteA() {
        try {
            double[][] a = parseMatrix(matrizA.getText());
            double det = MatrixUtils.determinante(a);
            resultado.setText(String.valueOf(det));
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleTranspuestaA() {
        try {
            double[][] a = parseMatrix(matrizA.getText());
            double[][] r = MatrixUtils.transpuesta(a);
            resultado.setText(matrixToString(r));
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleInversaA() {
        try {
            double[][] a = parseMatrix(matrizA.getText());
            double[][] r = MatrixUtils.inversa(a);
            resultado.setText(matrixToString(r));
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }
}
