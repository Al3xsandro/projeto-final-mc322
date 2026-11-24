package br.unicamp.ic.mc322.telas;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import br.unicamp.ic.mc322.model.Agendamento;
import br.unicamp.ic.mc322.model.HorarioDisponivel;
import br.unicamp.ic.mc322.model.Medico;
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

public class PainelMedico {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final Medico medico;

    public PainelMedico(Medico medico) {
        this.medico = medico;
    }

    public Parent criar() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #eef2f7;");
        root.setTop(cabecalho());

        HBox colunas = new HBox(20, colunaAgenda(), colunaPendencias());
        colunas.setPadding(new Insets(20));
        colunas.setAlignment(Pos.TOP_LEFT);
        root.setCenter(colunas);
        root.setBottom(rodape());
        return root;
    }

    private VBox cabecalho() {
        Medico atual = medicoAtual();
        Label nome = new Label("Dr(a). " + texto(atual.getNome()));
        nome.setFont(Font.font("System", FontWeight.BOLD, 24));
        nome.setStyle("-fx-text-fill: white;");
        Label dados = new Label("CRM: " + texto(atual.getCrm()) + "   |   " + texto(atual.getEmail())
                + "   |   " + texto(atual.getTelefone()));
        dados.setStyle("-fx-text-fill: white;");
        Label biografia = new Label(texto(atual.getBiografia()));
        biografia.setStyle("-fx-text-fill: white;");
        biografia.setWrapText(true);

        VBox cabecalho = new VBox(4, nome, dados, biografia);
        cabecalho.setPadding(new Insets(18, 24, 18, 24));
        cabecalho.setStyle("-fx-background-color: #1f6feb;");
        return cabecalho;
    }

    private VBox colunaAgenda() {
        Label titulo = titulo("Agenda");

        VBox horarios = new VBox(6);
        List<HorarioDisponivel> lista = Navegacao.agendaService().listarHorariosDisponiveis(medico);
        if (lista.isEmpty()) {
            horarios.getChildren().add(new Label("Nenhum horário cadastrado."));
        }
        for (HorarioDisponivel horario : lista) {
            Label descricao = new Label(descrever(horario));
            Button remover = new Button("Remover");
            remover.setOnAction(e -> {
                Navegacao.agendaService().removerHorarioDisponivel(medico, horario.getId());
                recarregar();
            });
            HBox linha = new HBox(10, descricao, remover);
            linha.setAlignment(Pos.CENTER_LEFT);
            horarios.getChildren().add(linha);
        }

        ComboBox<DayOfWeek> dia = new ComboBox<>();
        dia.getItems().addAll(DayOfWeek.values());
        dia.setPromptText("Dia da semana");
        TextField inicio = new TextField();
        inicio.setPromptText("Início (08:00)");
        inicio.setPrefWidth(110);
        TextField fim = new TextField();
        fim.setPromptText("Fim (12:00)");
        fim.setPrefWidth(110);

        Label erro = erro();
        Button adicionar = new Button("Adicionar horário");
        adicionar.setOnAction(e -> {
            try {
                if (dia.getValue() == null) {
                    throw new IllegalArgumentException("Selecione o dia da semana.");
                }
                LocalTime horaInicio = LocalTime.parse(inicio.getText().trim());
                LocalTime horaFim = LocalTime.parse(fim.getText().trim());
                if (!horaFim.isAfter(horaInicio)) {
                    throw new IllegalArgumentException("O horário final deve ser maior que o inicial.");
                }
                Navegacao.agendaService().adicionarHorarioDisponivel(
                        medico, new HorarioDisponivel(null, dia.getValue(), horaInicio, horaFim, true));
                recarregar();
            } catch (DateTimeParseException ex) {
                erro.setText("Horário inválido. Use o formato HH:mm.");
            } catch (RuntimeException ex) {
                erro.setText(ex.getMessage());
            }
        });

        HBox formulario = new HBox(8, dia, inicio, fim, adicionar);
        formulario.setAlignment(Pos.CENTER_LEFT);

        VBox coluna = new VBox(10, titulo, horarios, formulario, erro);
        coluna.setPrefWidth(480);
        return coluna;
    }

    private VBox colunaPendencias() {
        Label titulo = titulo("Solicitações pendentes");
        VBox lista = new VBox(8);

        List<Agendamento> pendencias = Navegacao.agendamentoService().listarSolicitacoesPendentes(medico);
        if (pendencias.isEmpty()) {
            lista.getChildren().add(new Label("Nenhuma solicitação pendente."));
        }
        for (Agendamento agendamento : pendencias) {
            Label descricao = new Label(descrever(agendamento));
            descricao.setWrapText(true);
            Button confirmar = new Button("Confirmar");
            confirmar.setOnAction(e -> {
                Navegacao.agendamentoService().confirmarAgendamento(agendamento.getId());
                recarregar();
            });
            Button recusar = new Button("Recusar");
            recusar.setOnAction(e -> {
                Navegacao.agendamentoService().recusarAgendamento(agendamento.getId());
                recarregar();
            });

            VBox item = new VBox(6, descricao, new HBox(8, confirmar, recusar));
            item.setPadding(new Insets(8));
            item.setStyle("-fx-background-color: white; -fx-background-radius: 6;");
            lista.getChildren().add(item);
        }

        VBox coluna = new VBox(10, titulo, lista);
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

    private void recarregar() {
        Medico atual = Navegacao.medicoService().buscarPorId(medico.getId());
        Navegacao.irPara(new PainelMedico(atual != null ? atual : medico).criar());
    }

    private Medico medicoAtual() {
        Medico atual = Navegacao.medicoService().buscarPorId(medico.getId());
        return atual != null ? atual : medico;
    }

    private static String descrever(HorarioDisponivel horario) {
        String dia = horario.getDiaDaSemana() == null ? "" : horario.getDiaDaSemana().toString();
        String inicio = horario.getHoraInicio() == null ? "" : horario.getHoraInicio().toString();
        String fim = horario.getHoraFim() == null ? "" : horario.getHoraFim().toString();
        return dia + "  " + inicio + " - " + fim + (horario.estaDisponivel() ? "" : " (indisponível)");
    }

    private static String descrever(Agendamento agendamento) {
        String paciente = agendamento.getPaciente() == null ? "Paciente" : texto(agendamento.getPaciente().getNome());
        String tipo = agendamento.getTipo() == TipoAgendamento.EXAME ? "Exame" : "Consulta";
        String exame = agendamento.getExame() == null ? "" : " - " + texto(agendamento.getExame().getNome());
        String quando = agendamento.getDataHora() == null ? "" : agendamento.getDataHora().format(FORMATO_DATA);
        return paciente + " | " + tipo + exame + " | " + quando;
    }

    private static Label titulo(String texto) {
        Label titulo = new Label(texto);
        titulo.setFont(Font.font("System", FontWeight.BOLD, 18));
        return titulo;
    }

    private static Label erro() {
        Label erro = new Label();
        erro.setStyle("-fx-text-fill: #b00020;");
        return erro;
    }

    private static String texto(String valor) {
        return valor == null ? "" : valor;
    }
}
