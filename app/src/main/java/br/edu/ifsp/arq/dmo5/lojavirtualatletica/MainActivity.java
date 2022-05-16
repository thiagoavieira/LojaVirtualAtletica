package br.edu.ifsp.arq.dmo5.lojavirtualatletica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;

import br.edu.ifsp.arq.dmo5.lojavirtualatletica.model.UsuarioComEndereco;
import br.edu.ifsp.arq.dmo5.lojavirtualatletica.viewmodel.UsuarioViewModel;

public class MainActivity extends AppCompatActivity {

    private LinearLayout produtoItem;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView txtTitulo;
    private TextView txtLogin;
    private ImageView imagePerfil;

    private UsuarioViewModel usuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioViewModel = new ViewModelProvider(this)
                .get(UsuarioViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txtTitulo = findViewById(R.id.toolbar_titulo);
        txtTitulo.setText(getString(R.string.app_name));


        drawerLayout = findViewById(R.id.nav_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.toogle_open,
                R.string.toogle_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                switch(item.getItemId()){
                    case R.id.nav_home:
                        intent = new Intent(MainActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_account:
                        intent = new Intent(MainActivity.this,
                                UsuarioPerfilActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_categoria:
                        Toast.makeText(MainActivity.this, "Categorias",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_pedidos:
                        Toast.makeText(MainActivity.this, "Pedidos",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_carrinho:
                        Toast.makeText(MainActivity.this, "Carrinho de Compras",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_settings:
                        intent = new Intent(MainActivity.this,
                                SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_logout:
                        usuarioViewModel.logout();
                        finish();
                        startActivity(getIntent());
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        produtoItem = findViewById(R.id.ll_produto_item);
        produtoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        ProdutoDetalheActivity.class);
                startActivity(intent);
            }
        });

        txtLogin = navigationView.getHeaderView(0)
                .findViewById(R.id.header_profile_name);
        txtLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        UsuarioLoginActivity.class);
                startActivity(intent);
            }
        });
        imagePerfil = navigationView.getHeaderView(0).findViewById(R.id.header_profile_image);
    }

    @Override
    protected void onResume() {
        super.onResume();
        usuarioViewModel.isLogged().observe(this, new Observer<UsuarioComEndereco>() {
            @Override
            public void onChanged(UsuarioComEndereco usuarioComEndereco) {
                if(usuarioComEndereco != null){
                    txtLogin.setText(usuarioComEndereco.getUsuario().getNome()
                     + " " + usuarioComEndereco.getUsuario().getSobrenome());
                    String perfilImage = PreferenceManager
                            .getDefaultSharedPreferences(MainActivity.this)
                            .getString(MediaStore.EXTRA_OUTPUT, null);
                    if(perfilImage != null){
                        imagePerfil.setImageURI(Uri.parse(perfilImage));
                    }else{
                        imagePerfil.setImageResource(R.drawable.profile_image);
                    }
                }
            }
        });{

        }
    }

    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}