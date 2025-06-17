package app.calculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    public void openBasic(ActionEvent event) throws IOException {
        loadView(event, "/resources/Basic.fxml", "Basic Operations");
    }

    public void openVectors(ActionEvent event) throws IOException {
        loadView(event, "/resources/Vectors.fxml", "Vector Operations");
    }

    public void openMatrices(ActionEvent event) throws IOException {
        loadView(event, "/resources/Matrices.fxml", "Matrix Operations");
    }

    public void openSystems(ActionEvent event) throws IOException {
        loadView(event, "/resources/Systems.fxml", "Equation Systems");
    }

    public void exitApplication(ActionEvent event) {
        System.exit(0);
    }

    private void loadView(ActionEvent event, String fxmlPath, String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
