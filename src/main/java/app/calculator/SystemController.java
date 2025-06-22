package app.calculator;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SystemController {

    @FXML
    private TextField a11_2, a12_2, b1_2;
    @FXML
    private TextField a21_2, a22_2, b2_2;

    @FXML
    private TextField a11_3, a12_3, a13_3, b1_3;
    @FXML
    private TextField a21_3, a22_3, a23_3, b2_3;
    @FXML
    private TextField a31_3, a32_3, a33_3, b3_3;

    @FXML
    private TextArea resultado;

    private double parseField(TextField tf) throws NumberFormatException {
        return Double.parseDouble(tf.getText().trim());
    }

    @FXML
    private void handleResolver2x2() {
        try {
            double[][] A = {
                    { parseField(a11_2), parseField(a12_2) },
                    { parseField(a21_2), parseField(a22_2) }
            };
            double[][] B = {
                    { parseField(b1_2) },
                    { parseField(b2_2) }
            };

            double det = MatrixUtils.determinante(A);
            if (det == 0) {
                resultado.setText("El sistema 2x2 no tiene solución única (determinante=0).");
                return;
            }
            double[][] invA = MatrixUtils.inversa(A);
            double[][] X = MatrixUtils.multiplicar(invA, B);

            resultado.setText(String.format("Solución 2x2:\nx = %.4f\ny = %.4f", X[0][0], X[1][0]));

        } catch (NumberFormatException e) {
            resultado.setText("Error: Ingrese todos los coeficientes numéricos.");
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleResolver3x3() {
        try {
            double[][] A = {
                    { parseField(a11_3), parseField(a12_3), parseField(a13_3) },
                    { parseField(a21_3), parseField(a22_3), parseField(a23_3) },
                    { parseField(a31_3), parseField(a32_3), parseField(a33_3) }
            };
            double[][] B = {
                    { parseField(b1_3) },
                    { parseField(b2_3) },
                    { parseField(b3_3) }
            };

            double det = MatrixUtils.determinante(A);
            if (det == 0) {
                resultado.setText("El sistema 3x3 no tiene solución única (determinante=0).");
                return;
            }
            double[][] invA = MatrixUtils.inversa(A);
            double[][] X = MatrixUtils.multiplicar(invA, B);

            resultado.setText(String.format("Solución 3x3:\nx = %.4f\ny = %.4f\nz = %.4f", X[0][0], X[1][0], X[2][0]));

        } catch (NumberFormatException e) {
            resultado.setText("Error: Ingrese todos los coeficientes numéricos.");
        } catch (Exception e) {
            resultado.setText("Error: " + e.getMessage());
        }
    }
}
