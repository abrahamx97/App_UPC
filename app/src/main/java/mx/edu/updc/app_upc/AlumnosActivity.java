package mx.edu.updc.app_upc;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class AlumnosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos);
        String id_grupo_materia = getIntent().getExtras().getString("id_grupo_materia");
        DataBasesOperation dbOperation = new DataBasesOperation(this);
        Cursor datosAlumnos = dbOperation.obtenerAlumnos(id_grupo_materia);

    }
}
