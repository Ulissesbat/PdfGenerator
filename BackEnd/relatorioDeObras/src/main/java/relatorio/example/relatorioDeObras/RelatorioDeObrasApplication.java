package relatorio.example.relatorioDeObras;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import relatorio.example.relatorioDeObras.entities.PdfGenerator;
import relatorio.example.relatorioDeObras.entities.RelatorioObras;

@SpringBootApplication
public class RelatorioDeObrasApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RelatorioDeObrasApplication.class, args);
	}

	@Override
	public void run(String... args) {
		   Scanner scanner = new Scanner(System.in);
	        RelatorioObras relatorio = new RelatorioObras();
	        
	        System.out.println("=== PREENCHIMENTO DE RELATÓRIO DE OBRAS ===");
	        
	        // Preenchendo dados básicos
	        System.out.print("Data (DD/MM/AAAA): ");
	        relatorio.setData(scanner.nextLine());
	        
	        System.out.print("Executante: ");
	        relatorio.setExecutante(scanner.nextLine());
	        
	        System.out.print("Rota/Endereço: ");
	        relatorio.setRotaEndereco(scanner.nextLine());
	        
	        System.out.print("Descrição dos Serviços Executados: ");
	        relatorio.setDescricaoServicos(scanner.nextLine());
	        
	        System.out.print("Legal: ");
	        relatorio.setLegal(scanner.nextLine());
	        
	        System.out.print("Tipo: ");
	        relatorio.setTipo(scanner.nextLine());
	        
	        System.out.print("CT: ");
	        relatorio.setCt(scanner.nextLine());
	        
	        System.out.print("Cabo#: ");
	        relatorio.setCaboNumero(scanner.nextLine());
	        
	        System.out.print("Lateral#: ");
	        relatorio.setLateralNumero(scanner.nextLine());
	        
	        System.out.print("ATP / OSX: ");
	        relatorio.setAtpOsx(scanner.nextLine());
	        
	        // Preenchendo inspeção do serviço
	        System.out.println("\n=== INSPEÇÃO DO SERVIÇO ===");
	        System.out.println("Preencha com C (Conforme), NC (Não Conforme) ou NA (Não Aplicável)");
	        
	        String[] itensInspecao = {
	            "Cabo aéreo espinado",
	            "Cabo subterrâneo lançado e tamponado",
	            "Verificado a pressurização do cabo",
	            "Instalação/transferência de linha feitas no padrão",
	            "Linha terra executada vinculação",
	            "Pavimento refeito",
	            "Medição do comprimento do cabo conferida",
	            "Limpeza e retirada de sobras no local"
	        };
	        
	        for (String item : itensInspecao) {
	            System.out.print(item + ": ");
	            relatorio.adicionarItemInspecao(item, scanner.nextLine().toUpperCase());
	        }
	        
	        // Preenchendo materiais gastos
	        System.out.println("\n=== MATERIAIS GASTOS ===");
	        System.out.println("Digite 'fim' para terminar");
	        
	        while (true) {
	            System.out.print("Descrição do material (ou 'fim'): ");
	            String descricao = scanner.nextLine();
	            if (descricao.equalsIgnoreCase("fim")) break;
	            
	            System.out.print("Quantidade: ");
	            String quantidade = scanner.nextLine();
	            
	            relatorio.adicionarMaterialGasto(descricao, quantidade);
	        }
	        
	        System.out.print("Nome do Coordenador Op. Telecom: ");
	        relatorio.setCoordenador(scanner.nextLine());
	        
	        // Gerando PDF
	        System.out.print("\nNome do arquivo PDF para salvar (sem extensão): ");
	        String nomeArquivo = scanner.nextLine();
	        
	        PdfGenerator.gerarPDF(relatorio, nomeArquivo + ".pdf");
	        System.out.println("Relatório gerado com sucesso!");
	        
	        scanner.close();
	}
}

