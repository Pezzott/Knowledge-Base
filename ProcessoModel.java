package com.ford.model;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProcessoModel {

    private static final String PROCESSOS_DIRECTORY = "src/main/resources/processos";

    // Método para listar arquivos de um diretório específico
    public List<String> listarArquivos(String subDiretorio) {
        List<String> arquivos = new ArrayList<>();
        File diretorio = new File(PROCESSOS_DIRECTORY + "/" + subDiretorio);

        if (diretorio.exists() && diretorio.isDirectory()) {
            for (File arquivo : diretorio.listFiles()) {
                if (arquivo.isFile()) {
                    arquivos.add(arquivo.getName());
                }
            }
        }
        return arquivos;
    }

    // Método para ler o conteúdo de um arquivo .docx usando Apache POI e formatar como HTML
    public String lerConteudoDocx(String subDiretorio, String nomeArquivo) {
        String caminhoArquivo = PROCESSOS_DIRECTORY + "/" + subDiretorio + "/" + nomeArquivo;
        StringBuilder conteudo = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(caminhoArquivo);
             XWPFDocument document = new XWPFDocument(fis)) {

            // Abre uma tag de <div> para encapsular o conteúdo do documento
            conteudo.append("<div class='processo-documento'>");
            
            // Itera sobre os parágrafos do documento
            for (XWPFParagraph para : document.getParagraphs()) {
                String texto = para.getText();

                // Verifica se o conteúdo contém tags HTML e preserva o conteúdo original
                if (texto.trim().startsWith("<") && texto.trim().endsWith(">")) {
                    // Preserva o conteúdo HTML
                    conteudo.append(texto);
                } else {
                    // Se for texto comum, encapsula em tags <p>
                    conteudo.append("<p>").append(texto).append("</p>");
                }
            }
            conteudo.append("</div>");
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao ler o arquivo: " + e.getMessage();
        }

        return conteudo.toString();
    }

    // Método para ler o conteúdo de um arquivo .txt
    public String lerArquivo(String subDiretorio, String nomeArquivo) throws Exception {
        String caminhoArquivo = PROCESSOS_DIRECTORY + "/" + subDiretorio + "/" + nomeArquivo;
        return new String(Files.readAllBytes(Paths.get(caminhoArquivo)));
    }
}
