package br.edu.ifsp.arq.dmo5.lojavirtualatletica;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.edu.ifsp.arq.dmo5.lojavirtualatletica.model.Endereco;
import br.edu.ifsp.arq.dmo5.lojavirtualatletica.model.UsuarioComEndereco;
import br.edu.ifsp.arq.dmo5.lojavirtualatletica.viewmodel.UsuarioViewModel;

public class UsuarioPerfilActivity extends AppCompatActivity {

    private final int REQUEST_TAKE_PHOTO = 1;

    private Toolbar toolbar;
    private TextView txtTitulo;
    private TextInputEditText txtNome;
    private TextInputEditText txtSobrenome;
    private TextInputEditText txtEmail;
    private TextInputEditText txtLogradouro;
    private TextInputEditText txtNumero;
    private TextInputEditText txtComplemento;
    private TextInputEditText txtCidade;
    private TextInputEditText txtCep;
    private Spinner spnUf;
    private Button btnAtualizar;
    private ImageView imagePerfil;

    private Uri photoURI;

    private UsuarioViewModel usuarioViewModel;

    private UsuarioComEndereco usuarioComEndereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_perfil);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txtTitulo = findViewById(R.id.toolbar_titulo);
        txtTitulo.setText(R.string.usuario_perfil_titulo);
        txtNome = findViewById(R.id.txt_edit_perfil_nome);
        txtSobrenome = findViewById(R.id.txt_edit_perfil_sobrenome);
        txtEmail = findViewById(R.id.txt_edit_perfil_email);
        txtLogradouro = findViewById(R.id.txt_edit_perfil_logradouro);
        txtNumero = findViewById(R.id.txt_edit_perfil_numero);
        txtComplemento = findViewById(R.id.txt_edit_perfil_complemento);
        txtCidade = findViewById(R.id.txt_edit_perfil_cidade);
        txtCep = findViewById(R.id.txt_edit_perfil_cep);
        spnUf = findViewById(R.id.sp_uf);
        imagePerfil = findViewById(R.id.iv_perfil_imagem);
        imagePerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        String perfilImage = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(MediaStore.EXTRA_OUTPUT, null);

        if(perfilImage != null){
            photoURI = Uri.parse(perfilImage);
            imagePerfil.setImageURI(photoURI);
        }else{
            imagePerfil.setImageResource(R.drawable.profile_image);
        }

        usuarioViewModel = new ViewModelProvider(this)
                .get(UsuarioViewModel.class);

        usuarioViewModel.isLogged().observe(this, new Observer<UsuarioComEndereco>() {
            @Override
            public void onChanged(UsuarioComEndereco usuarioComEndereco) {
                if(usuarioComEndereco != null){
                    UsuarioPerfilActivity.this.usuarioComEndereco = usuarioComEndereco;
                    txtNome.setText(usuarioComEndereco.getUsuario().getNome());
                    txtSobrenome.setText(usuarioComEndereco.getUsuario().getSobrenome());
                    txtEmail.setText(usuarioComEndereco.getUsuario().getEmail());

                    if(usuarioComEndereco.getEnderecos().size() > 0){
                        Endereco endereco = usuarioComEndereco.getEnderecos().get(0);
                        txtLogradouro.setText(endereco.getLogradouro());
                        txtNumero.setText(endereco.getNumero());
                        txtComplemento.setText(endereco.getComplemento());
                        txtCidade.setText(endereco.getCidade());
                        txtCep.setText(endereco.getCep());
                        String[] uf = getResources().getStringArray(R.array.uf);
                        for (int i = 0; i < uf.length; i++){
                            if(uf[i].equals(endereco.getEstado())){
                                spnUf.setSelection(i);
                            }
                        }
                    }
                }else{
                    startActivity(new Intent(UsuarioPerfilActivity.this,
                            UsuarioLoginActivity.class));
                    finish();
                }
            }
        });

        btnAtualizar = findViewById(R.id.btn_usuario_atualizar);
        btnAtualizar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            photoURI = FileProvider.getUriForFile(this,
                    "br.edu.ifsp.arq.dmo5.lojavirtualatletica.fileprovider",
                    photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }
    }

    private File createImageFile() throws IOException{
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile("PERFIL_" + timestamp, ".jpg", storageDir);
    }

    private void update(){
        if(!validate()){
            return;
        }
        usuarioComEndereco.getUsuario().setNome(txtNome.getText().toString());
        usuarioComEndereco.getUsuario().setSobrenome(txtSobrenome.getText().toString());
        usuarioComEndereco.getUsuario().setEmail(txtEmail.getText().toString());

        usuarioViewModel.updateUsuario(usuarioComEndereco.getUsuario());

        if(usuarioComEndereco.getEnderecos().isEmpty()){
            Endereco endereco = new Endereco(
                    usuarioComEndereco.getUsuario().getId(),
                    txtLogradouro.getText().toString(),
                    txtComplemento.getText().toString(),
                    txtNumero.getText().toString(),
                    getResources().getStringArray(R.array.uf)
                            [spnUf.getSelectedItemPosition()],
                    txtCidade.getText().toString(),
                    txtCep.getText().toString(),
                    "Brasil"
            );
            usuarioViewModel.createEndereco(endereco);
        }else{
            Endereco endereco = usuarioComEndereco.getEnderecos().get(0);
            endereco.setLogradouro(txtLogradouro.getText().toString());
            endereco.setNumero(txtNumero.getText().toString());
            endereco.setComplemento(txtComplemento.getText().toString());
            endereco.setCidade(txtCidade.getText().toString());
            endereco.setEstado(getResources().getStringArray(R.array.uf)
                [spnUf.getSelectedItemPosition()]);
            endereco.setCep(txtCep.getText().toString());
            usuarioViewModel.updateEndereco(endereco);
        }
        Toast.makeText(this, R.string.msg_perfil_sucesso,
                Toast.LENGTH_SHORT).show();
    }

    private boolean validate(){
        boolean isValid = true;
        if(txtNome.getText().toString().trim().isEmpty()){
            txtNome.setError("Preencha o campo nome");
            isValid = false;
        }else{
            txtNome.setError(null);
        }
        if(txtSobrenome.getText().toString().trim().isEmpty()){
            txtSobrenome.setError("Preencha o campo sobrenome");
            isValid = false;
        }else{
            txtSobrenome.setError(null);
        }
        if(txtEmail.getText().toString().trim().isEmpty()){
            txtEmail.setError("Preencha o campo email");
            isValid = false;
        }else{
            txtEmail.setError(null);
        }
        if(txtLogradouro.getText().toString().trim().isEmpty()){
            txtLogradouro.setError("Preencha o campo logradouro");
            isValid = false;
        }else{
            txtLogradouro.setError(null);
        }
        if(txtNumero.getText().toString().trim().isEmpty()){
            txtNumero.setError("Preencha o campo numero");
            isValid = false;
        }else{
            txtNumero.setError(null);
        }
        if(txtComplemento.getText().toString().trim().isEmpty()){
            txtComplemento.setError("Preencha o campo complemento");
            isValid = false;
        }else{
            txtComplemento.setError(null);
        }
        if(txtCidade.getText().toString().trim().isEmpty()){
            txtCidade.setError("Preencha o campo cidade");
            isValid = false;
        }else{
            txtCidade.setError(null);
        }
        if(txtCep.getText().toString().trim().isEmpty()){
            txtCep.setError("Preencha o campo CEP");
            isValid = false;
        }else{
            txtCep.setError(null);
        }
        return isValid;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit().putString(MediaStore.EXTRA_OUTPUT, photoURI.toString())
                .apply();
        imagePerfil.setImageURI(photoURI);
    }
}