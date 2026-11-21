package br.unicamp.ic.mc322.model;

import java.util.ArrayList;
import java.util.List;

public class Agenda {

    private Medico medico;
    private List<HorarioDisponivel> horariosDisponiveis;
    private List<Agendamento> agendamentos;

    public Agenda() {
        this.horariosDisponiveis = new ArrayList<>();
        this.agendamentos = new ArrayList<>();
    }

    public Agenda(Medico medico) {
        this.medico = medico;
        this.horariosDisponiveis = new ArrayList<>();
        this.agendamentos = new ArrayList<>();
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public List<HorarioDisponivel> getHorariosDisponiveis() {
        return horariosDisponiveis;
    }

    public void setHorariosDisponiveis(List<HorarioDisponivel> horariosDisponiveis) {
        this.horariosDisponiveis = horariosDisponiveis;
    }

    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    public void setAgendamentos(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }
}
