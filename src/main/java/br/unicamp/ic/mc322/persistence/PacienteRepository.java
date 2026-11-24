package br.unicamp.ic.mc322.persistence;

import br.unicamp.ic.mc322.model.Paciente;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PacienteRepository {

    private static final Path ARQUIVO = Path.of("data", "pacientes.xml");

    public PacienteRepository() {
    }

    public List<Paciente> carregar() {
        Document doc = XmlUtil.ler(ARQUIVO, "pacientes");
        List<Paciente> pacientes = new ArrayList<>();

        for (Element ep : XmlUtil.filhos(doc.getDocumentElement(), "paciente")) {
            Paciente paciente = new Paciente();
            paciente.setId(XmlUtil.texto(ep, "id"));
            paciente.setNome(XmlUtil.texto(ep, "nome"));
            paciente.setEmail(XmlUtil.texto(ep, "email"));
            paciente.setSenha(XmlUtil.texto(ep, "senha"));
            paciente.setTelefone(XmlUtil.texto(ep, "telefone"));
            paciente.setDataCadastro(XmlUtil.data(XmlUtil.texto(ep, "dataCadastro")));
            paciente.setDataNascimento(XmlUtil.data(XmlUtil.texto(ep, "dataNascimento")));
            paciente.setCpf(XmlUtil.texto(ep, "cpf"));
            pacientes.add(paciente);
        }
        return pacientes;
    }

    public void salvar(List<Paciente> pacientes) {
        Document doc = XmlUtil.criarDocumento("pacientes");
        Element raiz = doc.getDocumentElement();

        for (Paciente paciente : pacientes) {
            Element ep = doc.createElement("paciente");
            raiz.appendChild(ep);

            XmlUtil.add(doc, ep, "id", paciente.getId());
            XmlUtil.add(doc, ep, "nome", paciente.getNome());
            XmlUtil.add(doc, ep, "email", paciente.getEmail());
            XmlUtil.add(doc, ep, "senha", paciente.getSenha());
            XmlUtil.add(doc, ep, "telefone", paciente.getTelefone());
            XmlUtil.add(doc, ep, "dataCadastro", paciente.getDataCadastro());
            XmlUtil.add(doc, ep, "dataNascimento", paciente.getDataNascimento());
            XmlUtil.add(doc, ep, "cpf", paciente.getCpf());
        }
        XmlUtil.salvar(doc, ARQUIVO);
    }
}
