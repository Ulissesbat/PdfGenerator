package relatorio.example.relatorioDeObras.entities;

import java.util.LinkedHashMap;
import java.util.Map;

public class RelatorioObras {
	private String data;
	private String executante;
	private String rotaEndereco;
	private String descricaoServicos;
	private String legal;
	private String tipo;
	private String ct;
	private String caboNumero;
	private String lateralNumero;
	private String AtpOsx;
	private Map<String, String> inspecaoServico;
	private Map<String, String> materiaisGastos;
	private String coordenador;

	public RelatorioObras() {
		inspecaoServico = new LinkedHashMap<>();
		materiaisGastos = new LinkedHashMap<>();
	}

	// Getters e Setters
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getExecutante() {
		return executante;
	}

	public void setExecutante(String executante) {
		this.executante = executante;
	}

	public String getRotaEndereco() {
		return rotaEndereco;
	}

	public void setRotaEndereco(String rotaEndereco) {
		this.rotaEndereco = rotaEndereco;
	}

	public String getDescricaoServicos() {
		return descricaoServicos;
	}

	public void setDescricaoServicos(String descricaoServicos) {
		this.descricaoServicos = descricaoServicos;
	}

	public String getLegal() {
		return legal;
	}

	public void setLegal(String legal) {
		this.legal = legal;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	public String getCaboNumero() {
		return caboNumero;
	}

	public void setCaboNumero(String caboNumero) {
		this.caboNumero = caboNumero;
	}

	public String getLateralNumero() {
		return lateralNumero;
	}

	public void setLateralNumero(String lateralNumero) {
		this.lateralNumero = lateralNumero;
	}


	public String getAtpOsx() {
		return AtpOsx;
	}

	public void setAtpOsx(String atpOsx) {
		AtpOsx = atpOsx;
	}

	public Map<String, String> getInspecaoServico() {
		return inspecaoServico;
	}

	public void adicionarItemInspecao(String item, String situacao) {
		inspecaoServico.put(item, situacao);
	}

	public Map<String, String> getMateriaisGastos() {
		return materiaisGastos;
	}

	public void adicionarMaterialGasto(String descricao, String quantidade) {
		materiaisGastos.put(descricao, quantidade);
	}

	public String getCoordenador() {
		return coordenador;
	}

	public void setCoordenador(String coordenador) {
		this.coordenador = coordenador;
	}
}
