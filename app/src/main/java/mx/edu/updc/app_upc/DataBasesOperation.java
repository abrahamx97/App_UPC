package mx.edu.updc.app_upc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import static android.R.attr.animationResolution;
import static android.R.attr.id;

/**
 * Clase auxiliar que implementa a {@link DataBaseHelper} para llevar a cabo el CRUD
 * sobre las entidades existentes.
 */

public class DataBasesOperation {

    private static DataBaseHelper baseDatos;

    //private static DataBasesOperation instancia = new DataBasesOperation();

    public DataBasesOperation(Context contexto) {
        if (baseDatos == null) {
            baseDatos = new DataBaseHelper(contexto);
        }
    }

    public Cursor obtenerMaestro() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(Tablas.MAESTROS);
        return builder.query(db,columMaestros,null,null,null,null,null);
    };

    public Cursor obtenerAlumnos() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(Tablas.ALUMNOS);
        return builder.query(db,columAlumnos,null,null,null,null,null);
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

    public void insertarAlumno(String id , String id_alumno, String id_grupo_materia, String nombre, String matricula, String activo){
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        ContentValues valores = new ContentValues();
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

