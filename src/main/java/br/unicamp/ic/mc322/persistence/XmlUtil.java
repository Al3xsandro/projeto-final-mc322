package br.unicamp.ic.mc322.persistence;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

final class XmlUtil {

    private XmlUtil() {
    }

    static Document criarDocumento(String raiz) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            doc.appendChild(doc.createElement(raiz));
            return doc;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao criar o XML.", e);
        }
    }

    static Document ler(Path arquivo, String raiz) {
        try {
            if (!Files.exists(arquivo)) {
                return criarDocumento(raiz);
            }
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(arquivo.toFile());
        } catch (Exception e) {
            throw new RuntimeException("Falha ao ler " + arquivo + ".", e);
        }
    }

    static void salvar(Document doc, Path arquivo) {
        try {
            if (arquivo.getParent() != null) {
                Files.createDirectories(arquivo.getParent());
            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(doc), new StreamResult(arquivo.toFile()));
        } catch (Exception e) {
            throw new RuntimeException("Falha ao salvar " + arquivo + ".", e);
        }
    }

    static Element add(Document doc, Element pai, String tag, Object valor) {
        Element elemento = doc.createElement(tag);
        if (valor != null) {
            elemento.setTextContent(valor.toString());
        }
        pai.appendChild(elemento);
        return elemento;
    }

    static Element filho(Element pai, String tag) {
        for (Node n = pai.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n instanceof Element elemento && elemento.getTagName().equals(tag)) {
                return elemento;
            }
        }
        return null;
    }

    static List<Element> filhos(Element pai, String tag) {
        List<Element> elementos = new ArrayList<>();
        for (Node n = pai.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n instanceof Element elemento && elemento.getTagName().equals(tag)) {
                elementos.add(elemento);
            }
        }
        return elementos;
    }

    static String texto(Element pai, String tag) {
        Element elemento = filho(pai, tag);
        if (elemento == null) {
            return null;
        }
        String valor = elemento.getTextContent();
        return valor == null || valor.isEmpty() ? null : valor;
    }

    static LocalDate data(String valor) {
        return valor == null ? null : LocalDate.parse(valor);
    }

    static LocalDateTime dataHora(String valor) {
        return valor == null ? null : LocalDateTime.parse(valor);
    }

    static LocalTime hora(String valor) {
        return valor == null ? null : LocalTime.parse(valor);
    }

    static boolean logico(String valor) {
        return valor != null && Boolean.parseBoolean(valor);
    }
}
