package com.example.juntardinheiro;

public class Gasto {
    private int gastoId;
    private String cpfUsuario;
    private String tipoGasto;
    private String valor;
    private String data;
    private String descricao;

    // Construtor
    public Gasto(int gastoId, String cpfUsuario, String tipoGasto, String valor, String data, String descricao) {
        this.gastoId = gastoId;
        this.cpfUsuario = cpfUsuario;
        this.tipoGasto = tipoGasto;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
    }

    // Métodos getters e setters
    public int getGastoId() {
        return gastoId;
    }

    public void setGastoId(int gastoId) {
        this.gastoId = gastoId;
    }

    public String getCpfUsuario() {
        return cpfUsuario;
    }

    public void setCpfUsuario(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public String getTipoGasto() {
        return tipoGasto;
    }

    public void setTipoGasto(String tipoGasto) {
        this.tipoGasto = tipoGasto;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

