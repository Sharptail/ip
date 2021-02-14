package snom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import snom.model.Snom;
import snom.ui.MainWindow;

import java.io.IOException;

/**
 * Runs the Application.
 */
public class Main extends Application {

    private Snom snom = new Snom("data/snom.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setResizable(false);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setSnom(snom);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}