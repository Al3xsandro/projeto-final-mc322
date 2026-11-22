package br.unicamp.ic.mc322.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class HorarioDisponivel {

    private String id;
    private DayOfWeek diaDaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private boolean disponivel;

    public HorarioDisponivel() {
    }

    public HorarioDisponivel(String id, DayOfWeek diaDaSemana, LocalTime horaInicio, LocalTime horaFim, boolean disponivel) {
        this.id = id;
        this.diaDaSemana = diaDaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.disponivel = disponivel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DayOfWeek getDiaDaSemana() {
        return diaDaSemana;
    }

    public void setDiaDaSemana(DayOfWeek diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public boolean estaDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}
