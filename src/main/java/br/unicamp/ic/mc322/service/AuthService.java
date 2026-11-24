package br.unicamp.ic.mc322.service;

import br.unicamp.ic.mc322.exception.AutenticacaoException;
import br.unicamp.ic.mc322.model.Agenda;
import br.unicamp.ic.mc322.model.Medico;
import br.unicamp.ic.mc322.model.Paciente;
import br.unicamp.ic.mc322.persistence.MedicoRepository;
import br.unicamp.ic.mc322.persistence.PacienteRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class AuthService {

    private final MedicoRepository medicoRepository = new MedicoRepository();
    private final PacienteRepository pacienteRepository = new PacienteRepository();

    public AuthService() {
    }

    public Medico autenticarMedico(String crm, String senha) {
        for (Medico medico : medicoRepository.carregar()) {
            if (medico.getCrm() != null && medico.getCrm().equalsIgnoreCase(crm)
                    && medico.getSenha() != null && medico.getSenha().equals(senha)) {
                return medico;
            }
        }
        throw new AutenticacaoException("CRM ou senha inválidos.");
    }

    public Paciente autenticarPaciente(String email, String senha) {
        for (Paciente paciente : pacienteRepository.carregar()) {
            if (paciente.getEmail() != null && paciente.getEmail().equalsIgnoreCase(email)
                    && paciente.getSenha() != null && paciente.getSenha().equals(senha)) {
                return paciente;
            }
        }
        throw new AutenticacaoException("E-mail ou senha inválidos.");
    }

    public void cadastrarMedico(Medico medico) {
        if (medico == null || vazio(medico.getCrm()) || vazio(medico.getNome())
                || vazio(medico.getEmail()) || vazio(medico.getSenha())) {
            throw new IllegalArgumentException("Preencha nome, e-mail, senha e CRM.");
        }
        List<Medico> medicos = medicoRepository.carregar();
        for (Medico existente : medicos) {
            if (medico.getCrm().equalsIgnoreCase(existente.getCrm())) {
                throw new IllegalArgumentException("CRM já cadastrado.");
            }
        }
        if (medico.getId() == null) {
            medico.setId(UUID.randomUUID().toString());
        }
        if (medico.getDataCadastro() == null) {
            medico.setDataCadastro(LocalDate.now());
        }
        if (medico.getAgenda() == null) {
            medico.setAgenda(new Agenda(medico));
        }
        medicos.add(medico);
        medicoRepository.salvar(medicos);
    }

    public void cadastrarPaciente(Paciente paciente) {
        if (paciente == null || vazio(paciente.getCpf()) || vazio(paciente.getNome())
                || vazio(paciente.getEmail()) || vazio(paciente.getSenha())) {
            throw new IllegalArgumentException("Preencha nome, e-mail, senha e CPF.");
        }
        List<Paciente> pacientes = pacienteRepository.carregar();
        for (Paciente existente : pacientes) {
            if (paciente.getEmail().equalsIgnoreCase(existente.getEmail())) {
                throw new IllegalArgumentException("E-mail já cadastrado.");
            }
        }
        if (paciente.getId() == null) {
            paciente.setId(UUID.randomUUID().toString());
        }
        if (paciente.getDataCadastro() == null) {
            paciente.setDataCadastro(LocalDate.now());
        }
        pacientes.add(paciente);
        pacienteRepository.salvar(pacientes);
    }

    private static boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
