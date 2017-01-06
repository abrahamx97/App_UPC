package mx.edu.updc.app_upc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;

/**
 * Created by root on 16/12/16.
 */

public class GruposFragment extends Fragment {
    private ListView gruposList;
    private FloatingActionButton fab_sincronizar;
    private GruposAdapter gruposAdapter;
    private DataBaseOperation dbOperation;
    private Cursor grupos,asistencia;
    private int indice_id_grupo_materia;
    private int indice_nombre_grupo;
    private static final String url = "http://192.168.43.204/appupc/asistencias_asinc.php";
    public static final SimpleDateFormat senhor_formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public GruposFragment() {
        // Required empty public constructor
    }

    public static GruposFragment newInstance() {
        GruposFragment gruposFragment = new GruposFragment();
        return gruposFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //get arguments
        }
    }


    public void inicializar() {
        dbOperation = new DataBaseOperation(getContext());
        grupos = dbOperation.obtenerGrupos();
        indice_id_grupo_materia = grupos.getColumnIndex(Grupos.ID);
        indice_nombre_grupo = grupos.getColumnIndex(Grupos.NOMBRE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_grupos, container, false);
        gruposList = (ListView) root.findViewById(R.id.lista_grupos);
        gruposList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent alumnosActivity = new Intent(getContext(), AlumnosActivity.class);
                alumnosActivity.putExtra("id_grupo_materia", view.getTag().toString());

                Cursor alumnos = dbOperation.obtenerAlumnos(view.getTag().toString());
                int columna_id_alumno = alumnos.getColumnIndex(Alumnos.ID_ALUMNO);
                String now = senhor_formato.format(new Date());

                if (!dbOperation.existeLista(view.getTag().toString())) {
                    try {
                        dbOperation.getDb().beginTransaction();
                        for (alumnos.moveToFirst(); !alumnos.isAfterLast(); alumnos.moveToNext()) {
                            dbOperation.insertarAsistencias(null, view.getTag().toString(),
                                    alumnos.getString(columna_id_alumno), now, "A", "1");
                        }

                        dbOperation.getDb().setTransactionSuccessful();
                    } finally {
                        dbOperation.getDb().endTransaction();
                    }
                }
                startActivity(alumnosActivity);
            }
        });

        Button pie = new Button(getContext());
        pie.setBackgroundColor(0);
        gruposList.addFooterView(pie);

        fab_sincronizar = (FloatingActionButton)
                root.findViewById(R.id.fab_sincronizar);

        fab_sincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 asistencia = dbOperation.obtenerAsisSincronizar(dbOperation.obtenerMaxSincro());

                int indice_id = asistencia.getColumnIndex(Asistencias.ID);
                int indice_grupo = asistencia.getColumnIndex(Grupos.ID_GRUPO);
                int indice_materia = asistencia.getColumnIndex(Grupos.ID_MATERIA);
                int indice_alumno = asistencia.getColumnIndex(Asistencias.ID_ALUMNO);
                int indice_fecha = asistencia.getColumnIndex(Asistencias.FECHA);
                int indice_tipo = asistencia.getColumnIndex(Asistencias.TIPO);
                int indice_activo = asistencia.getColumnIndex(Asistencias.ACTIVO);

                String sql = "INSERT INTO asistencias (" + Grupos.ID_GRUPO + "," +
                        Grupos.ID_MATERIA + "," + Asistencias.ID_ALUMNO + "," + Asistencias.FECHA +
                        "," + Asistencias.TIPO + "," + Asistencias.ACTIVO + ") VALUES";
                String ultimo = "0";

                for (asistencia.moveToFirst(); !asistencia.isAfterLast(); asistencia.moveToNext()) {
                    sql += "(" + asistencia.getString(indice_grupo) + "," + asistencia.getString(indice_materia) + "," +
                            asistencia.getString(indice_alumno) + ",'" + asistencia.getString(indice_fecha) + "','" +
                            asistencia.getString(indice_tipo) + "'," + asistencia.getString(indice_activo) + ")";

                    if (asistencia.isLast() == false) {
                        sql += ",";
                    } else {
                        ultimo = asistencia.getString(indice_id);
                    }
                }
                String fecha = senhor_formato.format(new Date());
                dbOperation.insertarSincronizacion(null,ultimo,fecha);
                System.out.println("El sql: " + sql);
                RequestParams requestParams = new RequestParams();
                requestParams.put("sql",sql);
                UPClient_Asistencia upClient_asistencia = new UPClient_Asistencia();
                upClient_asistencia.intentConnection(url,requestParams,getContext());
            }
        });

        ArrayList<ItemGrupo> items = new ArrayList<ItemGrupo>();

        for (grupos.moveToFirst(); !grupos.isAfterLast(); grupos.moveToNext()) {
            items.add(new ItemGrupo(grupos.getString(indice_nombre_grupo),
                    grupos.getString(indice_id_grupo_materia)));
        }

        gruposAdapter = new GruposAdapter(getContext(), items);
        gruposList.setAdapter(gruposAdapter);
        return root;
    }

    public class UPClient_Asistencia{

        private AsyncHttpClient client = new AsyncHttpClient();

        public void intentConnection(String URL, RequestParams params, final Context context) {
            client.post(URL, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        int actualizado = 1;
                        if (response.getInt("actualizado")==actualizado){
                            Toast.makeText(getContext(), Integer.toString(asistencia.getCount())+
                                    " actualizados", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    //NO SE RETORNO UN JSON VALIDO
                    if (throwable.getCause() instanceof ConnectTimeoutException) {
                        Toast.makeText(getContext(), "No sé que pasa :v", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

    }

}