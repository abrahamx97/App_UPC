package mx.edu.updc.app_upc;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;

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

        final String usuario = txtUsuario.getText().toString() ;
        final String contrasena = txtContrasena.getText().toString();

        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                enviarDatos(usuario, contrasena);
            }
        });

        hilo.start();



        txtUsuario.setText("");
        txtContrasena.setText("");

    }
    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

    private void enviarDatos(String usuario, String contrasena){

        try{
            URL url = new URL("http://www.android.com/");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try{
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                readStream(in);
            }finally {
                urlConnection.disconnect();
            }

        }catch (Exception e){

        }


    }

}
