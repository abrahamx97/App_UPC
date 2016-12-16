package mx.edu.updc.app_upc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

/**
 * Clase que administra la conexión de la base de datos y su estructuración
 */
/**
 * Created by Usuario on 08/12/2016.
 */


public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String NOMBRE_BASE_DATOS = "appupc.db";
    public static final String DATA_BASE_PATH = "/data/data/mx.edu.updc.app_upc/databases/";

    private static final int VERSION_ACTUAL = 1;

    private final Context contexto;




    interface Referencias {

        String ID_GRUPO_MATERIA= String.format("REFERENCES %s(%s)",
                Tablas.GRUPOS, Grupos.ID);

        String ID_GRUPO = String.format("REFERENCES %s(%s)",
                Tablas.GRUPOS, Grupos.ID_GRUPO);

        String ID_MATERIA = String.format("REFERENCES %s(%s)",
                Tablas.GRUPOS, Grupos.ID_MATERIA);

        String ID_ALUMNO = String.format("REFERENCES %s(%s)",
                Tablas.ALUMNOS, Alumnos.ID_ALUMNO);

        String ID_MAESTRO = String.format("REFERENCES %s(%s)",
                Tablas.MAESTROS, Maestros.ID_MAESTRO);

    }

    public DataBaseHelper(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s INTEGER NOT NULL,%s TEXT,%s TEXT,%s TEXT)",
                Tablas.MAESTROS, Maestros.ID,
                Maestros.ID_MAESTRO, Maestros.NOMBRE, Maestros.USUARIO, Maestros.CONTRASENA));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY," +
                        "%s INTEGER NOT NULL,%s INTEGER NOT NULL,%s TEXT)",
                Tablas.GRUPOS, Grupos.ID,
                Grupos.ID_GRUPO, Grupos.ID_MATERIA, Grupos.NOMBRE));

        //Insertar en DataBaseHelper
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s INTEGER NOT NULL,%s DATETIME)",
                Tablas.SINCRONIZACIONES, Sincronizaciones.ID,
                Sincronizaciones.ULTIMO, Sincronizaciones.FECHA));
        //Creamos Sincronizacion 0

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, " +
                        " %s TEXT, %s INTEGER NOT NULL,%s TEXT,%s TEXT,%s INTEGER, %s INTEGER %s)",
                Tablas.ALUMNOS, Alumnos.ID, Alumnos.ID_PROGRAMA, Alumnos.NOMBRE_PROGRAMA,
                Alumnos.ID_ALUMNO, Alumnos.NOMBRE, Alumnos.MATRICULA, Alumnos.ACTIVO,
                Alumnos.ID_GRUPO_MATERIA, Referencias.ID_GRUPO_MATERIA));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s INTEGER NOT NULL %s," +
                        "%s INTEGER NOT NULL ,%s TEXT,%s DATETIME,%s INTEGER)",
                Tablas.ASISTENCIAS, Asistencias.ID,
                Asistencias.ID_GRUPO_MATERIA, Referencias.ID_GRUPO_MATERIA, Asistencias.ID_ALUMNO,
                Asistencias.TIPO, Asistencias.FECHA, Asistencias.ACTIVO));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s INTEGER NOT NULL %s,%s INTEGER NOT NULL %s,%s TEXT,%s TEXT)",
                Tablas.HORARIOS, Horarios.ID,
                Horarios.ID_GRUPO, Referencias.ID_GRUPO, Horarios.ID_MATERIA, Referencias.ID_MATERIA,
                Horarios.DIA, Horarios.HORA));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Tablas.MAESTROS);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.GRUPOS);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.ALUMNOS);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.ASISTENCIAS);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.HORARIOS);

        onCreate(db);
    }


}