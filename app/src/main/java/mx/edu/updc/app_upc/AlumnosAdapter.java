package mx.edu.updc.app_upc;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by root on 12/12/16.
 */

public class AlumnosAdapter extends ArrayAdapter<ItemAlumno> {
    Typeface face;
    final private String ASISTENCIA="A";
    final private String RETARDO="R";
    final private String FALTA="F";
    final private int num_columnas = 2; //REPRESENTA EL NUM DE CAMPOS NECESARIOS EN ASISTENCIA:
                                            // ID_AUTO INCREMENTABLE DE ASISTENCIAS, TIPO DE ASISTENCIA
    public static final int columna_id = 0;
    public static final int columna_tipo_asistencia = 1;
    public static String[][] datos;

    public AlumnosAdapter(Context context, List<ItemAlumno> objects){
        super(context,0,objects);
        datos=new String[objects.size()][num_columnas];
        face = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Obtener inflater
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );

        //se comprueba si existe el View actual
        if (convertView==null){
            convertView = inflater.inflate(
                    R.layout.lista_alumnos_item,
                    parent,
                    false
            );
        }

        //referencias UI
        ImageView avatar = (ImageView) convertView.findViewById(R.id.img_avatar);
        TextView mat_alumno = (TextView) convertView.findViewById(R.id.text_mat_alumno);
        TextView programa_edu = (TextView) convertView.findViewById(R.id.text_programa);

        final Button boton_asistencia = (Button) convertView.findViewById(R.id.boton_asistencia);
        final Button boton_retardo = (Button) convertView.findViewById(R.id.boton_retardo);
        final Button boton_falta = (Button) convertView.findViewById(R.id.boton_falta);

        //SE AÑADE UN TAG IDENTIFICATIVO DE CADA POSICION EN EL ITEM DE LISTA
        boton_asistencia.setTag(position);
        boton_falta.setTag(position);
        boton_retardo.setTag(position);

        //SE AÑADEN LOS ICONOS
        boton_asistencia.setTypeface(face);
        boton_asistencia.setText("\uf00c");
        boton_retardo.setTypeface(face);
        boton_retardo.setText("\uf017");
        boton_falta.setTypeface(face);
        boton_falta.setText("\uf00d");

        boton_asistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boton_asistencia.getCurrentTextColor() != Color.parseColor("#1e9630")){
                    boton_asistencia.setTextColor(Color.parseColor("#1e9630"));
                    boton_falta.setTextColor(Color.parseColor("#d3d1d1"));
                    boton_retardo.setTextColor(Color.parseColor("#d3d1d1"));
                }
                datos[Integer.parseInt(boton_asistencia.getTag().toString())][columna_tipo_asistencia]=ASISTENCIA;
            }
        });
        boton_falta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boton_falta.getCurrentTextColor() != Color.parseColor("#e21e00")){
                    boton_asistencia.setTextColor(Color.parseColor("#d3d1d1"));
                    boton_falta.setTextColor(Color.parseColor("#e21e00"));
                    boton_retardo.setTextColor(Color.parseColor("#d3d1d1"));
                }

                datos[Integer.parseInt(boton_falta.getTag().toString())][columna_tipo_asistencia]=FALTA;
            }
        });
        boton_retardo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boton_retardo.getCurrentTextColor() != Color.parseColor("#ffc300")){
                    boton_asistencia.setTextColor(Color.parseColor("#d3d1d1"));
                    boton_falta.setTextColor(Color.parseColor("#d3d1d1"));
                    boton_retardo.setTextColor(Color.parseColor("#ffc300"));
                }
                datos[Integer.parseInt(boton_retardo.getTag().toString())][columna_tipo_asistencia]=RETARDO;
            }
        });

        //item de alumno actual
        ItemAlumno itemAlumno = getItem(position);


        //setup
        Glide.with(getContext()).load(itemAlumno.getImagen()).into(avatar);
        mat_alumno.setText(itemAlumno.getMatricula_nombre());
        programa_edu.setText(itemAlumno.getPrograma_educativo());
        datos[position][columna_id]=itemAlumno.getId_en_lista();

        if(datos[position][columna_tipo_asistencia]==null){
            datos[position][columna_tipo_asistencia]=itemAlumno.getEstado_en_lista();
        }

        //SE COLOREA EL BOTON REPRESENTANTE A SU TIPO DE ASISTENCIA
        boton_asistencia.setTextColor(Color.parseColor("#d3d1d1"));
        boton_retardo.setTextColor(Color.parseColor("#d3d1d1"));
        boton_falta.setTextColor(Color.parseColor("#d3d1d1"));
        switch (datos[position][columna_tipo_asistencia]){
            case ASISTENCIA:
                boton_asistencia.setTextColor(Color.parseColor("#1e9630"));
                break;
            case RETARDO:
                boton_retardo.setTextColor(Color.parseColor("#ffd400"));
                break;
            case FALTA:
                boton_falta.setTextColor(Color.parseColor("#e21e00"));
                break;
        }

        return convertView;
    }
}
