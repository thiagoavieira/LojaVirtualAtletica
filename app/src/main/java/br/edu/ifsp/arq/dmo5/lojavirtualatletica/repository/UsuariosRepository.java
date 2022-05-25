package br.edu.ifsp.arq.dmo5.lojavirtualatletica.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ifsp.arq.dmo5.lojavirtualatletica.database.AppDatabase;
import br.edu.ifsp.arq.dmo5.lojavirtualatletica.database.UsuarioDao;
import br.edu.ifsp.arq.dmo5.lojavirtualatletica.model.Endereco;
import br.edu.ifsp.arq.dmo5.lojavirtualatletica.model.Usuario;
import br.edu.ifsp.arq.dmo5.lojavirtualatletica.model.UsuarioComEndereco;

public class UsuariosRepository {

    private static final String BASE_URL = "https://identitytoolkit.googleapis.com/v1/";
    private static final String SIGNUP = "accounts:signUp";
    private static final String SIGNIN = "accounts:signInWithPassword";
    private static final String PASSWORD_RESET = "accounts:sendOobCode";
    private static final String KEY = "?key=AIzaSyB6_sQA75g35wW4a5rh9FPNsY7RUWQbmcE";

    private UsuarioDao usuarioDao;

    private FirebaseFirestore firestore;

    private RequestQueue queue;

    public UsuariosRepository(Application application) {
        usuarioDao = AppDatabase.getInstance(application).usuarioDao();
        firestore = FirebaseFirestore.getInstance();
        queue = Volley.newRequestQueue(application);
    }

    public void createUsuario(Usuario usuario){
        JSONObject parametros = new JSONObject();
        try{
            parametros.put("email", usuario.getEmail());
            parametros.put("password", usuario.getSenha());
            parametros.put("returnSecureToken", true);
            parametros.put("Content-Type",
                    "application/json; charset=utf-8");
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + SIGNUP + KEY,
                parametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            usuario.setId(response.getString("localId"));
                            usuario.setSenha(response.getString("idToken"));

                            firestore.collection("usuario")
                                    .document(usuario.getId()).set(usuario)
                                    .addOnSuccessListener(unused -> {
                                        Log.d(this.toString(), "Usuário " +
                                                usuario.getEmail() + " cadastrado com sucesso.");
                                    });
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(this.toString(), error.getMessage());
                    }
                }
        );
        queue.add(request);
    }

    public Usuario login(String email, String senha) {
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
