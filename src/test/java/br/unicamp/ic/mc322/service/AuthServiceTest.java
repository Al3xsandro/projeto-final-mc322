package br.unicamp.ic.mc322.service;

import br.unicamp.ic.mc322.TesteComDados;
import br.unicamp.ic.mc322.exception.AutenticacaoException;
import br.unicamp.ic.mc322.model.Medico;
import br.unicamp.ic.mc322.model.Paciente;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthServiceTest extends TesteComDados {

    private final AuthService authService = new AuthService();

    @Test
    void cadastraEAutenticaPaciente() {
        Paciente paciente = novoPaciente(novoEmail());

        authService.cadastrarPaciente(paciente);
        Paciente autenticado = authService.autenticarPaciente(paciente.getEmail(), paciente.getSenha());

        assertEquals(paciente.getEmail(), autenticado.getEmail());
        assertNotNull(autenticado.getId());
        assertNotNull(autenticado.getDataCadastro());
    }

    @Test
    void rejeitaSenhaIncorretaDoPaciente() {
        Paciente paciente = novoPaciente(novoEmail());
        authService.cadastrarPaciente(paciente);

        assertThrows(AutenticacaoException.class,
                () -> authService.autenticarPaciente(paciente.getEmail(), "senha-errada"));
    }

    @Test
    void cadastraEAutenticaMedico() {
        Medico medico = novoMedico();

        authService.cadastrarMedico(medico);
        Medico autenticado = authService.autenticarMedico(medico.getCrm(), medico.getSenha());

        assertEquals(medico.getCrm(), autenticado.getCrm());
        assertNotNull(autenticado.getAgenda());
    }

    @Test
    void impedeCadastroComEmailDuplicado() {
        Paciente paciente = novoPaciente(novoEmail());
        authService.cadastrarPaciente(paciente);

        Paciente duplicado = novoPaciente(paciente.getEmail());
        assertThrows(IllegalArgumentException.class, () -> authService.cadastrarPaciente(duplicado));
    }

    private static String novoEmail() {
        return "paciente-" + UUID.randomUUID() + "@teste.com";
    }

    private static Paciente novoPaciente(String email) {
        return new Paciente(null, "Paciente Teste", email, "senha123", "11999999999",
                null, LocalDate.of(1990, 1, 1), "cpf-" + UUID.randomUUID());
    }

    private static Medico novoMedico() {
        return new Medico(null, "Medico Teste", "medico-" + UUID.randomUUID() + "@teste.com",
                "senha123", "11988888888", null, "CRM-" + UUID.randomUUID(), "Biografia");
    }
}
