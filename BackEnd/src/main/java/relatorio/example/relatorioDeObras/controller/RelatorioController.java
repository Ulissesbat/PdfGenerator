package relatorio.example.relatorioDeObras.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.http.ContentDisposition;
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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            logger.info("Iniciando geração de relatório...");
            
            // Validação simplificada
            if (dados.getInspecaoServico() == null || dados.getCoordenador() == null) {
                logger.warning("Dados incompletos recebidos");
                return ResponseEntity.badRequest().body("Dados incompletos".getBytes());
            }

            // Geração do PDF
            PdfGenerator.gerarPDF(dados, baos);
            
            logger.info("PDF gerado com sucesso. Tamanho: " + baos.size() + " bytes");

            // Configuração do response com headers corretos
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                ContentDisposition.builder("attachment")
                    .filename("relatorio_obras.pdf")
                    .build());
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
            
        } catch (DocumentException e) {
            logger.severe("Erro no documento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(("Erro ao gerar PDF: " + e.getMessage()).getBytes());
        } catch (IOException e) {
            logger.severe("Erro de IO: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(("Erro de sistema: " + e.getMessage()).getBytes());
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                logger.warning("Erro ao fechar stream: " + e.getMessage());
            }
        }
    }
}