package br.unicamp.ic.mc322.telas;

import br.unicamp.ic.mc322.model.Especialidade;
import br.unicamp.ic.mc322.model.Exame;
import br.unicamp.ic.mc322.model.Medico;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.util.UUID;

public class TelaCadastroMedico {

    public Parent criar() {
        Label titulo = new Label("Cadastro de Médico");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 22));

        TextField nome = campo("Nome completo");
        TextField email = campo("E-mail");
        PasswordField senha = new PasswordField();
        senha.setPromptText("Senha");
        senha.setMaxWidth(380);
        TextField telefone = campo("Telefone");
        TextField crm = campo("CRM");
        TextArea biografia = new TextArea();
        biografia.setPromptText("Biografia");
        biografia.setPrefRowCount(3);
        biografia.setMaxWidth(380);
        TextField especialidades = campo("Especialidades separadas por vírgula");
        TextField exames = campo("Exames oferecidos separados por vírgula");

        Label erro = new Label();
        erro.setStyle("-fx-text-fill: #b00020;");

        Button cadastrar = new Button("Cadastrar");
        cadastrar.setPrefWidth(380);
        cadastrar.setStyle("-fx-background-color: #1f6feb; -fx-text-fill: white; -fx-background-radius: 6;");
        cadastrar.setOnAction(e -> {
            try {
                if (vazio(nome, email, senha, crm)) {
                    throw new IllegalArgumentException("Preencha nome, e-mail, senha e CRM.");
                }
                Medico medico = new Medico(
                        UUID.randomUUID().toString(),
                        nome.getText().trim(),
                        email.getText().trim(),
                        senha.getText(),
                        telefone.getText().trim(),
                        LocalDate.now(),
                        crm.getText().trim(),
                        biografia.getText().trim());
                adicionarEspecialidades(medico, especialidades.getText());
                adicionarExames(medico, exames.getText());
                Navegacao.auth().cadastrarMedico(medico);
                Navegacao.irPara(new TelaLoginMedico().criar());
            } catch (RuntimeException ex) {
                erro.setText(ex.getMessage());
            }
        });

        Button voltar = new Button("Voltar");
        voltar.setOnAction(e -> Navegacao.irPara(new TelaInicial().criar()));

        VBox root = new VBox(8, titulo, nome, email, senha, telefone, crm, biografia,
                especialidades, exames, erro, cadastrar, voltar);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color: #eef2f7;");
        return root;
    }

    private static void adicionarEspecialidades(Medico medico, String texto) {
        for (String nome : texto.split(",")) {
            String limpo = nome.trim();
            if (!limpo.isEmpty()) {
                medico.getEspecialidades().add(new Especialidade(UUID.randomUUID().toString(), limpo, null));
            }
        }
    }

    private static void adicionarExames(Medico medico, String texto) {
        for (String nome : texto.split(",")) {
            String limpo = nome.trim();
            if (!limpo.isEmpty()) {
                medico.getExamesOferecidos().add(new Exame(UUID.randomUUID().toString(), limpo, null));
            }
        }
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
