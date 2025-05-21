package relatorio.example.relatorioDeObras.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class RelatorioObras {
    private String data;
    private String executante;
    private String legal;
    private String rotaEndereco;
    private String descricaoServicos;
    private String tipo;
    private String ct;
    private String caboNumero;
    private String lateralNumero;
    private String atpOsx;
    private String coordenador;
    private Map<String, String> inspecaoServico = new HashMap<>();
    private List<Material> materiaisGastos = new ArrayList<>();

    public static class Material {
        private String descricao;
        private String quantidade;

        // Construtor
        public Material() {}

        // Getters e Setters
        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public String getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(String quantidade) {
            this.quantidade = quantidade;
        }
    }

    // Construtor
    public RelatorioObras() {}

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

    public String getLegal() {
        return legal;
    }

    public void setLegal(String legal) {
        this.legal = legal;
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
        return atpOsx;
    }

    public void setAtpOsx(String atpOsx) {
        this.atpOsx = atpOsx;
    }

    public String getCoordenador() {
        return coordenador;
    }

    public void setCoordenador(String coordenador) {
        this.coordenador = coordenador;
    }

    public Map<String, String> getInspecaoServico() {
        return inspecaoServico;
    }

    public void setInspecaoServico(Map<String, String> inspecaoServico) {
        this.inspecaoServico = inspecaoServico;
    }

    public List<Material> getMateriaisGastos() {
        return materiaisGastos;
    }

    public void setMateriaisGastos(List<Material> materiaisGastos) {
        this.materiaisGastos = materiaisGastos;
    }

    // Método para adicionar item de inspeção
    public void adicionarItemInspecao(String item, String situacao) {
        this.inspecaoServico.put(item, situacao);
    }

    // Método para adicionar material
    public void adicionarMaterialGasto(String descricao, String quantidade) {
        Material material = new Material();
        material.setDescricao(descricao);
        material.setQuantidade(quantidade);
        this.materiaisGastos.add(material);
    }
}