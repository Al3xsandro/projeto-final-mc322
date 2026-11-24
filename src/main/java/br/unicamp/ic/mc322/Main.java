package br.unicamp.ic.mc322;

import br.unicamp.ic.mc322.telas.Navegacao;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Navegacao.iniciar(primaryStage);
    }
}
