package relatorio.example.relatorioDeObras.entities;

import java.io.FileOutputStream;
import java.util.Map;
import java.util.UUID;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfGenerator {
	private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
	private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
	private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10);
	private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC);

	public static void gerarPDF(RelatorioObras relatorio, String fileName) {
		Document document = new Document(PageSize.A4.rotate());

		try {
			PdfWriter.getInstance(document, new FileOutputStream(fileName));
			document.open();

			// 1. CABEÇALHO
			PdfPTable headerTable = new PdfPTable(2);
			headerTable.setWidthPercentage(100);
			headerTable.setWidths(new float[] { 70, 30 });

			// Título principal (único)
			PdfPCell titleCell = new PdfPCell(
					new Phrase("ICOMON - RELATÓRIO DE ATIVIDADE E INSPEÇÃO DE SERVIÇOS - CLASSE F / L", TITLE_FONT));
			titleCell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(titleCell);

			// Célula de informações com RG + ID curto
			PdfPCell infoCell = new PdfPCell();
			infoCell.setBorder(Rectangle.NO_BORDER);

			// Gerar ID curto (8 caracteres maiúsculos)
			String shortId = "RG: " + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

			infoCell.addElement(new Phrase(shortId, NORMAL_FONT));
			infoCell.addElement(new Phrase("Data: " + relatorio.getData(), NORMAL_FONT));
			infoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			headerTable.addCell(infoCell);

			document.add(headerTable);
			document.add(Chunk.NEWLINE);

			// 2. DADOS BÁSICOS (4 colunas)
			PdfPTable mainTable = new PdfPTable(4);
			mainTable.setWidthPercentage(100);
			mainTable.setWidths(new float[] { 25, 25, 25, 25 });

			// Linha 1
			addCell(mainTable, "Executante:", HEADER_FONT);
			addCell(mainTable, relatorio.getExecutante(), NORMAL_FONT);
			addCell(mainTable, "Legal:", HEADER_FONT);
			addCell(mainTable, relatorio.getLegal(), NORMAL_FONT);

			// Linha 2
			addCell(mainTable, "Rota / Endereço:", HEADER_FONT);
			addCell(mainTable, relatorio.getRotaEndereco(), NORMAL_FONT, 3);

			// Linha 3
			addCell(mainTable, "Descrição dos Serviços Executados:", HEADER_FONT);
			addCell(mainTable, relatorio.getDescricaoServicos(), NORMAL_FONT, 3);

			// Linha 4
			addCell(mainTable, "Tipo:", HEADER_FONT);
			addCell(mainTable, relatorio.getTipo(), NORMAL_FONT);
			addCell(mainTable, "CT:", HEADER_FONT);
			addCell(mainTable, relatorio.getCt(), NORMAL_FONT);

			// Linha 5
			addCell(mainTable, "Cabo#:", HEADER_FONT);
			addCell(mainTable, relatorio.getCaboNumero(), NORMAL_FONT);
			addCell(mainTable, "ATP / OSX:", HEADER_FONT);
			addCell(mainTable, relatorio.getAtpOsx(), NORMAL_FONT);

			// Linha 6
			addCell(mainTable, "Lateral#:", HEADER_FONT);
			addCell(mainTable, relatorio.getLateralNumero(), NORMAL_FONT);
			addCell(mainTable, "", NORMAL_FONT);
			addCell(mainTable, "", NORMAL_FONT);

			document.add(mainTable);
			document.add(Chunk.NEWLINE);

			// 3. SEÇÃO INSPEÇÃO + MATERIAIS (2 colunas)
			PdfPTable inspectionMaterialsTable = new PdfPTable(2);
			inspectionMaterialsTable.setWidthPercentage(100);
			inspectionMaterialsTable.setWidths(new float[] { 50, 50 });

			// COLUNA DA INSPEÇÃO
			PdfPCell inspectionCell = new PdfPCell();
			inspectionCell.setBorder(Rectangle.BOX);
			inspectionCell.setPadding(3);

			Paragraph inspectionTitle = new Paragraph("INSPEÇÃO DO SERVIÇO",
					new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD));
			inspectionTitle.setAlignment(Element.ALIGN_CENTER);
			inspectionCell.addElement(inspectionTitle);

			PdfPTable checklistTable = new PdfPTable(4);
			checklistTable.setWidthPercentage(100);
			checklistTable.setWidths(new float[] { 60, 10, 10, 10 });

			// Cabeçalhos
			addSmallCell(checklistTable, "Check-List", HEADER_FONT);
			addSmallCell(checklistTable, "C", HEADER_FONT);
			addSmallCell(checklistTable, "NC", HEADER_FONT);
			addSmallCell(checklistTable, "NA", HEADER_FONT);

			// Itens de inspeção com preenchimento dos X
			Map<String, String> inspecaoServico = relatorio.getInspecaoServico();
			String[] itensInspecao = { "Cabo aéreo espinado", "Cabo subterrâneo lançado e tamponado",
					"Verificado a pressurização do cabo", "Instalação/transferência de linha feitas no padrão",
					"Linha terra executada vinculação", "Pavimento refeito", "Medição do comprimento do cabo conferida",
					"Limpeza e retirada de sobras no local" };

			for (String item : itensInspecao) {
				addSmallCell(checklistTable, item, NORMAL_FONT);

				String situacao = inspecaoServico.getOrDefault(item, "");
				addSmallCell(checklistTable, situacao.equals("C") ? "X" : "", NORMAL_FONT);
				addSmallCell(checklistTable, situacao.equals("NC") ? "X" : "", NORMAL_FONT);
				addSmallCell(checklistTable, situacao.equals("NA") ? "X" : "", NORMAL_FONT);
			}

			inspectionCell.addElement(checklistTable);
			inspectionMaterialsTable.addCell(inspectionCell);

			// COLUNA DE MATERIAIS
			PdfPCell materialsCell = new PdfPCell();
			materialsCell.setBorder(Rectangle.BOX);
			materialsCell.setPadding(3);

			Paragraph materialsTitle = new Paragraph("MATERIAL GASTO",
					new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD));
			materialsTitle.setAlignment(Element.ALIGN_CENTER);
			materialsCell.addElement(materialsTitle);

			PdfPTable materialsTable = new PdfPTable(2);
			materialsTable.setWidthPercentage(100);

			addSmallCell(materialsTable, "Descrição", HEADER_FONT);
			addSmallCell(materialsTable, "Qtde.", HEADER_FONT);

			for (Map.Entry<String, String> entry : relatorio.getMateriaisGastos().entrySet()) {
				addSmallCell(materialsTable, entry.getKey(), NORMAL_FONT);
				addSmallCell(materialsTable, entry.getValue(), NORMAL_FONT);
			}

			materialsCell.addElement(materialsTable);
			inspectionMaterialsTable.addCell(materialsCell);

			document.add(inspectionMaterialsTable);
			document.add(Chunk.NEWLINE);

			// 4. RODAPÉ
			Paragraph footer = new Paragraph();
			footer.add(new Phrase("Preencher com Situação: C = Conforme / NC = Não Conforme / NA = Não Aplicável",
					SMALL_FONT));
			footer.add(Chunk.NEWLINE);
			footer.add(new Phrase("Coordenador Op. Telecom: " + relatorio.getCoordenador(), NORMAL_FONT));
			document.add(footer);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			document.close();
		}
	}

	private static void addCell(PdfPTable table, String text, Font font) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setBorder(Rectangle.BOX);
		cell.setPadding(5);
		table.addCell(cell);
	}

	private static void addCell(PdfPTable table, String text, Font font, int colspan) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setBorder(Rectangle.BOX);
		cell.setPadding(5);
		cell.setColspan(colspan);
		table.addCell(cell);
	}

	private static void addSmallCell(PdfPTable table, String text, Font font) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setBorder(Rectangle.BOX);
		cell.setPadding(3);
		cell.setMinimumHeight(15);
		table.addCell(cell);
	}
}