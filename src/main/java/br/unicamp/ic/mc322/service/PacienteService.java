package br.unicamp.ic.mc322.service;

import br.unicamp.ic.mc322.model.Agendamento;
import br.unicamp.ic.mc322.model.Medico;
import br.unicamp.ic.mc322.model.Paciente;
import br.unicamp.ic.mc322.model.enums.StatusAgendamento;
import br.unicamp.ic.mc322.persistence.AgendamentoRepository;
import br.unicamp.ic.mc322.persistence.MedicoRepository;
import br.unicamp.ic.mc322.persistence.PacienteRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class PacienteService {

    private final PacienteRepository pacienteRepository = new PacienteRepository();
    private final MedicoRepository medicoRepository = new MedicoRepository();
    private final AgendamentoRepository agendamentoRepository = new AgendamentoRepository();

    public PacienteService() {
    }

    public void atualizarPerfil(Paciente paciente) {
        if (paciente == null) {
            return;
        }
        List<Paciente> pacientes = pacienteRepository.carregar();
        Paciente existente = encontrar(pacientes, paciente.getId());
        if (existente == null) {
            return;
        }
        existente.setNome(paciente.getNome());
        existente.setEmail(paciente.getEmail());
        existente.setSenha(paciente.getSenha());
        existente.setTelefone(paciente.getTelefone());
        existente.setDataNascimento(paciente.getDataNascimento());
        existente.setCpf(paciente.getCpf());
        pacienteRepository.salvar(pacientes);
    }

    public List<Agendamento> listarHistorico(Paciente paciente) {
        List<Agendamento> historico = new ArrayList<>();
        if (paciente == null) {
            return historico;
        }
        List<Medico> medicos = medicoRepository.carregar();
        List<Paciente> pacientes = pacienteRepository.carregar();
        for (Agendamento agendamento : agendamentoRepository.carregar(medicos, pacientes)) {
            if (Objects.equals(agendamento.getPaciente().getId(), paciente.getId())
                    && finalizado(agendamento.getStatus())) {
                historico.add(agendamento);
            }
        }
        historico.sort(Comparator.comparing(Agendamento::getDataHora,
                Comparator.nullsLast(Comparator.reverseOrder())));
        return historico;
    }

    public Paciente buscarPorId(String id) {
        return encontrar(pacienteRepository.carregar(), id);
    }

    private static boolean finalizado(StatusAgendamento status) {
        return status == StatusAgendamento.REALIZADO
                || status == StatusAgendamento.RECUSADO
                || status == StatusAgendamento.CANCELADO;
    }

    private static Paciente encontrar(List<Paciente> pacientes, String id) {
        for (Paciente paciente : pacientes) {
            if (Objects.equals(paciente.getId(), id)) {
                return paciente;
            }
        }
        return null;
    }
}
