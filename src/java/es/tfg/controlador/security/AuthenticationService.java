package es.tfg.controlador.security;

import es.tfg.modelo.PersonasDAO;
import es.tfg.modelo.TokenDAO;
import java.io.IOException;

import java.util.Base64;
import java.util.StringTokenizer;

public class AuthenticationService {

    private PersonasDAO personaDAO;
    private TokenDAO tokenDAO;

    public String authenticate(String authCredentials) {
        String usuarioToken = null;
        tokenDAO = new TokenDAO();
        if (null == authCredentials) {
            return usuarioToken;
        }
        usuarioToken = tokenDAO.verificaYusuario(authCredentials);
        
        
        return usuarioToken;

    }

    public boolean authoritate(String usuToken, String peticion, String url) {

        if (null == usuToken) {
            return false;
        }
        

        personaDAO = new PersonasDAO();
        boolean isAdmin = personaDAO.esAdmin(usuToken);
        if (isAdmin) {
            return true;
        } else {
            //reglas de autorizacion
            String raizUrl = "/personas";
            
            boolean b1 = url.equals(raizUrl+"/"+usuToken);
            //Get /personas
            if(peticion.equals("GET") && url.equals(raizUrl)){
                return true;
            }else if((peticion.equals("PUT") || peticion.equals("DELETE") || peticion.equals("GET") )&& url.equals(raizUrl+"/"+usuToken) ){
                return true; //PUT-DELETE-GET /personas/{username} <-debe coincidir url con usuario
            }else{
                return false;// RESTO DE OPCIONES NO PERMITIDAS
            }
//            String[] peticionURL = url.split("/");
//            String s = peticionURL[0];
//            String t = peticionURL[1];
//            String r = peticionURL[2];
        }
    }
}
