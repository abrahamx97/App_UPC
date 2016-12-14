package mx.edu.updc.app_upc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.design.widget.TabLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Clase auxiliar que implementa a {@link DataBaseHelper} para llevar a cabo el CRUD
 * sobre las entidades existentes.
 */

public class DataBasesOperation {

    private static DataBaseHelper baseDatos;

    //private static DataBasesOperation instancia = new DataBasesOperation();

    private String obtenerHoy(){
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        return "'"+dateFormat1.format(cal.getTime())+"'";

    }

    private  String obtenerManana(){
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,+1);
        return "'"+dateFormat1.format(cal.getTime())+"'";
    }

    public DataBasesOperation(Context contexto) {
        if (baseDatos == null) {
            baseDatos = new DataBaseHelper(contexto);
        }
    }

    public int obtenerImagen(int i){
        int imagen;
        switch(i){
            //Biotecnología
            case 1:
                imagen = R.drawable.bio;
                break;
            //Mecánica Automotriz
            case 2:
                imagen = R.drawable.auto;
                break;
            //Mecatrónica
            case 3:
                imagen = R.drawable.meca;
                break;
            //Electrónica y Telecom
            case 4:
                imagen = R.drawable.elec;
                break;
            //Software
            case 5:
                imagen = R.drawable.soft;
                break;
            //Geofísica Petrolera
            case 6:
                imagen = R.drawable.geo;
                break;
            default:
                imagen = 0;
        }
        return imagen;
    }

    public boolean existeLista(String id_grupo_materia){
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String[] select ={"1 as existe"};
        builder.setTables(Tablas.ASISTENCIAS);

        if (!builder.query(db,select,Asistencias.FECHA+">"+obtenerHoy()+" AND "
                +Asistencias.FECHA+"<"+obtenerManana()+" AND "+
                Asistencias.ID_GRUPO_MATERIA+"="+id_grupo_materia,null,null,null,null).moveToFirst()){
            return false;
        }else{
            return true;
        }
    }


    public Cursor loginMaestro(String usuario, String contrasena){
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String[] select = new String[]{"1 as logeado"};
        builder.setTables(Tablas.MAESTROS);
        return builder.query(db,select,"usuario='"+usuario+"' AND contrasena='"+contrasena+"'",null,null,null,null);
    }

    public Cursor obtenerMaestro() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(Tablas.MAESTROS);
        return builder.query(db,columMaestros,null,null,null,null,null);
    };

    public Cursor obtenerAlumnos(String id_grupo_materia) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(Tablas.ALUMNOS);
        return builder.query(db,columAlumnos,"id_grupo_materia="+id_grupo_materia,null,null,null,null);
    };

    public Cursor obtenerGrupos() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(Tablas.GRUPOS);
        return builder.query(db,columGrupos,null,null,null,null,null);
    };

    private final String[] columMaestros = new String[]{Maestros.ID, Maestros.ID_MAESTRO, Maestros.NOMBRE, Maestros.USUARIO
                                                        ,Maestros.CONTRASENA};
    private final String[] columAlumnos = new String[]{Alumnos.ID,Alumnos.ID_ALUMNO, Alumnos.ID_GRUPO_MATERIA, Alumnos.NOMBRE
                                                        ,Alumnos.MATRICULA, Alumnos.ACTIVO};
    private final String[] columGrupos = new String[]{Grupos.ID, Grupos.ID_GRUPO,Grupos.ID_MATERIA
                                                        ,Grupos.NOMBRE};
    private final String[] columAlumnosAsis = new String[]{Tablas.ALUMNOS+"."+Alumnos.ID,
            Tablas.ALUMNOS+"."+Alumnos.ID_ALUMNO,Tablas.ALUMNOS+"."+Alumnos.ID_GRUPO_MATERIA,
            Tablas.ALUMNOS+"."+Alumnos.NOMBRE,Tablas.ALUMNOS+"."+Alumnos.ID_PROGRAMA
            , Tablas.ALUMNOS+"."+Alumnos.NOMBRE_PROGRAMA,Tablas.ALUMNOS+"."+Alumnos.MATRICULA,
            Tablas.ALUMNOS+"."+Alumnos.ACTIVO, Tablas.ASISTENCIAS+"."+Asistencias.TIPO};

    public void insertarMaestro(String id, String id_maestro, String nombre, String usuario, String contrasena) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(Maestros.ID, id);
        valores.put(Maestros.ID_MAESTRO, id_maestro);
        valores.put(Maestros.NOMBRE, nombre);
        valores.put(Maestros.USUARIO,usuario);
        valores.put(Maestros.CONTRASENA,contrasena);
        db.insertOrThrow(Tablas.MAESTROS, null, valores);
    }

    public Cursor obtenerAlumnosAsis(String id_grupo_materia) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String buscar = Tablas.ALUMNOS+" INNER JOIN "+Tablas.ASISTENCIAS+
                " ON "+Tablas.ALUMNOS+".id_alumno = "+Tablas.ASISTENCIAS+".id_alumno";
        builder.setTables(buscar);
        return builder.query(db,columAlumnosAsis,Tablas.ALUMNOS+".id_grupo_materia="+id_grupo_materia+
                " AND "+Tablas.ASISTENCIAS+".id_grupo_materia="+id_grupo_materia+" AND "+
                Asistencias.FECHA+">"+obtenerHoy()+" AND "+Asistencias.FECHA+"<"+obtenerManana(),
                null,null,null,null);
    }

    public void actualizarAsistencia(String id, String tipo, String fecha) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(Asistencias.TIPO, tipo);
        valores.put(Asistencias.FECHA, fecha);

        String whereClause = String.format("%s=?", Asistencias.ID);
        String[] whereArgs = {id};

        db.update(Tablas.ASISTENCIAS, valores, whereClause, whereArgs);
    }


    public void insertarAsistencias(String id,  String id_grupo_materia, String id_alumno,
                                     String fecha, String tipo, String activo){
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(Asistencias.ID,id);
        valores.put(Asistencias.ID_GRUPO_MATERIA,id_grupo_materia);
        valores.put(Asistencias.ID_ALUMNO,id_alumno);
        valores.put(Asistencias.FECHA, fecha);
        valores.put(Asistencias.TIPO, tipo);
        valores.put(Asistencias.ACTIVO,activo);
        db.insertOrThrow(Tablas.ASISTENCIAS,null,valores);
    }

    public void insertarAlumno(String id , String id_alumno, String id_grupo_materia, String nombre,
                               String matricula, String id_programa, String programa, String activo){
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(Alumnos.ID_PROGRAMA,id_programa);
        valores.put(Alumnos.NOMBRE_PROGRAMA,programa);
        valores.put(Alumnos.ID, id);
        valores.put(Alumnos.ID_ALUMNO, id_alumno);
        valores.put(Alumnos.NOMBRE,nombre);
        valores.put(Alumnos.MATRICULA,matricula);
        valores.put(Alumnos.ACTIVO, activo);
        valores.put(Alumnos.ID_GRUPO_MATERIA, id_grupo_materia);

        db.insertOrThrow(Tablas.ALUMNOS, null, valores);

    }

    public void insertarGrupo(String id_grupo_materia, String id_grupo,String id_materia, String nombre){
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(Grupos.ID, id_grupo_materia);
        valores.put(Grupos.ID_GRUPO, id_grupo);
        valores.put(Grupos.ID_MATERIA,id_materia);
        valores.put(Grupos.NOMBRE,nombre);

        db.insertOrThrow(Tablas.GRUPOS, null, valores);

    }
    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }
}

