package br.unicamp.ic.mc322.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paciente extends Usuario {

    private LocalDate dataNascimento;
    private String cpf;
    private List<Agendamento> historico;

    public Paciente() {
        this.historico = new ArrayList<>();
    }

    public Paciente(String id, String nome, String email, String senha, String telefone,
                    LocalDate dataCadastro, LocalDate dataNascimento, String cpf) {
        super(id, nome, email, senha, telefone, dataCadastro);
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.historico = new ArrayList<>();
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<Agendamento> getHistorico() {
        return historico;
    }

    public void setHistorico(List<Agendamento> historico) {
        this.historico = historico;
    }
}
