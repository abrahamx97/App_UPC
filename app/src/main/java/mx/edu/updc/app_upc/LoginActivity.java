package mx.edu.updc.app_upc;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    EditText txtUsuario, txtContrasena;

    private Handler puente = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Toast.makeText(getApplicationContext(), msg.obj.toString(),
            Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsuario = (EditText) findViewById(R.id.textUsuario);
        txtContrasena = (EditText) findViewById(R.id.textContrasena);


    }

    public void login(View v){
        if (txtUsuario.getText().length()==0){
            txtUsuario.requestFocus();
            //SALIR DEL EVENTO
        }

        if (txtContrasena.getText().length()==0){
            txtContrasena.requestFocus();
            //salir del evento
        }

        String usuario = txtUsuario.getText().toString() ;
        String contrasena = txtContrasena.getText().toString();

        enviarDatos(usuario,contrasena);


        txtUsuario.setText("");
        txtContrasena.setText("");

    }


    private void enviarDatos(String usuario, String contrasena){
        AsyncHttpClient client = new AsyncHttpClient();
        String url="http://http://192.168.43.204/login.php";
        String parametros = "usuario="+usuario+"&contrasena"+contrasena;
        client.post(url + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statuscode, Header[] headers, byte[] responseBody) {
                if (statuscode==200){
                    String resultado = new String(responseBody);
                    Toast.makeText(LoginActivity.this,"Ok: "+resultado, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

}
