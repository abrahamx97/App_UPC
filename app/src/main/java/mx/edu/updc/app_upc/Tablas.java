package mx.edu.updc.app_upc;

import java.util.UUID;

/**
 * Created by Usuario on 08/12/2016.
 */

public class Tablas {
    static final String MAESTROS = "maestros";
    static final String GRUPOS = "grupos";
    static final String ALUMNOS = "alumnos";
    static final String ASISTENCIAS = "asistencia";
    static final String HORARIOS = "horarios";
    static final String SINCRONIZACIONES = "sincronizacion";
}

class Sincronizaciones {
    static final String ID = "_id";
    static final String ULTIMO = "ultimo";
    static final String FECHA = "fecha";

}

class Maestros {
    static final String ID = "_id";
    static final String ID_MAESTRO = "id_maestro";
    static final String NOMBRE = "nombre";
    static final String USUARIO = "usuario";
    static final String CONTRASENA = "contrasena";
}

class Grupos {
    static final String ID = "_id";
    static final String ID_GRUPO = "id_grupo";
    static final String ID_MATERIA = "id_materia";
    static final String NOMBRE = "nombre";
}

class Alumnos {
    static final String ID = "_id"; //ID AUTOINCREMENTABLE
    static final String ID_PROGRAMA = "id_programa";
    static final String NOMBRE_PROGRAMA = "programa";
    static final String ID_ALUMNO = "id_alumno";
    static final String ID_GRUPO_MATERIA = "id_grupo_materia";
    static final String NOMBRE = "nombre";
    static final String MATRICULA = "matricula";
    static final String ACTIVO = "activo";
}

class Asistencias {
    static final String ID = "_id";
    static final String ID_GRUPO_MATERIA = "id_grupo_materia";
    static final String ID_ALUMNO = "id_alumno";
    static final String TIPO = "tipo";
    static final String FECHA = "fecha";
    static final String ACTIVO = "activo";
}

class Horarios {
    static final String ID = "_id";
    static final String ID_GRUPO = "ide_grupo";
    static final String ID_MATERIA = "id_materia";
    static final String DIA = "dia";
    static final String HORA = "hora";
}


