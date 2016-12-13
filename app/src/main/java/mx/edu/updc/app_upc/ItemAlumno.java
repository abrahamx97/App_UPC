package mx.edu.updc.app_upc;

/**
 * Created by root on 12/12/16.
 */

public class ItemAlumno {

    private String matricula_nombre;
    private String programa_educativo;
    private int imagen;

    public ItemAlumno(String matricula_nombre,  String programa_educativo, int imagen) {
        this.matricula_nombre = matricula_nombre;
        this.programa_educativo = programa_educativo;
        this.imagen = imagen;
    }

    public String getMatricula_nombre() {
        return matricula_nombre;
    }

    public void setMatricula_nombre(String matricula_nombre) {
        this.matricula_nombre = matricula_nombre;
    }

    public String getPrograma_educativo() {
        return programa_educativo;
    }

    public void setPrograma_educativo(String programa_educativo) {
        this.programa_educativo = programa_educativo;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "ItemAlumno{" +
                ", Mat_nombre='" + matricula_nombre + '\'' +
                ", Programa='" +  programa_educativo+ '\'' +
                '}';
    }
}
