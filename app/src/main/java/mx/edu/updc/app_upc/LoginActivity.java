package mx.edu.updc.app_upc;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    EditText txtUsuario, txtContrasena;
    TextView textEstado;

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
        textEstado = (TextView) findViewById(R.id.textEstado);

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
        String url="http://192.168.43.204/appupc/login.php";
        RequestParams rq=new RequestParams();
        rq.put("usuario",usuario);
        rq.put("contrasena",contrasena);
        client.post(url, rq, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String estado = "0";
                    if (response.getString("estado").equals("1")) {
                        estado = "1";
                    };
                    textEstado.setText(estado);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                textEstado.setText("No paso");
            }
        });


    }

}
