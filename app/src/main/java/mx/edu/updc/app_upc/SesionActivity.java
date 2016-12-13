package mx.edu.updc.app_upc;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SesionActivity extends AppCompatActivity {
    ArrayList<String> id_grupo_materia = new ArrayList<String>();
    ArrayList<String> id_grupo = new ArrayList<String>();
    ArrayList<String> id_materia = new ArrayList<String>();

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
        int indiceId_grupo = cursorDatos.getColumnIndex(Grupos.ID_GRUPO);
        int indiceId_materia = cursorDatos.getColumnIndex(Grupos.ID_MATERIA);


        for(cursorDatos.moveToFirst(); !cursorDatos.isAfterLast(); cursorDatos.moveToNext()){
            nombreGrupos.add(cursorDatos.getString(indiceNombre));
            id_grupo_materia.add(cursorDatos.getString(indiceID));
            id_grupo.add(cursorDatos.getString(indiceId_grupo));
            id_materia.add(cursorDatos.getString(indiceId_materia));
        }


        ArrayAdapter adaptadorDatosCursor = new ArrayAdapter(this,android.R.layout.simple_list_item_1, nombreGrupos);
        grupos.setAdapter(adaptadorDatosCursor);;

        grupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alumnosActivity.putExtra("id_grupo_materia", id_grupo_materia.get(i));
                alumnosActivity.putExtra("id_grupo",id_grupo.get(i));
                alumnosActivity.putExtra("id_materia",id_materia.get(i));

                Cursor alumnos = dbOperation.obtenerAlumnos(id_grupo_materia.get(i));
                int columna_id_alumno = alumnos.getColumnIndex(Alumnos.ID_ALUMNO);

                try{
                    dbOperation.getDb().beginTransaction();
                    for(alumnos.moveToFirst(); alumnos.isAfterLast(); alumnos.moveToNext()){
                        dbOperation.insertarAsistencias(null, id_grupo.get(i),id_materia.get(i),
                                alumnos.getString(columna_id_alumno),"1"," now() ","A","1");
                    }

                    dbOperation.getDb().setTransactionSuccessful();
                }finally {
                    dbOperation.getDb().endTransaction();
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
