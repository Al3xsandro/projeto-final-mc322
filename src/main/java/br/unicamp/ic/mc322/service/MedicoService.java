package br.unicamp.ic.mc322.service;

import br.unicamp.ic.mc322.model.Especialidade;
import br.unicamp.ic.mc322.model.Exame;
import br.unicamp.ic.mc322.model.Medico;
import br.unicamp.ic.mc322.persistence.MedicoRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MedicoService {

    private final MedicoRepository medicoRepository = new MedicoRepository();

    public MedicoService() {
    }

    public void atualizarPerfil(Medico medico) {
        if (medico == null) {
            return;
        }
        List<Medico> medicos = medicoRepository.carregar();
        Medico existente = encontrar(medicos, medico.getId());
        if (existente == null) {
            return;
        }
        existente.setNome(medico.getNome());
        existente.setEmail(medico.getEmail());
        existente.setSenha(medico.getSenha());
        existente.setTelefone(medico.getTelefone());
        existente.setCrm(medico.getCrm());
        existente.setBiografia(medico.getBiografia());
        medicoRepository.salvar(medicos);
    }

    public void adicionarEspecialidade(Medico medico, Especialidade especialidade) {
        List<Medico> medicos = medicoRepository.carregar();
        Medico existente = encontrar(medicos, medico.getId());
        if (existente == null || especialidade == null) {
            return;
        }
        if (especialidade.getId() == null) {
            especialidade.setId(UUID.randomUUID().toString());
        }
        existente.getEspecialidades().add(especialidade);
        medicoRepository.salvar(medicos);
    }

    public void removerEspecialidade(Medico medico, String idEspecialidade) {
        List<Medico> medicos = medicoRepository.carregar();
        Medico existente = encontrar(medicos, medico.getId());
        if (existente == null || existente.getEspecialidades() == null) {
            return;
        }
        existente.getEspecialidades().removeIf(e -> Objects.equals(e.getId(), idEspecialidade));
        medicoRepository.salvar(medicos);
    }

    public void adicionarExame(Medico medico, Exame exame) {
        List<Medico> medicos = medicoRepository.carregar();
        Medico existente = encontrar(medicos, medico.getId());
        if (existente == null || exame == null) {
            return;
        }
        if (exame.getId() == null) {
            exame.setId(UUID.randomUUID().toString());
        }
        existente.getExamesOferecidos().add(exame);
        medicoRepository.salvar(medicos);
    }

    public void removerExame(Medico medico, String idExame) {
        List<Medico> medicos = medicoRepository.carregar();
        Medico existente = encontrar(medicos, medico.getId());
        if (existente == null || existente.getExamesOferecidos() == null) {
            return;
        }
        existente.getExamesOferecidos().removeIf(e -> Objects.equals(e.getId(), idExame));
        medicoRepository.salvar(medicos);
    }

    public Medico buscarPorId(String id) {
        return encontrar(medicoRepository.carregar(), id);
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
