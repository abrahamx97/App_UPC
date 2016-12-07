package mx.edu.updc.app_upc;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity{


    private EditText txtContrasena;
    private AutoCompleteTextView txtUsuario;
    private Intent sesionActivity;
    private UPClient upClient = new UPClient();
    private Maestro maestro;
    private ArrayList<Grupo> grupos = new ArrayList<Grupo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsuario = (AutoCompleteTextView) findViewById(R.id.textUsuario);
        txtContrasena = (EditText) findViewById(R.id.textContrasena);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void login(View v) throws JSONException {
        String usuario = txtUsuario.getText().toString() ;
        String contrasena = txtContrasena.getText().toString();

        if (usuario.trim().length()==0 ){
            txtUsuario.requestFocus();
        }else if (contrasena.trim().length()==0){
            txtContrasena.requestFocus();
        }else {
            if (conectar(usuario, contrasena)) {
                sesionActivity = new Intent(this, SesionActivity.class);
                maestro = upClient.getMaestro();
                sesionActivity.putExtra("maestro", maestro);
                grupos = upClient.getGrupos();
                sesionActivity.putExtra("grupos",grupos);
                startActivity(sesionActivity);
                finish();  //limpia la actividad de la memoria para no tenerla en pila
            }
        }

    }


    private boolean conectar(String usuario, String contrasena){
        String url="http://192.168.43.204/appupc/login.php";
        RequestParams rq=new RequestParams();
        rq.put("usuario",usuario);
        rq.put("contrasena",contrasena);

        boolean logeado = upClient.postConnection(url,rq);

        return logeado;
    }

}
