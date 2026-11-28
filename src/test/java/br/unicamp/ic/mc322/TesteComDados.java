package br.unicamp.ic.mc322;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Fução criada para preservar o conteúdo dos arquivos XML antes de cada teste e o restaura depois,
 * evitando que os testes alterem os dados reais da aplicação.
 */
public abstract class TesteComDados {

    private static final List<Path> ARQUIVOS = List.of(
            Path.of("data", "pacientes.xml"),
            Path.of("data", "medicos.xml"),
            Path.of("data", "agendamentos.xml"));

    private final Map<Path, byte[]> backup = new HashMap<>();

    @BeforeEach
    protected void preservarDados() {
        backup.clear();
        for (Path arquivo : ARQUIVOS) {
            try {
                if (Files.exists(arquivo)) {
                    backup.put(arquivo, Files.readAllBytes(arquivo));
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @AfterEach
    protected void restaurarDados() {
        for (Path arquivo : ARQUIVOS) {
            try {
                byte[] conteudo = backup.get(arquivo);
                if (conteudo == null) {
                    Files.deleteIfExists(arquivo);
                } else {
                    Files.createDirectories(arquivo.getParent());
                    Files.write(arquivo, conteudo);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
