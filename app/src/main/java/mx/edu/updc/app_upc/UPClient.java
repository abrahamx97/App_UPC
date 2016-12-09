package mx.edu.updc.app_upc;

import android.content.Context;
import android.content.Intent;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;

/**
 * Created by root on 2/12/16.
 */

public class UPClient{

    private AsyncHttpClient client = new AsyncHttpClient();
    private final int activo=1;

    private void cargarDatosMaestro(JSONObject data, DataBasesOperation db){
        try{
            db.getDb().beginTransaction();  //Se incia la transaccion

            db.insertarMaestro(null,data.getString("id_maestro"),data.getString("maestro"),  //se inserta el maestro
                    data.getString("usuario"),data.getString("contrasena"));

            JSONArray grupos = data.optJSONArray("grupos");
            JSONObject grupo, alumno;
            JSONArray alumnos;
            for (int i=0; i<grupos.length(); i++){
                grupo = grupos.getJSONObject(i);

                db.insertarGrupo(grupo.getString("id_grupo_materia"), grupo.getString("id_grupo"),
                                grupo.getString("id_materia"), grupo.getString("nombre_grupo"));
                alumnos = grupo.getJSONArray("alumnos");
                for (int j=0; j<alumnos.length(); j++){
                    alumno = alumnos.getJSONObject(j);
                    db.insertarAlumno(null, alumno.getString("id_alumno"), grupo.getString("id_grupo_materia"),
                            alumno.getString("nombre_alumno"),alumno.getString("matricula"),alumno.getString("id_programa"),
                            alumno.getString("alumno_activo"));
                }
            }
            db.getDb().setTransactionSuccessful();  //se finaliza exitosamente la transaccion
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            db.getDb().endTransaction();    //se finaliza la transaccion
        }

    }



    public void postConnection(String URL, RequestParams params, final Context context){
        client.post(URL, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int encontrado=1;
                    if (response.getInt("logeado")==encontrado && response.getInt("activo")==activo){ //SE ENCONTRO AL USUARIO
                        Intent sesionActivity = new Intent(context, SesionActivity.class);

                        DataBasesOperation dbOperation = new DataBasesOperation(context);

                        cargarDatosMaestro(response,dbOperation);

                        sesionActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //sesionActivity.putExtra("id_maestro",response.getString("id_maestro"));
                        context.startActivity(sesionActivity);

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
                if(throwable.getCause() instanceof ConnectTimeoutException){
                    System.out.printf("Tiempo de respuesta superado");
                }
            }
        }) ;
    }

}
