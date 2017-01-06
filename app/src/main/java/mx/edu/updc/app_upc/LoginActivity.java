package mx.edu.updc.app_upc;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;

public class LoginActivity extends AppCompatActivity {


    public EditText txtContrasena;
    private AutoCompleteTextView txtUsuario;
    private Button boton_ingresar;
    private View progress_view;
    private View login_formView;
    public static final String url = "http://192.168.43.204/appupc/login.php";
    private DataBaseOperation dbOperation = new DataBaseOperation(this);

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            login_formView.setVisibility(show ? View.GONE : View.VISIBLE);
            login_formView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    login_formView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progress_view.setVisibility(show ? View.VISIBLE : View.GONE);
            progress_view.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progress_view.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progress_view.setVisibility(show ? View.VISIBLE : View.GONE);
            progress_view.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        txtUsuario = (AutoCompleteTextView) findViewById(R.id.textUsuario);
        txtContrasena = (EditText) findViewById(R.id.textContrasena);
        progress_view = findViewById(R.id.login_progress);
        login_formView =  findViewById(R.id.scroll_login_form);
        boton_ingresar = (Button) findViewById(R.id.boton_Ingresar);
        final Intent sesionActivity = new Intent(this, GruposActivity.class);
        boton_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View focusView = null;
                txtUsuario.setError(null);
                txtContrasena.setError(null);
                String usuario = txtUsuario.getText().toString();
                String contrasena = txtContrasena.getText().toString();
                boolean cancelar = false;

                if (TextUtils.isEmpty(usuario)) {
                    txtUsuario.setError("Este campo es requerido");
                    focusView=txtUsuario;
                    cancelar = true;
                }
                if (TextUtils.isEmpty(contrasena)) {
                    //txtContrasena.requestFocus();
                    txtContrasena.setError("Este campo es requerido");
                    focusView=txtContrasena;
                    cancelar = true;
                }
                if (!cancelar) {
                    boolean accessInternet = true;
                    if (!isNetDisponible()) {
                        Toast.makeText(view.getContext(), "Red inhabilitada", Toast.LENGTH_SHORT).show();
                        accessInternet = false;
                    } /*else if (!isOnlineNet()) {
                        Toast.makeText(view.getContext(), "Sin acceso a internet", Toast.LENGTH_SHORT).show();
                        //accessInternet = false;
                    }*/

                    if (checkDataBase()) {
                        showProgress(true);
                        Cursor validar = dbOperation.loginMaestro(usuario.trim(),
                                contrasena);
                        int indiceLogeado = validar.getColumnIndex("logeado");

                        String logeado = "0";
                        for (validar.moveToFirst(); !validar.isAfterLast(); validar.moveToNext()) {
                            logeado = validar.getString(indiceLogeado);
                        }
                        if (logeado.equals("1")) {
                            startActivity(sesionActivity);
                        } else {
                            txtUsuario.setError("Usuario o contraseña incorrectos");
                            txtUsuario.requestFocus();
                            showProgress(false);
                        }
                    } else if (accessInternet) {
                        showProgress(true);
                        UPClient upClient = new UPClient();
                        RequestParams rq = new RequestParams();
                        rq.put("usuario", usuario.trim());
                        rq.put("contrasena", contrasena);
                        upClient.intentConnection(url, rq, getApplicationContext());
                    }
                }else {focusView.requestFocus();}
            }
        });

    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {

            String myPath = DataBaseHelper.DATA_BASE_PATH + DataBaseHelper.NOMBRE_BASE_DATOS;
            File database = new File(myPath);
            if (database.exists() && !database.isDirectory()) {
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            }
        } catch (SQLiteException e) {
            //si llegamos aqui es porque la base de datos no existe todavía.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private boolean isNetDisponible() {
        //comprueba si la red esta habilitada
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }

    public Boolean isOnlineNet() {
        //comprueba si hay acceso a internet
        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");
            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public class UPClient {

        private AsyncHttpClient client = new AsyncHttpClient();

        private void cargarDatosMaestro(JSONObject data, DataBaseOperation db) {
            try {
                db.getDb().beginTransaction();  //Se incia la transaccion

                db.insertarMaestro(null, data.getString("id_maestro"), data.getString("maestro"),  //se inserta el maestro
                        data.getString("usuario"), data.getString("contrasena"));

                JSONArray grupos = data.optJSONArray("grupos");
                JSONObject grupo, alumno;
                JSONArray alumnos;
                for (int i = 0; i < grupos.length(); i++) {
                    grupo = grupos.getJSONObject(i);

                    db.insertarGrupo(grupo.getString("id_grupo_materia"), grupo.getString("id_grupo"),
                            grupo.getString("id_materia"), grupo.getString("nombre_grupo"));
                    alumnos = grupo.getJSONArray("alumnos");
                    for (int j = 0; j < alumnos.length(); j++) {
                        alumno = alumnos.getJSONObject(j);
                        db.insertarAlumno(null, alumno.getString("id_alumno"), grupo.getString("id_grupo_materia"),
                                alumno.getString("nombre_alumno"), alumno.getString("matricula"), alumno.getString("id_programa"),
                                alumno.getString("programa"), alumno.getString("alumno_activo"));
                    }
                }
                db.getDb().setTransactionSuccessful();  //se finaliza exitosamente la transaccion
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                db.getDb().endTransaction();    //se finaliza la transaccion
            }

        }

        public void intentConnection(String URL, RequestParams params, final Context context) {
            client.post(URL, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        int encontrado = 1, activo = 1;
                        if (response.getInt("logeado") == encontrado) { //SE ENCONTRO AL USUARIO
                            if (response.getInt("activo") == activo) {
                                Intent sesionActivity = new Intent(context, GruposActivity.class);
                                DataBaseOperation dbOperation = new DataBaseOperation(context);
                                cargarDatosMaestro(response, dbOperation);
                                sesionActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(sesionActivity);
                            } else {
                                txtUsuario.setError("Usuario dado de baja");
                                txtUsuario.requestFocus();
                                showProgress(false);
                            }

                        } else {
                            txtUsuario.setError("Usuario o contraseña incorrectos");
                            txtUsuario.requestFocus();
                            showProgress(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showProgress(false);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    //NO SE RETORNO UN JSON VALIDO
                    if (throwable.getCause() instanceof ConnectTimeoutException) {
                        System.out.printf("No sé que pasa");
                        showProgress(false);
                    }
                }
            });
        }

    }
}


