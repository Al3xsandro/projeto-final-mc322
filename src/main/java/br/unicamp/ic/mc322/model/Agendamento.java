package br.unicamp.ic.mc322.model;

import br.unicamp.ic.mc322.model.enums.StatusAgendamento;
import br.unicamp.ic.mc322.model.enums.TipoAgendamento;

import java.time.LocalDateTime;

public class Agendamento {

    private String id;
    private Paciente paciente;
    private Medico medico;
    private TipoAgendamento tipo;
    private Exame exame;
    private LocalDateTime dataHora;
    private StatusAgendamento status;
    private LocalDateTime dataSolicitacao;

    public Agendamento() {
    }

    public Agendamento(String id, Paciente paciente, Medico medico, TipoAgendamento tipo,
                       Exame exame, LocalDateTime dataHora, StatusAgendamento status,
                       LocalDateTime dataSolicitacao) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.tipo = tipo;
        this.exame = exame;
        this.dataHora = dataHora;
        this.status = status;
        this.dataSolicitacao = dataSolicitacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public TipoAgendamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoAgendamento tipo) {
        this.tipo = tipo;
    }

    public Exame getExame() {
        return exame;
    }

    public void setExame(Exame exame) {
        this.exame = exame;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }
}
