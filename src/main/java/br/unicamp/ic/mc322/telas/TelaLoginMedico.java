package br.unicamp.ic.mc322.telas;

import br.unicamp.ic.mc322.model.Medico;
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

public class TelaLoginMedico {

    public Parent criar() {
        Label titulo = new Label("Portal do Médico");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 22));

        TextField crm = new TextField();
        crm.setPromptText("CRM");
        crm.setMaxWidth(300);
        PasswordField senha = new PasswordField();
        senha.setPromptText("Senha");
        senha.setMaxWidth(300);

        Label erro = new Label();
        erro.setStyle("-fx-text-fill: #b00020;");

        Button entrar = new Button("Entrar");
        entrar.setPrefWidth(300);
        entrar.setStyle("-fx-background-color: #1f6feb; -fx-text-fill: white; -fx-background-radius: 6;");
        entrar.setOnAction(e -> {
            try {
                Medico medico = Navegacao.auth().autenticarMedico(crm.getText().trim(), senha.getText());
                Navegacao.irPara(new PainelMedico(medico).criar());
            } catch (RuntimeException ex) {
                erro.setText(ex.getMessage());
            }
        });

        Button voltar = new Button("Voltar");
        voltar.setOnAction(e -> Navegacao.irPara(new TelaInicial().criar()));

        VBox root = new VBox(10, titulo, crm, senha, erro, entrar, voltar);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #eef2f7;");
        return root;
    }
}
