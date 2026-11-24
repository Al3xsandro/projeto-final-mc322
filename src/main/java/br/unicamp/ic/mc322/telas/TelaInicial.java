package br.unicamp.ic.mc322.telas;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TelaInicial {

    public Parent criar() {
        Label titulo = new Label("MEDAGENDA");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 26));
        Label subtitulo = new Label("Escolha uma opção para continuar");

        Button loginMedico = botao("Portal do Médico");
        loginMedico.setOnAction(e -> Navegacao.irPara(new TelaLoginMedico().criar()));

        Button loginPaciente = botao("Portal do Paciente");
        loginPaciente.setOnAction(e -> Navegacao.irPara(new TelaLoginPaciente().criar()));

        Button cadastroMedico = botao("Cadastrar Médico");
        cadastroMedico.setOnAction(e -> Navegacao.irPara(new TelaCadastroMedico().criar()));

        Button cadastroPaciente = botao("Cadastrar Paciente");
        cadastroPaciente.setOnAction(e -> Navegacao.irPara(new TelaCadastroPaciente().criar()));

        VBox root = new VBox(12, titulo, subtitulo, loginMedico, loginPaciente, cadastroMedico, cadastroPaciente);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #eef2f7;");
        return root;
    }

    private Button botao(String texto) {
        Button botao = new Button(texto);
        botao.setPrefWidth(260);
        botao.setStyle("-fx-font-size: 14px; -fx-background-color: #1f6feb; -fx-text-fill: white; -fx-background-radius: 6;");
        return botao;
    }
}
