package es.tfg.modelo;

import java.util.Date;

public class Aporte {
    private long id;
    private String titulo;
    private String descripcion;
    private Date f_creacion;
    private String URLimagen;
    private String propietario; //usuario
    private String tipo;

    public Aporte() {
    }

    public Aporte(long id, String titulo, String deescripcion, Date f_creacion, String URLaporte, String propietario, String tipo) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = deescripcion;
        this.f_creacion = f_creacion;
        this.URLimagen = URLaporte;
        this.propietario = propietario;
        this.tipo = tipo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getF_creacion() {
        return f_creacion;
    }

    public void setF_creacion(Date f_creacion) {
        this.f_creacion = f_creacion;
    }
    
    public String getURLimagen() {
        return URLimagen;
    }

    public void setURLimagen(String URLimagen) {
        this.URLimagen = URLimagen;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}
