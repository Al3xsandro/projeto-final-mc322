package br.unicamp.ic.mc322.service;

import br.unicamp.ic.mc322.model.Especialidade;
import br.unicamp.ic.mc322.model.Exame;
import br.unicamp.ic.mc322.model.HorarioDisponivel;
import br.unicamp.ic.mc322.model.Medico;
import br.unicamp.ic.mc322.persistence.MedicoRepository;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BuscaService {

    private final MedicoRepository medicoRepository = new MedicoRepository();

    public BuscaService() {
    }

    public List<Medico> buscarPorNome(String nome) {
        String termo = normalizar(nome);
        List<Medico> resultado = new ArrayList<>();
        for (Medico medico : medicoRepository.carregar()) {
            if (normalizar(medico.getNome()).contains(termo)) {
                resultado.add(medico);
            }
        }
        return resultado;
    }

    public List<Medico> buscarPorEspecialidade(String nome) {
        String termo = normalizar(nome);
        List<Medico> resultado = new ArrayList<>();
        for (Medico medico : medicoRepository.carregar()) {
            if (termo.isEmpty() || possuiEspecialidade(medico, termo)) {
                resultado.add(medico);
            }
        }
        return resultado;
    }

    public List<Medico> buscarPorExame(String nome) {
        String termo = normalizar(nome);
        List<Medico> resultado = new ArrayList<>();
        for (Medico medico : medicoRepository.carregar()) {
            if (termo.isEmpty() || possuiExame(medico, termo)) {
                resultado.add(medico);
            }
        }
        return resultado;
    }

    public List<Medico> buscarPorDisponibilidade(DayOfWeek dia) {
        List<Medico> resultado = new ArrayList<>();
        if (dia == null) {
            return resultado;
        }
        for (Medico medico : medicoRepository.carregar()) {
            if (possuiDisponibilidade(medico, dia)) {
                resultado.add(medico);
            }
        }
        return resultado;
    }

    private static boolean possuiEspecialidade(Medico medico, String termo) {
        if (medico.getEspecialidades() == null) {
            return false;
        }
        for (Especialidade especialidade : medico.getEspecialidades()) {
            if (normalizar(especialidade.getNome()).contains(termo)) {
                return true;
            }
        }
        return false;
    }

    private static boolean possuiExame(Medico medico, String termo) {
        if (medico.getExamesOferecidos() == null) {
            return false;
        }
        for (Exame exame : medico.getExamesOferecidos()) {
            if (normalizar(exame.getNome()).contains(termo)) {
                return true;
            }
        }
        return false;
    }

    private static boolean possuiDisponibilidade(Medico medico, DayOfWeek dia) {
        if (medico.getAgenda() == null) {
            return false;
        }
        for (HorarioDisponivel horario : medico.getAgenda().getHorariosDisponiveis()) {
            if (horario.getDiaDaSemana() == dia && horario.isDisponivel()) {
                return true;
            }
        }
        return false;
    }

    private static String normalizar(String valor) {
        return valor == null ? "" : valor.trim().toLowerCase(Locale.ROOT);
    }
}
