package mx.edu.updc.app_upc;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SesionActivity extends AppCompatActivity {

    TextView textMaestro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);
        DataBasesOperation dbOperation = new DataBasesOperation(this);
        //String id_maestro = getIntent().getExtras().getString("id_maestro");
        /*        Maestro maestro = (Maestro) getIntent().getExtras().getSerializable("maestro");
        textMaestro = (TextView) findViewById(R.id.textMaestro);
        textMaestro.setText(maestro.nombre_maestro);*/

        Cursor grupos = dbOperation.obtenerAlumnos();
        String campos[] = {"nombre"};
        int to[] = {android.R.id.text1};
        SimpleCursorAdapter gruposAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, grupos, campos, to,0);


        ListView lista = (ListView) findViewById(R.id.lista_grupos);
        lista.setAdapter(gruposAdapter);


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
