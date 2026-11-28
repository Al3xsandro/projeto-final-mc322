package br.unicamp.ic.mc322.service;

import br.unicamp.ic.mc322.TesteComDados;
import br.unicamp.ic.mc322.exception.AgendamentoException;
import br.unicamp.ic.mc322.model.Agendamento;
import br.unicamp.ic.mc322.model.Exame;
import br.unicamp.ic.mc322.model.Medico;
import br.unicamp.ic.mc322.model.Paciente;
import br.unicamp.ic.mc322.model.enums.StatusAgendamento;
import br.unicamp.ic.mc322.model.enums.TipoAgendamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AgendamentoServiceTest extends TesteComDados {

    private final AuthService authService = new AuthService();
    private final AgendamentoService agendamentoService = new AgendamentoService();

    private Paciente paciente;
    private Medico medico;

    @BeforeEach
    void prepararUsuarios() {
        String id = UUID.randomUUID().toString();

        Paciente novoPaciente = new Paciente(null, "Paciente " + id, "pac-" + id + "@teste.com",
                "senha", null, null, LocalDate.of(1990, 1, 1), "cpf-" + id);
        Medico novoMedico = new Medico(null, "Medico " + id, "med-" + id + "@teste.com",
                "senha", null, null, "CRM-" + id, null);

        authService.cadastrarPaciente(novoPaciente);
        authService.cadastrarMedico(novoMedico);

        paciente = authService.autenticarPaciente(novoPaciente.getEmail(), "senha");
        medico = authService.autenticarMedico(novoMedico.getCrm(), "senha");
    }

    @Test
    void solicitaConsultaEPendenteParaOMedico() {
        Agendamento agendamento = agendamentoService.solicitarConsulta(paciente, medico, amanha());

        assertEquals(StatusAgendamento.SOLICITADO, agendamento.getStatus());
        assertTrue(contem(agendamentoService.listarSolicitacoesPendentes(medico), agendamento));
        assertTrue(contem(agendamentoService.listarProximosAgendamentos(paciente), agendamento));
    }

    @Test
    void confirmaAgendamento() {
        Agendamento agendamento = agendamentoService.solicitarConsulta(paciente, medico, amanha());

        agendamentoService.confirmarAgendamento(agendamento.getId());

        Agendamento confirmado = buscar(agendamentoService.listarProximosAgendamentos(paciente), agendamento);
        assertEquals(StatusAgendamento.CONFIRMADO, confirmado.getStatus());
        assertFalse(contem(agendamentoService.listarSolicitacoesPendentes(medico), agendamento));
    }

    @Test
    void cancelaAgendamentoPeloPaciente() {
        Agendamento agendamento = agendamentoService.solicitarConsulta(paciente, medico, amanha());

        agendamentoService.cancelarPeloPaciente(agendamento.getId());

        assertFalse(contem(agendamentoService.listarProximosAgendamentos(paciente), agendamento));
    }

    @Test
    void solicitaExameComDadosDoExame() {
        Exame exame = new Exame("exame-" + UUID.randomUUID(), "Hemograma", "Exame de sangue");

        Agendamento agendamento = agendamentoService.solicitarExame(paciente, medico, exame, amanha());

        assertEquals(TipoAgendamento.EXAME, agendamento.getTipo());
        Agendamento salvo = buscar(agendamentoService.listarSolicitacoesPendentes(medico), agendamento);
        assertNotNull(salvo.getExame());
        assertEquals("Hemograma", salvo.getExame().getNome());
    }

    @Test
    void rejeitaAgendamentoSemData() {
        assertThrows(AgendamentoException.class,
                () -> agendamentoService.solicitarConsulta(paciente, medico, null));
    }

    private static LocalDateTime amanha() {
        return LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);
    }

    private static boolean contem(java.util.List<Agendamento> agendamentos, Agendamento alvo) {
        return agendamentos.stream().anyMatch(a -> a.getId().equals(alvo.getId()));
    }

    private static Agendamento buscar(java.util.List<Agendamento> agendamentos, Agendamento alvo) {
        return agendamentos.stream()
                .filter(a -> a.getId().equals(alvo.getId()))
                .findFirst()
                .orElseThrow();
    }
}
