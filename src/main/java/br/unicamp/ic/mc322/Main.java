package br.unicamp.ic.mc322;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Teste do javafx");

        Scene scene = new Scene(label, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
