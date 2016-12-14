package mx.edu.updc.app_upc;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SesionActivity extends AppCompatActivity {
    ArrayList<String> id_grupo_materia = new ArrayList<String>();
    private  final SimpleDateFormat senhor_formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);
        final DataBasesOperation dbOperation = new DataBasesOperation(this);
        ListView grupos = (ListView) findViewById(R.id.lista_grupos);
        ArrayList<String> nombreGrupos = new ArrayList<String>();

        Cursor cursorDatos = dbOperation.obtenerGrupos();
        final Intent alumnosActivity = new Intent(this, AlumnosActivity.class);
        int indiceNombre = cursorDatos.getColumnIndex(Grupos.NOMBRE);
        int indiceID = cursorDatos.getColumnIndex(Grupos.ID);


        for(cursorDatos.moveToFirst(); !cursorDatos.isAfterLast(); cursorDatos.moveToNext()){
            nombreGrupos.add(cursorDatos.getString(indiceNombre));
            id_grupo_materia.add(cursorDatos.getString(indiceID));
        }


        ArrayAdapter adaptadorDatosCursor = new ArrayAdapter(this,android.R.layout.simple_list_item_1, nombreGrupos);
        grupos.setAdapter(adaptadorDatosCursor);;

        grupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alumnosActivity.putExtra("id_grupo_materia", id_grupo_materia.get(i));

                Cursor alumnos = dbOperation.obtenerAlumnos(id_grupo_materia.get(i));
                int columna_id_alumno = alumnos.getColumnIndex(Alumnos.ID_ALUMNO);
                String now = senhor_formato.format(new Date());


                if(!dbOperation.existeLista(id_grupo_materia.get(i))) {
                    try {
                        dbOperation.getDb().beginTransaction();
                        for (alumnos.moveToFirst(); !alumnos.isAfterLast(); alumnos.moveToNext()) {
                            dbOperation.insertarAsistencias(null, id_grupo_materia.get(i),
                                    alumnos.getString(columna_id_alumno),now, "A", "1");
                        }

                        dbOperation.getDb().setTransactionSuccessful();
                    } finally {
                        dbOperation.getDb().endTransaction();
                    }
                }

                startActivity(alumnosActivity);
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
