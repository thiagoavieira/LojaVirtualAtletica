package br.edu.ifsp.arq.dmo5.lojavirtualatletica.model;


import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

public class UsuarioComEndereco {

    @Embedded
    private Usuario usuario;
    @Relation(
            parentColumn = "id",
            entityColumn = "usuarioId"
    )
    private List<Endereco> enderecos;

    public UsuarioComEndereco() {
        enderecos = new ArrayList<>();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }
}
