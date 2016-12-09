package mx.edu.updc.app_upc;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity{


    private EditText txtContrasena;
    private AutoCompleteTextView txtUsuario;
    private Button btn;
    private final String url="http://192.168.43.204/appupc/login.php";;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*DataBaseManager dbManager = new DataBaseManager(this);
        dbManager.insertarMaestro("1","jose","JOSE1","12345");*/

        txtUsuario = (AutoCompleteTextView) findViewById(R.id.textUsuario);
        txtContrasena = (EditText) findViewById(R.id.textContrasena);

        btn = (Button) findViewById(R.id.boton_Ingresar);
        final Intent sesionActivity = new Intent(this, SesionActivity.class);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtUsuario.getText().toString().trim().length()==0 ){
                    txtUsuario.requestFocus();
                }else if (txtContrasena.getText().toString().trim().length()==0){
                    txtContrasena.requestFocus();
                }else {
                    if (checkDataBase()){
                        startActivity(sesionActivity);
                    }else{
                        UPClient upClient = new UPClient();
                        RequestParams rq=new RequestParams();
                        rq.put("usuario",txtUsuario.getText().toString());
                        rq.put("contrasena",txtContrasena.getText().toString());
                        upClient.postConnection(url,rq, getApplicationContext());
                    }
                }
            }
        });

    }

    public  boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{

            String myPath = DataBaseHelper.DATA_BASE_PATH + DataBaseHelper.NOMBRE_BASE_DATOS;
            File database = new File(myPath);
            if(database.exists() && !database.isDirectory()) {
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            }
        }catch(SQLiteException e){
            //si llegamos aqui es porque la base de datos no existe todavía.
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }


    /*sesionActivity.putExtra("maestro", maestro);
      sesionActivity.putExtra("grupos",grupos);
      finish();  //limpia la actividad de la memoria para no tenerla en pila*/

}
