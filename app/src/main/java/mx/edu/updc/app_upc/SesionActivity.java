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

public class SesionActivity extends AppCompatActivity {
    ArrayList<String> id_grupos = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);
        DataBasesOperation dbOperation = new DataBasesOperation(this);
        ListView grupos = (ListView) findViewById(R.id.lista_grupos);
        ArrayList<String> nombreGrupos = new ArrayList<String>();

        Cursor cursorDatos = dbOperation.obtenerGrupos();
        final Intent alumnosActivity = new Intent(this, AlumnosActivity.class);
        int indiceNombre = cursorDatos.getColumnIndex("nombre");
        int indiceID = cursorDatos.getColumnIndex("_id");

        String name,id;
        for(cursorDatos.moveToFirst(); !cursorDatos.isAfterLast(); cursorDatos.moveToNext()){
            name = cursorDatos.getString(indiceNombre);
            id = cursorDatos.getString(indiceID);
            nombreGrupos.add(name);
            id_grupos.add(id);
        }


        ArrayAdapter adaptadorDatosCursor = new ArrayAdapter(this,android.R.layout.simple_list_item_1, nombreGrupos);
        grupos.setAdapter(adaptadorDatosCursor);;

        grupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alumnosActivity.putExtra("id_grupo_materia", id_grupos.get(i));
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
