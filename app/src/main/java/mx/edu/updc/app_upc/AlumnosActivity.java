package mx.edu.updc.app_upc;

import android.database.SQLException;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AlumnosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos);
        AlumnosFragment alumnosFragment = null;
            alumnosFragment = alumnosFragment.newInstance();
            alumnosFragment.setDatos_grupo(getIntent().getExtras().getString("id_grupo_materia"));
            getSupportFragmentManager().beginTransaction().add(R.id.activity_alumnos, alumnosFragment)
                    .commit();

    }
}
