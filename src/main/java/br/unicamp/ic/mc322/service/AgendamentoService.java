package br.unicamp.ic.mc322.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import br.unicamp.ic.mc322.exception.AgendamentoException;
import br.unicamp.ic.mc322.model.Agendamento;
import br.unicamp.ic.mc322.model.Exame;
import br.unicamp.ic.mc322.model.Medico;
import br.unicamp.ic.mc322.model.Paciente;
import br.unicamp.ic.mc322.model.enums.StatusAgendamento;
import br.unicamp.ic.mc322.model.enums.TipoAgendamento;
import br.unicamp.ic.mc322.persistence.AgendamentoRepository;
import br.unicamp.ic.mc322.persistence.MedicoRepository;
import br.unicamp.ic.mc322.persistence.PacienteRepository;

public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository = new AgendamentoRepository();
    private final MedicoRepository medicoRepository = new MedicoRepository();
    private final PacienteRepository pacienteRepository = new PacienteRepository();

    public AgendamentoService() {
    }

    public Agendamento solicitarConsulta(Paciente paciente, Medico medico, LocalDateTime dataHora) {
        return solicitar(paciente, medico, TipoAgendamento.CONSULTA, null, dataHora);
    }

    public Agendamento solicitarExame(Paciente paciente, Medico medico, Exame exame, LocalDateTime dataHora) {
        return solicitar(paciente, medico, TipoAgendamento.EXAME, exame, dataHora);
    }

    public void confirmarAgendamento(String id) {
        alterarStatus(id, StatusAgendamento.CONFIRMADO);
    }

    public void recusarAgendamento(String id) {
        alterarStatus(id, StatusAgendamento.RECUSADO);
    }

    public void cancelarPeloMedico(String id) {
        alterarStatus(id, StatusAgendamento.CANCELADO);
    }

    public void cancelarPeloPaciente(String id) {
        alterarStatus(id, StatusAgendamento.CANCELADO);
    }

    public void registrarRealizacao(String id) {
        alterarStatus(id, StatusAgendamento.REALIZADO);
    }

    public List<Agendamento> listarSolicitacoesPendentes(Medico medico) {
        List<Agendamento> pendencias = new ArrayList<>();
        if (medico == null) {
            return pendencias;
        }
        for (Agendamento agendamento : carregarTodos()) {
            if (Objects.equals(agendamento.getMedico().getId(), medico.getId())
                    && agendamento.getStatus() == StatusAgendamento.SOLICITADO) {
                pendencias.add(agendamento);
            }
        }
        pendencias.sort(Comparator.comparing(Agendamento::getDataSolicitacao,
                Comparator.nullsLast(Comparator.naturalOrder())));
        return pendencias;
    }

    public List<Agendamento> listarProximosAgendamentos(Paciente paciente) {
        List<Agendamento> proximos = new ArrayList<>();
        if (paciente == null) {
            return proximos;
        }
        for (Agendamento agendamento : carregarTodos()) {
            if (Objects.equals(agendamento.getPaciente().getId(), paciente.getId())
                    && (agendamento.getStatus() == StatusAgendamento.SOLICITADO
                    || agendamento.getStatus() == StatusAgendamento.CONFIRMADO)) {
                proximos.add(agendamento);
            }
        }
        proximos.sort(Comparator.comparing(Agendamento::getDataHora,
                Comparator.nullsLast(Comparator.naturalOrder())));
        return proximos;
    }

    private Agendamento solicitar(Paciente paciente, Medico medico, TipoAgendamento tipo,
            Exame exame, LocalDateTime dataHora) {
        if (paciente == null || medico == null || dataHora == null) {
            throw new AgendamentoException("Dados do agendamento incompletos.");
        }
        Agendamento agendamento = new Agendamento(
                UUID.randomUUID().toString(),
                paciente,
                medico,
                tipo,
                exame,
                dataHora,
                StatusAgendamento.SOLICITADO,
                LocalDateTime.now());
        List<Agendamento> agendamentos = carregarTodos();
        agendamentos.add(agendamento);
        agendamentoRepository.salvar(agendamentos);
        return agendamento;
    }

    private void alterarStatus(String id, StatusAgendamento novoStatus) {
        List<Agendamento> agendamentos = carregarTodos();
        for (Agendamento agendamento : agendamentos) {
            if (Objects.equals(agendamento.getId(), id)) {
                agendamento.setStatus(novoStatus);
                agendamentoRepository.salvar(agendamentos);
                return;
            }
        }
        throw new AgendamentoException("Agendamento não encontrado.");
    }

    private List<Agendamento> carregarTodos() {
        return agendamentoRepository.carregar(medicoRepository.carregar(), pacienteRepository.carregar());
    }
}
