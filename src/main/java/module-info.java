module app.calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.scripting;

    opens app.calculator to javafx.fxml;
    exports app.calculator;
}