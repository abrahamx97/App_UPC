package mx.edu.updc.app_upc;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;



public class AlumnosFragment extends Fragment {

    ListView alumnosList;
    AlumnosAdapter alumnosAdapter;
    private String id_grupo_materia;
    private String id_grupo;
    private String id_materia;

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

    public void setDatos_grupo(String id_grupo_materia, String id_grupo, String id_materia){
        this.id_grupo=id_grupo;
        this.id_materia=id_materia;
        this.id_grupo_materia=id_grupo_materia;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_alumnos, container, false);
        DataBasesOperation dbOperation = new DataBasesOperation(getContext());

        //instancia del listView
        alumnosList = (ListView) root.findViewById(R.id.lista_alumnos);
        ArrayList<ItemAlumno> items = new ArrayList<ItemAlumno>();

        Cursor alumnos = dbOperation.obtenerAlumnos(id_grupo_materia);
        int indice_matricula = alumnos.getColumnIndex(Alumnos.MATRICULA);
        int indice_nombre = alumnos.getColumnIndex(Alumnos.NOMBRE);
        //int indice_programa = alumnos.getColumnIndex(Alumnos.)
        for(alumnos.moveToFirst(); !alumnos.isAfterLast(); alumnos.moveToNext()){
            items.add(new ItemAlumno(alumnos.getString(indice_matricula)+" "+alumnos.getString(indice_nombre),
                    "Software",R.drawable.app_development));
        }

        alumnosAdapter = new AlumnosAdapter(getContext(),items);
        alumnosList.setAdapter(alumnosAdapter);
        return root;
    }

}
