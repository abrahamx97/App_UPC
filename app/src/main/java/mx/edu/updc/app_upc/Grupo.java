package mx.edu.updc.app_upc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 6/12/16.
 */

public class Grupo implements Serializable{

    String nombre_grupo;
    int id_grupo,id_materia;
    ArrayList<Alumno> alumnos = new ArrayList<Alumno>();

    Grupo(JSONObject grupo) throws JSONException {
        JSONArray alumnos = grupo.optJSONArray("alumnos");
        nombre_grupo=grupo.getString("nombre_grupo");
        id_grupo=grupo.getInt("id_grupo");
        id_materia=grupo.getInt("id_materia");

        for (int i=0; i<alumnos.length(); i++){
            this.alumnos.add(new Alumno(alumnos.getJSONObject(i)));
        }
    }
}

class Alumno implements Serializable{

    String nombre_alumno, matricula,programa;
    int id_alumno;

    Alumno(JSONObject alumno) throws JSONException {
        nombre_alumno=alumno.getString("nombre_alumno");
        matricula=alumno.getString("matricula");
        programa=alumno.getString("programa");
        id_alumno=alumno.getInt("id_alumno");

    }
}
