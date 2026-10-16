package com.tuempresa.papeleriacampus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ProductoApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("producto-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 560);
        stage.setTitle("Bienvenido a Papeleria Campus");
        stage.setScene(scene);
        stage.show();
    }
}
