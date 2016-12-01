package mx.edu.updc.app_upc;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
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

    public final int activo=1;
    public final int baja=0;
    EditText txtContrasena;
    AutoCompleteTextView txtUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsuario = (AutoCompleteTextView) findViewById(R.id.textUsuario);
        txtContrasena = (EditText) findViewById(R.id.textContrasena);

    }

    public void login(View v){
        String usuario = txtUsuario.getText().toString() ;
        String contrasena = txtContrasena.getText().toString();

        if (usuario.trim().length()==0 ){
            txtUsuario.requestFocus();
        }else if (contrasena.trim().length()==0){
            txtContrasena.requestFocus();
        }else {
            conectar(usuario, contrasena);
        }

    }


    private void conectar(String usuario, String contrasena){
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
                    int encontrado=1;
                    if (response.getInt("estado")==encontrado){
                        if (response.getInt("activo")==activo) {
                            int id_profesor = response.getInt("id_profesor");

                        }else if (response.getInt("activo")==baja){
                            //SI EL USUARIO ESTA DADO DE BAJA
                        }
                    }else{
                        //NO SE ENCONTRO USUARIO
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                //NO SE RETORNO UN JSON VALIDO
            }
        });


    }

}
