/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.tfg.modelo;

import java.util.Date;

/**
 *
 * @author Usuario
 */
public class Token {
    private String token;
    private Date caducidad;
    private String idUsuario;

    public Token() {
    }
    
    public Token(String token, Date caducidad, String idUsuario) {
        this.token = token;
        this.caducidad = caducidad;
        this.idUsuario = idUsuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(Date caducidad) {
        this.caducidad = caducidad;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    
}
