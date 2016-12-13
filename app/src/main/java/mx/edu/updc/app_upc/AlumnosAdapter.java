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
    public AlumnosAdapter(Context context, List<ItemAlumno> objects){
        super(context,0,objects);
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
        //programa_edu.setTextSize(20);
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
                    boton_falta.setTextColor(Color.parseColor("#000000"));
                    boton_retardo.setTextColor(Color.parseColor("#000000"));
                }
                boton_asistencia.getTag();
            }
        });
        boton_falta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boton_falta.getCurrentTextColor() != Color.parseColor("#e21e00")){
                    boton_asistencia.setTextColor(Color.parseColor("#000000"));
                    boton_falta.setTextColor(Color.parseColor("#e21e00"));
                    boton_retardo.setTextColor(Color.parseColor("#000000"));
                }

                boton_falta.getTag();
            }
        });
        boton_retardo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boton_retardo.getCurrentTextColor() != Color.parseColor("#ffd400")){
                    boton_asistencia.setTextColor(Color.parseColor("#000000"));
                    boton_falta.setTextColor(Color.parseColor("#000000"));
                    boton_retardo.setTextColor(Color.parseColor("#ffd400"));
                }
                boton_retardo.getTag();
            }
        });

        //item de alumno actual
        ItemAlumno itemAlumno = getItem(position);

        //setup
        Glide.with(getContext()).load(itemAlumno.getImagen()).into(avatar);
        mat_alumno.setText(itemAlumno.getMatricula_nombre());
        programa_edu.setText(itemAlumno.getPrograma_educativo());

        return convertView;
    }
}
