package br.edu.ifsp.arq.dmo5.lojavirtualatletica.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import br.edu.ifsp.arq.dmo5.lojavirtualatletica.model.Endereco;
import br.edu.ifsp.arq.dmo5.lojavirtualatletica.model.Usuario;
import br.edu.ifsp.arq.dmo5.lojavirtualatletica.model.UsuarioComEndereco;

@Dao
public interface UsuarioDao {

    @Query("SELECT * FROM usuario WHERE email = :email AND senha = :senha")
    Usuario login(String email, String senha);

    @Transaction
    @Query("SELECT * FROM usuario WHERE id = :usuarioId")
    LiveData<UsuarioComEndereco> loadUsuarioComEndereco(String usuarioId);

    @Insert
    void insert(Usuario usario);

    @Insert
    void insert(Endereco endereco);

    @Update
    void update(Usuario usuario);

    @Update
    void update(Endereco endereco);

}
