package br.unicamp.ic.mc322.telas;

import br.unicamp.ic.mc322.model.Paciente;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.util.UUID;

public class TelaCadastroPaciente {

    public Parent criar() {
        Label titulo = new Label("Cadastro de Paciente");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 22));

        TextField nome = campo("Nome completo");
        TextField email = campo("E-mail");
        PasswordField senha = new PasswordField();
        senha.setPromptText("Senha");
        senha.setMaxWidth(380);
        TextField telefone = campo("Telefone");
        TextField dataNascimento = campo("Data de nascimento (AAAA-MM-DD)");
        TextField cpf = campo("CPF");

        Label erro = new Label();
        erro.setStyle("-fx-text-fill: #b00020;");

        Button cadastrar = new Button("Cadastrar");
        cadastrar.setPrefWidth(380);
        cadastrar.setStyle("-fx-background-color: #1f6feb; -fx-text-fill: white; -fx-background-radius: 6;");
        cadastrar.setOnAction(e -> {
            try {
                if (vazio(nome, email, senha, cpf)) {
                    throw new IllegalArgumentException("Preencha nome, e-mail, senha e CPF.");
                }
                if (dataNascimento.getText().isBlank()) {
                    throw new IllegalArgumentException("Informe a data de nascimento.");
                }
                LocalDate nascimento;
                try {
                    nascimento = LocalDate.parse(dataNascimento.getText().trim());
                } catch (RuntimeException ex) {
                    throw new IllegalArgumentException("Data inválida. Use o formato AAAA-MM-DD.");
                }
                Paciente paciente = new Paciente(
                        UUID.randomUUID().toString(),
                        nome.getText().trim(),
                        email.getText().trim(),
                        senha.getText(),
                        telefone.getText().trim(),
                        LocalDate.now(),
                        nascimento,
                        cpf.getText().trim());
                Navegacao.auth().cadastrarPaciente(paciente);
                Navegacao.irPara(new TelaLoginPaciente().criar());
            } catch (RuntimeException ex) {
                erro.setText(ex.getMessage());
            }
        });

        Button voltar = new Button("Voltar");
        voltar.setOnAction(e -> Navegacao.irPara(new TelaInicial().criar()));

        VBox root = new VBox(8, titulo, nome, email, senha, telefone, dataNascimento, cpf,
                erro, cadastrar, voltar);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color: #eef2f7;");
        return root;
    }

    private static TextField campo(String prompt) {
        TextField campo = new TextField();
        campo.setPromptText(prompt);
        campo.setMaxWidth(380);
        return campo;
    }

    private static boolean vazio(TextField... campos) {
        for (TextField campo : campos) {
            if (campo.getText() == null || campo.getText().isBlank()) {
                return true;
            }
        }
        return false;
    }
}
