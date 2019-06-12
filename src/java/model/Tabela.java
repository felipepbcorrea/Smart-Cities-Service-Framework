/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author FELIPE
 */
public class Tabela {
    
    private String nometabela;
    private String nomecoluna;
    private String tipo;
    private String tamanho;
    private String nn;
    private String ai;
    private String pk;
    private String fk;
    private String tabelafk;

    public Tabela() {
    }

    public Tabela(String nometabela, String nomecoluna, String tipo, String tamanho, String nn, String ai, String pk, String fk, String tabelafk) {
        this.nometabela = nometabela;
        this.nomecoluna = nomecoluna;
        this.tipo = tipo;
        this.tamanho = tamanho;
        this.nn = nn;
        this.ai = ai;
        this.pk = pk;
        this.fk = fk;
        this.tabelafk = tabelafk;
    }

    public String getNometabela() {
        return nometabela;
    }

    public void setNometabela(String nometabela) {
        this.nometabela = nometabela;
    }

    public String getNomecoluna() {
        return nomecoluna;
    }

    public void setNomecoluna(String nomecoluna) {
        this.nomecoluna = nomecoluna;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getNn() {
        return nn;
    }

    public void setNn(String nn) {
        this.nn = nn;
    }

    public String getAi() {
        return ai;
    }

    public void setAi(String ai) {
        this.ai = ai;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getFk() {
        return fk;
    }

    public void setFk(String fk) {
        this.fk = fk;
    }

    public String getTabelafk() {
        return tabelafk;
    }

    public void setTabelafk(String tabelafk) {
        this.tabelafk = tabelafk;
    }
    
    
    
}
