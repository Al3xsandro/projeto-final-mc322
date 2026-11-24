package br.unicamp.ic.mc322.persistence;

import br.unicamp.ic.mc322.model.Agendamento;
import br.unicamp.ic.mc322.model.Exame;
import br.unicamp.ic.mc322.model.Medico;
import br.unicamp.ic.mc322.model.Paciente;
import br.unicamp.ic.mc322.model.enums.StatusAgendamento;
import br.unicamp.ic.mc322.model.enums.TipoAgendamento;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgendamentoRepository {

    private static final Path ARQUIVO = Path.of("data", "agendamentos.xml");

    public AgendamentoRepository() {
    }

    public List<Agendamento> carregar(List<Medico> medicos, List<Paciente> pacientes) {
        Map<String, Medico> medicosPorId = new HashMap<>();
        for (Medico medico : medicos) {
            medicosPorId.put(medico.getId(), medico);
        }
        Map<String, Paciente> pacientesPorId = new HashMap<>();
        for (Paciente paciente : pacientes) {
            pacientesPorId.put(paciente.getId(), paciente);
        }

        Document doc = XmlUtil.ler(ARQUIVO, "agendamentos");
        List<Agendamento> agendamentos = new ArrayList<>();

        for (Element ea : XmlUtil.filhos(doc.getDocumentElement(), "agendamento")) {
            Agendamento agendamento = new Agendamento();
            agendamento.setId(XmlUtil.texto(ea, "id"));
            agendamento.setMedico(medicosPorId.get(XmlUtil.texto(ea, "medicoId")));
            agendamento.setPaciente(pacientesPorId.get(XmlUtil.texto(ea, "pacienteId")));

            String tipo = XmlUtil.texto(ea, "tipo");
            agendamento.setTipo(tipo == null ? null : TipoAgendamento.valueOf(tipo));
            String status = XmlUtil.texto(ea, "status");
            agendamento.setStatus(status == null ? null : StatusAgendamento.valueOf(status));
            agendamento.setDataHora(XmlUtil.dataHora(XmlUtil.texto(ea, "dataHora")));
            agendamento.setDataSolicitacao(XmlUtil.dataHora(XmlUtil.texto(ea, "dataSolicitacao")));

            Element elementExame = XmlUtil.filho(ea, "exame");
            if (elementExame != null) {
                agendamento.setExame(new Exame(
                        XmlUtil.texto(elementExame, "id"),
                        XmlUtil.texto(elementExame, "nome"),
                        XmlUtil.texto(elementExame, "descricao")));
            }

            if (agendamento.getMedico() != null && agendamento.getPaciente() != null) {
                agendamentos.add(agendamento);
            }
        }
        return agendamentos;
    }

    public void salvar(List<Agendamento> agendamentos) {
        Document doc = XmlUtil.criarDocumento("agendamentos");
        Element raiz = doc.getDocumentElement();

        for (Agendamento agendamento : agendamentos) {
            Element ea = doc.createElement("agendamento");
            raiz.appendChild(ea);

            XmlUtil.add(doc, ea, "id", agendamento.getId());
            XmlUtil.add(doc, ea, "medicoId", agendamento.getMedico() == null ? null : agendamento.getMedico().getId());
            XmlUtil.add(doc, ea, "pacienteId", agendamento.getPaciente() == null ? null : agendamento.getPaciente().getId());
            XmlUtil.add(doc, ea, "tipo", agendamento.getTipo());
            XmlUtil.add(doc, ea, "dataHora", agendamento.getDataHora());
            XmlUtil.add(doc, ea, "status", agendamento.getStatus());
            XmlUtil.add(doc, ea, "dataSolicitacao", agendamento.getDataSolicitacao());

            if (agendamento.getExame() != null) {
                Element exame = XmlUtil.add(doc, ea, "exame", null);
                XmlUtil.add(doc, exame, "id", agendamento.getExame().getId());
                XmlUtil.add(doc, exame, "nome", agendamento.getExame().getNome());
                XmlUtil.add(doc, exame, "descricao", agendamento.getExame().getDescricao());
            }
        }
        XmlUtil.salvar(doc, ARQUIVO);
    }
}
