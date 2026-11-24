package br.unicamp.ic.mc322.persistence;

import java.nio.file.Path;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import br.unicamp.ic.mc322.model.Agenda;
import br.unicamp.ic.mc322.model.Especialidade;
import br.unicamp.ic.mc322.model.Exame;
import br.unicamp.ic.mc322.model.HorarioDisponivel;
import br.unicamp.ic.mc322.model.Medico;

public class MedicoRepository {

    private static final Path ARQUIVO = Path.of("data", "medicos.xml");

    public MedicoRepository() {
    }

    public List<Medico> carregar() {
        Document doc = XmlUtil.ler(ARQUIVO, "medicos");
        List<Medico> medicos = new ArrayList<>();

        for (Element em : XmlUtil.filhos(doc.getDocumentElement(), "medico")) {
            Medico medico = new Medico();
            medico.setId(XmlUtil.texto(em, "id"));
            medico.setNome(XmlUtil.texto(em, "nome"));
            medico.setEmail(XmlUtil.texto(em, "email"));
            medico.setSenha(XmlUtil.texto(em, "senha"));
            medico.setTelefone(XmlUtil.texto(em, "telefone"));
            medico.setDataCadastro(XmlUtil.data(XmlUtil.texto(em, "dataCadastro")));
            medico.setCrm(XmlUtil.texto(em, "crm"));
            medico.setBiografia(XmlUtil.texto(em, "biografia"));

            List<Especialidade> especialidades = new ArrayList<>();
            Element containerEspecialidades = XmlUtil.filho(em, "especialidades");
            if (containerEspecialidades != null) {
                for (Element e : XmlUtil.filhos(containerEspecialidades, "especialidade")) {
                    especialidades.add(new Especialidade(
                            XmlUtil.texto(e, "id"),
                            XmlUtil.texto(e, "nome"),
                            XmlUtil.texto(e, "descricao")));
                }
            }
            medico.setEspecialidades(especialidades);

            List<Exame> exames = new ArrayList<>();
            Element containerExames = XmlUtil.filho(em, "exames");
            if (containerExames != null) {
                for (Element e : XmlUtil.filhos(containerExames, "exame")) {
                    exames.add(new Exame(
                            XmlUtil.texto(e, "id"),
                            XmlUtil.texto(e, "nome"),
                            XmlUtil.texto(e, "descricao")));
                }
            }
            medico.setExamesOferecidos(exames);

            Agenda agenda = new Agenda(medico);
            Element elementAgenda = XmlUtil.filho(em, "agenda");
            if (elementAgenda != null) {
                Element containerHorarios = XmlUtil.filho(elementAgenda, "horarios");
                if (containerHorarios != null) {
                    for (Element e : XmlUtil.filhos(containerHorarios, "horario")) {
                        String dia = XmlUtil.texto(e, "diaDaSemana");
                        agenda.getHorariosDisponiveis().add(new HorarioDisponivel(
                                XmlUtil.texto(e, "id"),
                                dia == null ? null : DayOfWeek.valueOf(dia),
                                XmlUtil.hora(XmlUtil.texto(e, "horaInicio")),
                                XmlUtil.hora(XmlUtil.texto(e, "horaFim")),
                                XmlUtil.logico(XmlUtil.texto(e, "disponivel"))));
                    }
                }
            }
            medico.setAgenda(agenda);
            medicos.add(medico);
        }
        return medicos;
    }

    public void salvar(List<Medico> medicos) {
        Document doc = XmlUtil.criarDocumento("medicos");
        Element raiz = doc.getDocumentElement();

        for (Medico medico : medicos) {
            Element em = doc.createElement("medico");
            raiz.appendChild(em);

            XmlUtil.add(doc, em, "id", medico.getId());
            XmlUtil.add(doc, em, "nome", medico.getNome());
            XmlUtil.add(doc, em, "email", medico.getEmail());
            XmlUtil.add(doc, em, "senha", medico.getSenha());
            XmlUtil.add(doc, em, "telefone", medico.getTelefone());
            XmlUtil.add(doc, em, "dataCadastro", medico.getDataCadastro());
            XmlUtil.add(doc, em, "crm", medico.getCrm());
            XmlUtil.add(doc, em, "biografia", medico.getBiografia());

            Element especialidades = doc.createElement("especialidades");
            em.appendChild(especialidades);
            if (medico.getEspecialidades() != null) {
                for (Especialidade especialidade : medico.getEspecialidades()) {
                    Element ee = XmlUtil.add(doc, especialidades, "especialidade", null);
                    XmlUtil.add(doc, ee, "id", especialidade.getId());
                    XmlUtil.add(doc, ee, "nome", especialidade.getNome());
                    XmlUtil.add(doc, ee, "descricao", especialidade.getDescricao());
                }
            }

            Element exames = doc.createElement("exames");
            em.appendChild(exames);
            if (medico.getExamesOferecidos() != null) {
                for (Exame exame : medico.getExamesOferecidos()) {
                    Element ee = XmlUtil.add(doc, exames, "exame", null);
                    XmlUtil.add(doc, ee, "id", exame.getId());
                    XmlUtil.add(doc, ee, "nome", exame.getNome());
                    XmlUtil.add(doc, ee, "descricao", exame.getDescricao());
                }
            }

            Element agenda = doc.createElement("agenda");
            em.appendChild(agenda);
            Element horarios = doc.createElement("horarios");
            agenda.appendChild(horarios);
            if (medico.getAgenda() != null) {
                for (HorarioDisponivel horario : medico.getAgenda().getHorariosDisponiveis()) {
                    Element eh = XmlUtil.add(doc, horarios, "horario", null);
                    XmlUtil.add(doc, eh, "id", horario.getId());
                    XmlUtil.add(doc, eh, "diaDaSemana", horario.getDiaDaSemana());
                    XmlUtil.add(doc, eh, "horaInicio", horario.getHoraInicio());
                    XmlUtil.add(doc, eh, "horaFim", horario.getHoraFim());
                    XmlUtil.add(doc, eh, "disponivel", horario.estaDisponivel());
                }
            }
        }
        XmlUtil.salvar(doc, ARQUIVO);
    }
}
