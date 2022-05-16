package br.edu.ifsp.arq.dmo5.lojavirtualatletica.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "usuario")
public class Usuario implements Serializable{

    @NonNull
    @PrimaryKey
    private String id;
    private String email;
    private String nome;
    private String sobrenome;
    private String senha;
    private String imagem;

    public Usuario(String email, String nome, String sobrenome, String senha, String imagem) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.senha = senha;
        this.imagem = imagem;
    }

    @Ignore
    public Usuario(){
        this("","","","", "");
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

