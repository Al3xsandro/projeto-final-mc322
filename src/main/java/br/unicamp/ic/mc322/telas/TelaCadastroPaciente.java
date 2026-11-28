package br.unicamp.ic.mc322.telas;

import br.unicamp.ic.mc322.model.Paciente;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class TelaCadastroPaciente {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Parent criar() {
        Label titulo = new Label("Cadastro de Paciente");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 22));

        TextField nome = campo("Nome completo");
        TextField email = campo("E-mail");
        PasswordField senha = new PasswordField();
        senha.setPromptText("Senha");
        senha.setMaxWidth(380);
        TextField telefone = campo("Telefone");
        DatePicker dataNascimento = calendario("Data de nascimento");
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
                if (dataNascimento.getValue() == null) {
                    throw new IllegalArgumentException("Informe a data de nascimento.");
                }
                Paciente paciente = new Paciente(
                        UUID.randomUUID().toString(),
                        nome.getText().trim(),
                        email.getText().trim(),
                        senha.getText(),
                        telefone.getText().trim(),
                        LocalDate.now(),
                        dataNascimento.getValue(),
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

    private static DatePicker calendario(String prompt) {
        DatePicker calendario = new DatePicker();
        calendario.setPromptText("dd/mm/aaaa");
        calendario.setMaxWidth(380);
        calendario.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate valor) {
                return valor == null ? "" : valor.format(FORMATO_DATA);
            }

            @Override
            public LocalDate fromString(String texto) {
                try {
                    return texto == null || texto.isBlank() ? null : LocalDate.parse(texto.trim(), FORMATO_DATA);
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
        });
        return calendario;
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
