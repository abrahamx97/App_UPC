package mx.edu.updc.app_upc;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 15/12/16.
 */

public class AdapterGrupos extends ArrayAdapter<ItemGrupo> {
    public AdapterGrupos(Context context, List<ItemGrupo> objects){
        super(context,0,objects);
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
                    R.layout.lista_grupos_item,
                    parent,
                    false
            );
        }

        TextView text_grupo = (TextView) convertView.findViewById(R.id.text_grupo);
        ItemGrupo itemGrupo = getItem(position);

        text_grupo.setText(itemGrupo.getNombre_grupo());
        convertView.setTag(itemGrupo.getId_grupo_materia());
        return convertView;
    }

}



