package br.unicamp.ic.mc322.telas;

import br.unicamp.ic.mc322.service.AgendamentoService;
import br.unicamp.ic.mc322.service.AgendaService;
import br.unicamp.ic.mc322.service.AuthService;
import br.unicamp.ic.mc322.service.BuscaService;
import br.unicamp.ic.mc322.service.MedicoService;
import br.unicamp.ic.mc322.service.PacienteService;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Navegacao {

    private static Stage stage;

    private static final AuthService AUTH = new AuthService();
    private static final BuscaService BUSCA = new BuscaService();
    private static final MedicoService MEDICO = new MedicoService();
    private static final PacienteService PACIENTE = new PacienteService();
    private static final AgendaService AGENDA = new AgendaService();
    private static final AgendamentoService AGENDAMENTO = new AgendamentoService();

    private Navegacao() {
    }

    public static void iniciar(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Agendamento de Consultas e Exames");
        irPara(new TelaInicial().criar());
    }

    public static void irPara(Parent root) {
        stage.setScene(new Scene(root, 1000, 720));
        stage.show();
    }

    public static AuthService auth() {
        return AUTH;
    }

    public static BuscaService busca() {
        return BUSCA;
    }

    public static MedicoService medicoService() {
        return MEDICO;
    }

    public static PacienteService pacienteService() {
        return PACIENTE;
    }

    public static AgendaService agendaService() {
        return AGENDA;
    }

    public static AgendamentoService agendamentoService() {
        return AGENDAMENTO;
    }
}
