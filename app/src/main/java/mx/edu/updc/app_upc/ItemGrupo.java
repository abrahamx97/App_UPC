package mx.edu.updc.app_upc;

/**
 * Created by root on 15/12/16.
 */

public class ItemGrupo {
    private String nombre_grupo;
    private String id_grupo_materia;
    ItemGrupo(String nombre_grupo, String id_grupo_materia){
        this.nombre_grupo=nombre_grupo;
        this.id_grupo_materia=id_grupo_materia;
    }

    public String getNombre_grupo() {
        return nombre_grupo;
    }

    public String getId_grupo_materia(){
        return id_grupo_materia;
    }
}
