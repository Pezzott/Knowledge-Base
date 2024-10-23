package com.ford.controller;

import com.ford.model.ProcessoModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProcessoController {

    private final ProcessoModel processoModel = new ProcessoModel();

   
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/listar")
    public String listarArquivos(@RequestParam("processo") String processo, Model model) {
        List<String> arquivos = processoModel.listarArquivos(processo);
        model.addAttribute("arquivos", arquivos);
        model.addAttribute("processo", processo);
        return "listar";
    }

    // Rota para ler o conteúdo de um arquivo específico (usando Apache POI para .docx ou leitura de .txt)
    @GetMapping("/ler")
    public String lerArquivo(@RequestParam("processo") String processo, @RequestParam("arquivo") String arquivo, Model model) {
        try {
            String conteudo;
            if (arquivo.endsWith(".docx")) {
                conteudo = processoModel.lerConteudoDocx(processo, arquivo);
            } else {
                conteudo = processoModel.lerArquivo(processo, arquivo);
            }
            model.addAttribute("conteudo", conteudo);
            model.addAttribute("arquivo", arquivo);
            model.addAttribute("processo", processo);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao ler o arquivo: " + e.getMessage());
        }
        return "conteudo";
    }
}
