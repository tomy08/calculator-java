module app.calculator {
    requires javafx.controls;
    requires javafx.fxml;


    opens app.calculator to javafx.fxml;
    exports app.calculator;
}