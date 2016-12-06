package mx.edu.updc.app_upc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class SesionActivity extends AppCompatActivity {

    TextView textMaestro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);
        Maestro maestro = (Maestro) getIntent().getExtras().getSerializable("maestro");
        textMaestro = (TextView) findViewById(R.id.textMaestro);
        textMaestro.setText(maestro.nombre_maestro);
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
