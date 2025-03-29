package relatorio.example.relatorioDeObras.controller;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;

import relatorio.example.relatorioDeObras.entities.PdfGenerator;
import relatorio.example.relatorioDeObras.entities.RelatorioObras;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
public class RelatorioController {

    private static final Logger logger = Logger.getLogger(RelatorioController.class.getName());

    @PostMapping("/gerar")
    public ResponseEntity<byte[]> gerarRelatorio(@RequestBody RelatorioObras dados) {
        try {
            // Validação dos dados
            if (dados.getMateriaisGastos() == null || dados.getMateriaisGastos().isEmpty()) {
                return ResponseEntity.badRequest().body("Adicione pelo menos um material".getBytes());
            }

            if (dados.getInspecaoServico() == null || !isInspecaoValida(dados.getInspecaoServico())) {
                return ResponseEntity.badRequest().body("Preencha todos os itens de inspeção".getBytes());
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfGenerator.gerarPDF(dados, baos);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "relatorio_obras.pdf");
            
            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);

        } catch (DocumentException | IOException e) {
            logger.severe("Erro ao gerar PDF: " + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage().getBytes());
        }
    }

    private boolean isInspecaoValida(Map<String, String> inspecao) {
        String[] itens = {"caboAereo", "caboSubterraneo", "pressurizacao", 
                         "instalacaoPadrao", "linhaTerra", "pavimento", 
                         "medicao", "limpeza"};
        
        for (String item : itens) {
            if (inspecao.get(item) == null || inspecao.get(item).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}