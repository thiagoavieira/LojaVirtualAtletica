package br.edu.ifsp.arq.dmo5.lojavirtualatletica.viewmodel;

import android.app.Application;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Optional;

import br.edu.ifsp.arq.dmo5.lojavirtualatletica.model.Endereco;
import br.edu.ifsp.arq.dmo5.lojavirtualatletica.model.Usuario;
import br.edu.ifsp.arq.dmo5.lojavirtualatletica.model.UsuarioComEndereco;
import br.edu.ifsp.arq.dmo5.lojavirtualatletica.repository.UsuariosRepository;

public class UsuarioViewModel extends AndroidViewModel {

    public static final String USUARIO_ID = "USUARIO_ID";

    private UsuariosRepository usuariosRepository;

    public UsuarioViewModel(@NonNull Application application) {
        super(application);
        usuariosRepository = new UsuariosRepository(application);
    }

    public void createUsuario(Usuario usuario){
        usuariosRepository.createUsuario(usuario);
    }

    public void createEndereco(Endereco endereco){
        usuariosRepository.insert(endereco);
    }

    public void updateUsuario(Usuario usuario) {
        usuariosRepository.update(usuario);
    }

    public void updateEndereco(Endereco endereco){
        usuariosRepository.update(endereco);
    }

    public MutableLiveData<Usuario> login(String email, String senha){
        Optional<Usuario> usuario =
                Optional.ofNullable(usuariosRepository.login(email, senha));

        if(usuario.isPresent()){
            PreferenceManager.getDefaultSharedPreferences(getApplication())
                    .edit().putString(USUARIO_ID, usuario.get().getId())
                    .apply();
            return new MutableLiveData<>(usuario.get());
        }
        return new MutableLiveData<>(null);
    }

    public void logout(){
        PreferenceManager.getDefaultSharedPreferences(getApplication())
                .edit().remove(USUARIO_ID)
                .apply();
    }

    public LiveData<UsuarioComEndereco> isLogged(){
        Optional<String> id = Optional.ofNullable(
                PreferenceManager.getDefaultSharedPreferences(getApplication())
                        .getString(USUARIO_ID, null));
        if(!id.isPresent()){
            return new MutableLiveData<>(null);
        }
        return usuariosRepository.loadUsuarioComEndereco(id.get());
    }
}
