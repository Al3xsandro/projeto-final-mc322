package br.unicamp.ic.mc322.service;

import br.unicamp.ic.mc322.TesteComDados;
import br.unicamp.ic.mc322.model.HorarioDisponivel;
import br.unicamp.ic.mc322.model.Medico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AgendaServiceTest extends TesteComDados {

    private final AuthService authService = new AuthService();
    private final AgendaService agendaService = new AgendaService();

    private Medico medico;

    @BeforeEach
    void prepararMedico() {
        String id = UUID.randomUUID().toString();
        Medico novoMedico = new Medico(null, "Medico " + id, "med-" + id + "@teste.com",
                "senha", null, null, "CRM-" + id, null);
        authService.cadastrarMedico(novoMedico);
        medico = authService.autenticarMedico(novoMedico.getCrm(), "senha");
    }

    @Test
    void adicionaERemoveHorarioDisponivel() {
        HorarioDisponivel horario = new HorarioDisponivel(null, DayOfWeek.MONDAY,
                LocalTime.of(8, 0), LocalTime.of(12, 0), true);

        agendaService.adicionarHorarioDisponivel(medico, horario);

        List<HorarioDisponivel> horarios = agendaService.listarHorariosDisponiveis(medico);
        assertEquals(1, horarios.size());
        assertNotNull(horarios.get(0).getId());

        agendaService.removerHorarioDisponivel(medico, horarios.get(0).getId());

        assertTrue(agendaService.listarHorariosDisponiveis(medico).isEmpty());
    }
}
