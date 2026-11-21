package br.unicamp.ic.mc322.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Medico extends Usuario {

    private String crm;
    private String biografia;
    private List<Especialidade> especialidades;
    private List<Exame> examesOferecidos;
    private Agenda agenda;

    public Medico() {
        this.especialidades = new ArrayList<>();
        this.examesOferecidos = new ArrayList<>();
    }

    public Medico(String id, String nome, String email, String senha, String telefone,
                  LocalDate dataCadastro, String crm, String biografia) {
        super(id, nome, email, senha, telefone, dataCadastro);
        this.crm = crm;
        this.biografia = biografia;
        this.especialidades = new ArrayList<>();
        this.examesOferecidos = new ArrayList<>();
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public List<Especialidade> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<Especialidade> especialidades) {
        this.especialidades = especialidades;
    }

    public List<Exame> getExamesOferecidos() {
        return examesOferecidos;
    }

    public void setExamesOferecidos(List<Exame> examesOferecidos) {
        this.examesOferecidos = examesOferecidos;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }
}
