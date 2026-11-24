package br.unicamp.ic.mc322.telas;

import br.unicamp.ic.mc322.model.Agendamento;
import br.unicamp.ic.mc322.model.Atendimento;
import br.unicamp.ic.mc322.model.Exame;
import br.unicamp.ic.mc322.model.Medico;
import br.unicamp.ic.mc322.model.Paciente;
import br.unicamp.ic.mc322.model.enums.TipoAgendamento;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class PainelPaciente {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Paciente paciente;
    private final VBox resultados = new VBox(8);
    private final VBox proximos = new VBox(6);
    private final VBox historico = new VBox(6);
    private final TextField dataHora = new TextField();
    private final Label mensagem = new Label();

    public PainelPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Parent criar() {
        atualizarAgendamentos();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #eef2f7;");
        root.setTop(cabecalho());

        HBox colunas = new HBox(20, colunaBusca(), colunaAgendamentos());
        colunas.setPadding(new Insets(20));
        colunas.setAlignment(Pos.TOP_LEFT);
        root.setCenter(colunas);
        root.setBottom(rodape());
        return root;
    }

    private VBox cabecalho() {
        Label nome = new Label(texto(paciente.getNome()));
        nome.setFont(Font.font("System", FontWeight.BOLD, 24));
        nome.setStyle("-fx-text-fill: white;");
        Label dados = new Label("E-mail: " + texto(paciente.getEmail()) + "   |   CPF: " + texto(paciente.getCpf())
                + "   |   Telefone: " + texto(paciente.getTelefone()));
        dados.setStyle("-fx-text-fill: white;");

        VBox cabecalho = new VBox(4, nome, dados);
        cabecalho.setPadding(new Insets(18, 24, 18, 24));
        cabecalho.setStyle("-fx-background-color: #1f6feb;");
        return cabecalho;
    }

    private VBox colunaBusca() {
        Label titulo = titulo("Buscar médicos");

        TextField termo = new TextField();
        termo.setPromptText("Nome, especialidade ou exame");
        termo.setMaxWidth(470);

        Button porNome = new Button("Buscar por nome");
        porNome.setOnAction(e -> pesquisar(Navegacao.busca().buscarPorNome(termo.getText()), null));

        Button porEspecialidade = new Button("Buscar por especialidade");
        porEspecialidade.setOnAction(e -> pesquisar(Navegacao.busca().buscarPorEspecialidade(termo.getText()), null));

        Button porExame = new Button("Buscar por exame");
        porExame.setOnAction(e -> pesquisar(Navegacao.busca().buscarPorExame(termo.getText()), termo.getText().trim()));

        HBox botoes = new HBox(8, porNome, porEspecialidade, porExame);
        botoes.setAlignment(Pos.CENTER_LEFT);

        ComboBox<DayOfWeek> dia = new ComboBox<>();
        dia.getItems().addAll(DayOfWeek.values());
        dia.setPromptText("Dia da semana");
        Button porDisponibilidade = new Button("Buscar por disponibilidade");
        porDisponibilidade.setOnAction(e ->
                pesquisar(Navegacao.busca().buscarPorDisponibilidade(dia.getValue()), null));
        HBox disponibilidade = new HBox(8, dia, porDisponibilidade);
        disponibilidade.setAlignment(Pos.CENTER_LEFT);

        Label rotuloData = new Label("Data e hora do agendamento (AAAA-MM-DDTHH:MM)");
        dataHora.setPromptText("2026-12-01T14:00");
        dataHora.setMaxWidth(470);

        mensagem.setStyle("-fx-text-fill: #333333;");
        mensagem.setWrapText(true);

        if (resultados.getChildren().isEmpty()) {
            resultados.getChildren().add(new Label("Use os campos acima para buscar médicos."));
        }

        VBox coluna = new VBox(10, titulo, termo, botoes, disponibilidade, rotuloData, dataHora,
                mensagem, resultados);
        coluna.setPrefWidth(500);
        return coluna;
    }

    private VBox colunaAgendamentos() {
        Label tituloProximos = titulo("Próximos agendamentos");
        Label tituloHistorico = titulo("Histórico de atendimentos");
        VBox coluna = new VBox(10, tituloProximos, proximos, tituloHistorico, historico);
        coluna.setPrefWidth(440);
        return coluna;
    }

    private HBox rodape() {
        Button sair = new Button("Sair");
        sair.setOnAction(e -> Navegacao.irPara(new TelaInicial().criar()));
        HBox rodape = new HBox(sair);
        rodape.setAlignment(Pos.CENTER_RIGHT);
        rodape.setPadding(new Insets(10, 24, 16, 24));
        return rodape;
    }

    private void pesquisar(List<Medico> medicos, String termoExame) {
        resultados.getChildren().clear();
        mensagem.setStyle("-fx-text-fill: #333333;");
        mensagem.setText(medicos.size() + " médico(s) encontrado(s).");
        if (medicos.isEmpty()) {
            resultados.getChildren().add(new Label("Nenhum médico encontrado."));
            return;
        }
        for (Medico medico : medicos) {
            Label informacoes = new Label(resumo(medico));
            informacoes.setWrapText(true);
            Button solicitar = new Button(termoExame == null ? "Solicitar consulta" : "Solicitar exame");
            solicitar.setOnAction(e -> solicitar(medico, termoExame));

            VBox item = new VBox(6, informacoes, solicitar);
            item.setPadding(new Insets(8));
            item.setStyle("-fx-background-color: white; -fx-background-radius: 6;");
            resultados.getChildren().add(item);
        }
    }

    private void solicitar(Medico medico, String termoExame) {
        try {
            if (dataHora.getText().isBlank()) {
                throw new IllegalArgumentException("Informe a data e hora do agendamento.");
            }
            LocalDateTime quando = LocalDateTime.parse(dataHora.getText().trim());
            if (termoExame == null) {
                Navegacao.agendamentoService().solicitarConsulta(paciente, medico, quando);
            } else {
                Exame exame = encontrarExame(medico, termoExame);
                if (exame == null) {
                    throw new IllegalArgumentException("Este médico não oferece o exame pesquisado.");
                }
                Navegacao.agendamentoService().solicitarExame(paciente, medico, exame, quando);
            }
            mensagem.setStyle("-fx-text-fill: #1a7f37;");
            mensagem.setText("Solicitação enviada com sucesso.");
            atualizarAgendamentos();
        } catch (DateTimeParseException ex) {
            mensagem.setStyle("-fx-text-fill: #b00020;");
            mensagem.setText("Data/hora inválida. Use o formato AAAA-MM-DDTHH:MM.");
        } catch (RuntimeException ex) {
            mensagem.setStyle("-fx-text-fill: #b00020;");
            mensagem.setText(ex.getMessage());
        }
    }

    private void atualizarAgendamentos() {
        Paciente atual = Navegacao.pacienteService().buscarPorId(paciente.getId());
        if (atual != null) {
            paciente = atual;
        }

        proximos.getChildren().clear();
        historico.getChildren().clear();

        List<Agendamento> listaProximos = Navegacao.agendamentoService().listarProximosAgendamentos(paciente);
        if (listaProximos.isEmpty()) {
            proximos.getChildren().add(new Label("Nenhum agendamento próximo."));
        }
        for (Agendamento agendamento : listaProximos) {
            proximos.getChildren().add(itemAgendamento(agendamento));
        }

        List<Agendamento> listaHistorico = Navegacao.pacienteService().listarHistorico(paciente);
        if (listaHistorico.isEmpty()) {
            historico.getChildren().add(new Label("Nenhum atendimento no histórico."));
        }
        for (Agendamento agendamento : listaHistorico) {
            historico.getChildren().add(itemAgendamento(agendamento));
        }
    }

    private static Exame encontrarExame(Medico medico, String termo) {
        if (medico.getExamesOferecidos() == null) {
            return null;
        }
        String procurado = normalizar(termo);
        for (Exame exame : medico.getExamesOferecidos()) {
            if (normalizar(exame.getNome()).contains(procurado)) {
                return exame;
            }
        }
        return null;
    }

    private static Label itemAgendamento(Agendamento agendamento) {
        Label item = new Label(descrever(agendamento));
        item.setWrapText(true);
        item.setPadding(new Insets(6));
        item.setStyle("-fx-background-color: white; -fx-background-radius: 6;");
        return item;
    }

    private static String descrever(Agendamento agendamento) {
        String medico = agendamento.getMedico() == null ? "" : texto(agendamento.getMedico().getNome());
        String tipo = agendamento.getTipo() == TipoAgendamento.EXAME ? "Exame" : "Consulta";
        String exame = agendamento.getExame() == null ? "" : " - " + texto(agendamento.getExame().getNome());
        String quando = agendamento.getDataHora() == null ? "" : agendamento.getDataHora().format(FORMATO_DATA);
        String status = agendamento.getStatus() == null ? "" : " | " + agendamento.getStatus().name();
        return medico + " | " + tipo + exame + " | " + quando + status;
    }

    private static String resumo(Medico medico) {
        StringBuilder resumo = new StringBuilder(texto(medico.getNome()));
        if (medico.getCrm() != null) {
            resumo.append(" | CRM ").append(medico.getCrm());
        }
        if (medico.getEspecialidades() != null && !medico.getEspecialidades().isEmpty()) {
            resumo.append("\nEspecialidades: ").append(nomes(medico.getEspecialidades()));
        }
        if (medico.getExamesOferecidos() != null && !medico.getExamesOferecidos().isEmpty()) {
            resumo.append("\nExames: ").append(nomes(medico.getExamesOferecidos()));
        }
        return resumo.toString();
    }

    private static String nomes(List<? extends Atendimento> atendimentos) {
        StringBuilder nomes = new StringBuilder();
        for (int i = 0; i < atendimentos.size(); i++) {
            if (i > 0) {
                nomes.append(", ");
            }
            nomes.append(texto(atendimentos.get(i).getNome()));
        }
        return nomes.toString();
    }

    private static Label titulo(String texto) {
        Label titulo = new Label(texto);
        titulo.setFont(Font.font("System", FontWeight.BOLD, 18));
        return titulo;
    }

    private static String normalizar(String valor) {
        return valor == null ? "" : valor.trim().toLowerCase(Locale.ROOT);
    }

    private static String texto(String valor) {
        return valor == null ? "" : valor;
    }
}
