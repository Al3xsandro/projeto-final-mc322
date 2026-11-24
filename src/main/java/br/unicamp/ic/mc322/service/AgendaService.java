package br.unicamp.ic.mc322.service;

import br.unicamp.ic.mc322.model.Agenda;
import br.unicamp.ic.mc322.model.Agendamento;
import br.unicamp.ic.mc322.model.HorarioDisponivel;
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
import java.util.UUID;

public class AgendaService {

    private final MedicoRepository medicoRepository = new MedicoRepository();
    private final PacienteRepository pacienteRepository = new PacienteRepository();
    private final AgendamentoRepository agendamentoRepository = new AgendamentoRepository();

    public AgendaService() {
    }

    public void adicionarHorarioDisponivel(Medico medico, HorarioDisponivel horario) {
        if (medico == null || horario == null) {
            return;
        }
        List<Medico> medicos = medicoRepository.carregar();
        Medico existente = encontrar(medicos, medico.getId());
        if (existente == null) {
            return;
        }
        if (existente.getAgenda() == null) {
            existente.setAgenda(new Agenda(existente));
        }
        if (horario.getId() == null) {
            horario.setId(UUID.randomUUID().toString());
        }
        existente.getAgenda().getHorariosDisponiveis().add(horario);
        medicoRepository.salvar(medicos);
    }

    public void removerHorarioDisponivel(Medico medico, String idHorario) {
        if (medico == null) {
            return;
        }
        List<Medico> medicos = medicoRepository.carregar();
        Medico existente = encontrar(medicos, medico.getId());
        if (existente == null || existente.getAgenda() == null) {
            return;
        }
        existente.getAgenda().getHorariosDisponiveis()
                .removeIf(horario -> Objects.equals(horario.getId(), idHorario));
        medicoRepository.salvar(medicos);
    }

    public List<HorarioDisponivel> listarHorariosDisponiveis(Medico medico) {
        if (medico == null) {
            return new ArrayList<>();
        }
        Medico existente = encontrar(medicoRepository.carregar(), medico.getId());
        if (existente == null || existente.getAgenda() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(existente.getAgenda().getHorariosDisponiveis());
    }

    public List<Agendamento> listarHistoricoAtendimentos(Medico medico) {
        List<Agendamento> historico = new ArrayList<>();
        if (medico == null) {
            return historico;
        }
        List<Medico> medicos = medicoRepository.carregar();
        List<Paciente> pacientes = pacienteRepository.carregar();
        for (Agendamento agendamento : agendamentoRepository.carregar(medicos, pacientes)) {
            if (Objects.equals(agendamento.getMedico().getId(), medico.getId())
                    && finalizado(agendamento.getStatus())) {
                historico.add(agendamento);
            }
        }
        historico.sort(Comparator.comparing(Agendamento::getDataHora,
                Comparator.nullsLast(Comparator.reverseOrder())));
        return historico;
    }

    private static boolean finalizado(StatusAgendamento status) {
        return status == StatusAgendamento.REALIZADO
                || status == StatusAgendamento.RECUSADO
                || status == StatusAgendamento.CANCELADO;
    }

    private static Medico encontrar(List<Medico> medicos, String id) {
        for (Medico medico : medicos) {
            if (Objects.equals(medico.getId(), id)) {
                return medico;
            }
        }
        return null;
    }
}
