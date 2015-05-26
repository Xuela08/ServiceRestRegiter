package es.tfg.modelo;

public class Apunte {
    private long id;
    private Aporte datosGenerales;
    private String carrera;
    private int curso;
    private String asignatura;
    private String urlDescarga;

    public Apunte() {
    }

    public Apunte(long id, Aporte datosGenerales, String carrera, int curso, String asignatura, String urlDescarga) {
        this.id = id;
        this.datosGenerales = datosGenerales;
        this.carrera = carrera;
        this.curso = curso;
        this.asignatura = asignatura;
        this.urlDescarga = urlDescarga;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Aporte getDatosGenerales() {
        return datosGenerales;
    }

    public void setDatosGenerales(Aporte datosGenerales) {
        this.datosGenerales = datosGenerales;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getUrlDescarga() {
        return urlDescarga;
    }

    public void setUrlDescarga(String urlDescarga) {
        this.urlDescarga = urlDescarga;
    }
    
    
}
