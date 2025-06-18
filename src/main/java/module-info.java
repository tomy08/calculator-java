module app.calculator {
    requires Jama;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.scripting;

    opens app.calculator to javafx.fxml;
    exports app.calculator;
}