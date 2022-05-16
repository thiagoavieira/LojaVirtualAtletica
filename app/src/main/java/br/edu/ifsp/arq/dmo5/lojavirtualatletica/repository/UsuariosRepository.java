package br.edu.ifsp.arq.dmo5.lojavirtualatletica.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import br.edu.ifsp.arq.dmo5.lojavirtualatletica.database.AppDatabase;
import br.edu.ifsp.arq.dmo5.lojavirtualatletica.database.UsuarioDao;
import br.edu.ifsp.arq.dmo5.lojavirtualatletica.model.Endereco;
import br.edu.ifsp.arq.dmo5.lojavirtualatletica.model.Usuario;
import br.edu.ifsp.arq.dmo5.lojavirtualatletica.model.UsuarioComEndereco;

public class UsuariosRepository {

    private UsuarioDao usuarioDao;

    public UsuariosRepository(Application application) {
        usuarioDao = AppDatabase.getInstance(application).usuarioDao();
    }

    public Usuario login(String email, String senha){
        return usuarioDao.login(email, senha);
    }

    public LiveData<UsuarioComEndereco> loadUsuarioComEndereco(String usuarioId){
        return usuarioDao.loadUsuarioComEndereco(usuarioId);
    }

    public void insert(Usuario usuario){
        usuarioDao.insert(usuario);
    }

    public void insert(Endereco endereco){
        usuarioDao.insert(endereco);
    }

    public void update(Usuario usuario){
        usuarioDao.update(usuario);
    }

    public void update(Endereco endereco){
        usuarioDao.update(endereco);
    }
}
