package mx.edu.updc.app_upc;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;



public class AlumnosFragment extends Fragment {

    private ListView alumnosList;
    private AlumnosAdapter alumnosAdapter;
    private DataBasesOperation dbOperation;
    private Cursor alumnos;
    public AlumnosFragment() {
        // Required empty public constructor
    }

    public static AlumnosFragment newInstance() {
        AlumnosFragment alumnosFragment = new AlumnosFragment();
        return alumnosFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //get arguments
        }
    }

    public void setDatos_grupo(String id_grupo_materia){
         dbOperation = new DataBasesOperation(getContext());
         alumnos = dbOperation.obtenerAsistencia_Alumnos(id_grupo_materia);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_alumnos, container, false);
        //instancia del listView
        alumnosList = (ListView) root.findViewById(R.id.lista_alumnos);
        Button boton_guardar = new Button(getContext());
        boton_guardar.setBackgroundColor(0);
        alumnosList.addFooterView(boton_guardar);
        ArrayList<ItemAlumno> items = new ArrayList<ItemAlumno>();

        int indice_matricula = alumnos.getColumnIndex(Alumnos.MATRICULA);
        int indice_nombre = alumnos.getColumnIndex(Alumnos.NOMBRE);
        int indice_id_programa = alumnos.getColumnIndex(Alumnos.ID_PROGRAMA);
        int indice_programa = alumnos.getColumnIndex(Alumnos.NOMBRE_PROGRAMA);
        int indice_estado_lista = alumnos.getColumnIndex(Asistencias.TIPO);
        int indice_id_asistencia = alumnos.getColumnIndex(Asistencias.ID);

        for(alumnos.moveToFirst(); !alumnos.isAfterLast(); alumnos.moveToNext()){
            items.add(new ItemAlumno(alumnos.getString(indice_matricula)+" "+alumnos.getString(indice_nombre),
                    alumnos.getString(indice_programa),dbOperation.obtenerImagen(
                    alumnos.getInt(indice_id_programa)),alumnos.getString(indice_estado_lista),
                    alumnos.getString(indice_id_asistencia)));
        }

        FloatingActionButton fab_guardar = (FloatingActionButton) root.findViewById(R.id.fab_guardar);
        fab_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String datos_temporales[][] = AlumnosAdapter.datos;
                    dbOperation.getDb().beginTransaction();
                    for (int i=0; i<datos_temporales.length; i++){
                        dbOperation.actualizarAsistencia(
                                datos_temporales[i][AlumnosAdapter.columna_id],
                                datos_temporales[i][AlumnosAdapter.columna_tipo_asistencia]
                        );
                    }
                    dbOperation.getDb().setTransactionSuccessful();
                    Toast.makeText(view.getContext(),"Guardado",Toast.LENGTH_SHORT).show();
                }catch (SQLException e){
                    e.printStackTrace();    //ERROR AL ACTUALIZAR
                    Toast.makeText(view.getContext(),"Error al guardar",Toast.LENGTH_SHORT).show();
                }finally {
                    dbOperation.getDb().endTransaction();
                }

            }
        });

        alumnosAdapter = new AlumnosAdapter(getContext(),items);
        alumnosList.setAdapter(alumnosAdapter);
        return root;
    }

}
