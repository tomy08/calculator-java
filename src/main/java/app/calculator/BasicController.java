package app.calculator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

import javafx.event.ActionEvent;

import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class BasicController {

    @FXML
    private TextField pantalla;

    private double ans = 0;

    private int pos;
    private String input;

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

    @FXML
    private void initialize() {
        pantalla.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleIgual();
                event.consume();
            }
        });
    }

    @FXML
    private void handleBorrar() {
        String texto = pantalla.getText();
        if (!texto.isEmpty()) {
            pantalla.setText(texto.substring(0, texto.length() - 1));
            pantalla.positionCaret(pantalla.getText().length());
        }
    }

    @FXML
    private void handleBoton(ActionEvent event) {
        String texto = ((Button) event.getSource()).getText();

        if (texto.equals("√")) {
            insertarRaizConParentesis();
        } else {
            pantalla.appendText(texto);
        }
    }

    @FXML
    private void handleClear() {
        pantalla.clear();
    }

    @FXML
    private void handleAns() {
        pantalla.appendText(String.valueOf(ans));
    }

    @FXML
    private void handleIgual() {
        try {
            String expr = pantalla.getText();
            expr = reemplazarRaices(expr);
            double resultado = evaluar(expr);
            ans = resultado;
            pantalla.setText(String.valueOf(resultado));
            pantalla.positionCaret(pantalla.getText().length());
        } catch (Exception e) {
            pantalla.setText("Error");
        }
    }

    private void insertarRaizConParentesis() {
        pantalla.appendText("√(");
        pantalla.requestFocus();
        pantalla.positionCaret(pantalla.getText().length() - 1);
    }

    /**
     * Convierte expresiones con raíz (√) en potencias con exponentes fraccionales.
     * Por ejemplo: 3√(8) => (8)^(1/3)
     * Si no hay índice antes de la raíz, se asume raíz cuadrada.
     */
    private String reemplazarRaices(String expr) {
        StringBuilder nueva = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '√') {
                // Buscar índice n antes de la raíz (n√)
                int j = i - 1;
                StringBuilder sbNum = new StringBuilder();
                while (j >= 0 && (Character.isDigit(expr.charAt(j)) || expr.charAt(j) == '.')) {
                    sbNum.insert(0, expr.charAt(j));
                    j--;
                }
                double n;
                if (sbNum.length() == 0) {
                    n = 2; // raíz cuadrada por defecto
                } else {
                    n = Double.parseDouble(sbNum.toString());
                    // Borrar ese número que ya fue tomado (n) para no duplicar
                    nueva.delete(nueva.length() - sbNum.length(), nueva.length());
                }

                // Obtener contenido dentro de paréntesis tras la raíz
                int k = i + 1;
                if (k < expr.length() && expr.charAt(k) == '(') {
                    int count = 1;
                    k++;
                    int start = k;
                    while (k < expr.length() && count > 0) {
                        if (expr.charAt(k) == '(')
                            count++;
                        else if (expr.charAt(k) == ')')
                            count--;
                        k++;
                    }
                    String inside = expr.substring(start, k - 1);
                    nueva.append("(").append(inside).append(")^(1/").append(n).append(")");
                    i = k - 1;
                } else {
                    // Si no hay paréntesis, tomar el siguiente número simple
                    StringBuilder sbX = new StringBuilder();
                    while (k < expr.length() && (Character.isDigit(expr.charAt(k)) || expr.charAt(k) == '.')) {
                        sbX.append(expr.charAt(k));
                        k++;
                    }
                    nueva.append("(").append(sbX).append(")^(1/").append(n).append(")");
                    i = k - 1;
                }
            } else {
                nueva.append(c);
            }
        }
        return nueva.toString();
    }

    private double evaluar(String expr) {
        this.input = expr.replace(" ", "").replace("Ans", String.valueOf(ans));
        this.pos = 0;
        return parseExpression();
    }

    private double parseExpression() {
        double x = parseTerm();
        while (pos < input.length()) {
            char op = input.charAt(pos);
            if (op == '+' || op == '-') {
                pos++;
                double y = parseTerm();
                if (op == '+')
                    x += y;
                else
                    x -= y;
            } else {
                break;
            }
        }
        return x;
    }

    private double parseTerm() {
        double x = parseFactor();
        while (pos < input.length()) {
            char op = input.charAt(pos);
            if (op == '*' || op == '/') {
                pos++;
                double y = parseFactor();
                if (op == '*')
                    x *= y;
                else {
                    if (y == 0)
                        throw new ArithmeticException("División por cero");
                    x /= y;
                }
            } else {
                break;
            }
        }
        return x;
    }

    private double parseFactor() {
        return parsePower();
    }

    private double parsePower() {
        double x = parseUnary();
        while (pos < input.length() && input.charAt(pos) == '^') {
            pos++;
            double y = parseUnary();
            x = Math.pow(x, y);
        }
        return x;
    }

    private double parseUnary() {
        if (pos < input.length()) {
            char c = input.charAt(pos);
            if (c == '+') {
                pos++;
                return parseUnary();
            }
            if (c == '-') {
                pos++;
                return -parseUnary();
            }
        }
        return parsePrimary();
    }

    private double parsePrimary() {
        if (pos >= input.length())
            throw new RuntimeException("Expresión incompleta");

        char c = input.charAt(pos);

        if (c == '(') {
            pos++;
            double x = parseExpression();
            if (pos >= input.length() || input.charAt(pos) != ')')
                throw new RuntimeException("Paréntesis no cerrado");
            pos++;
            return x;
        }

        StringBuilder sb = new StringBuilder();
        boolean decimalFound = false;
        while (pos < input.length() && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
            if (input.charAt(pos) == '.') {
                if (decimalFound)
                    break;
                decimalFound = true;
            }
            sb.append(input.charAt(pos++));
        }

        if (sb.length() == 0) {
            throw new RuntimeException("Número esperado en posición " + pos);
        }

        return Double.parseDouble(sb.toString());
    }

}
