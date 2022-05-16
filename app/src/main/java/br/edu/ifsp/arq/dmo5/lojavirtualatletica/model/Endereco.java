package br.edu.ifsp.arq.dmo5.lojavirtualatletica.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "endereco")
public class Endereco implements Serializable {

    @NonNull
    @PrimaryKey
    private String id;
    private String usuarioId;
    private String logradouro;
    private String complemento;
    private String numero;
    private String estado;
    private String cidade;
    private String cep;
    private String pais;

    public Endereco(String usuarioId, String logradouro, String complemento, String numero,
                    String estado, String cidade, String cep, String pais) {
        this.id = UUID.randomUUID().toString();
        this.usuarioId = usuarioId;
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.numero = numero;
        this.estado = estado;
        this.cidade = cidade;
        this.cep = cep;
        this.pais = pais;
    }

    @Ignore
    public Endereco() {
        this("","","","","","","","");
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return id.equals(endereco.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
